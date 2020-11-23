package com.flance.jdbc.jpa.service;

import com.alibaba.fastjson.JSONObject;
import com.flance.jdbc.jpa.common.Operator;
import com.flance.jdbc.jpa.page.PageResponse;
import com.flance.jdbc.jpa.common.QueryLocal;
import com.flance.jdbc.jpa.common.QueryUtils;
import com.flance.jdbc.jpa.dao.BaseDao;
import com.flance.jdbc.jpa.parser.BaseParser;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * service封装
 * @param <PO>  po
 * @param <DTO> dto
 * @param <VO>  vo
 * @param <DO>  do
 * @param <ID>  主键
 */
public abstract class BaseDomainService<PO, DTO, VO, DO, ID extends Serializable> implements IBaseDomainService<PO, DTO, ID> {

    private BaseDao<PO, ID> baseDao;

    private BaseParser<PO, DTO, VO, DO> baseParser;

    protected void setBaseDao(BaseDao<PO, ID> baseDao) {
        this.baseDao = baseDao;
    }

    public void setBaseParser(BaseParser<PO, DTO, VO, DO> baseParser) {
        this.baseParser = baseParser;
    }

    @Override
    public DTO save(DTO t) {
        setId(t);
        PO po = this.baseDao.save(baseParser.parseDto2Po(t));
        return baseParser.parsePo2Dto(po);
    }

    @Override
    public void saveBatch(List<DTO> list) {
        if (!CollectionUtils.isEmpty(list)) {
            setId(list);
            int length = list.size();
            int page = length / 200;
            int mod = length % 200;
            int i;
            for(i = 0; i < page; ++i) {
                this.baseDao.saveAll(baseParser.parseListDto2Po(list.subList(i * 200, (i + 1) * 200)));
                this.baseDao.flush();
            }
            if (mod > 0) {
                this.baseDao.saveAll(baseParser.parseListDto2Po(list.subList(i * 200, length)));
            }
            this.baseDao.flush();
        }
    }

    @Override
    public void insertBatch(EntityManagerFactory factory, List<DTO> list) {
        List<PO> pos = baseParser.parseListDto2Po(list);
        setId(pos);
        int count = 0;
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        for (PO t : pos) {
            ++count;
            entityManager.persist(t);
            if (count % 2000 == 0) {
                this.flush(entityManager, transaction);
            }
        }

        entityManager.flush();
        entityManager.clear();
        transaction.commit();
        entityManager.close();
    }

    @Override
    public void delete(ID id) {
        this.baseDao.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteByIds(List<ID> ids) {
        List<DTO> list = baseParser.parseListPo2Dto(this.baseDao.findAllById(ids));
        this.deleteBatch(list);
    }

    @Override
    public void deleteBatch(List<DTO> list) {
        this.baseDao.deleteInBatch(baseParser.parseListDto2Po(list));
    }

    @Override
    public void deleteEntity(DTO t) {
        this.baseDao.delete(baseParser.parseDto2Po(t));
    }

    @Override
    public void deleteByProperty(String propertyName, Object propertyValue) {
        List<DTO> listByProperty = this.findListByProperty(propertyName, propertyValue);
        for (DTO entity : listByProperty) {
            this.deleteEntity(entity);
        }
    }

    @Override
    public DTO updateNotNull(DTO t, ID id) {
        DTO tDb = this.findOne(id);
        Assert.notNull(tDb, "查询不到该数据！[" + id + "]");
        this.baseDao.save(baseParser.parseDto2Po(tDb));
        return tDb;
    }

    @Override
    public void updateBatch(EntityManagerFactory factory, List<DTO> list) {
        List<PO> pos = baseParser.parseListDto2Po(list);
        int count = 0;
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        for (PO t : pos) {
            ++count;
            entityManager.merge(t);
            if (count % 2000 == 0) {
                this.flush(entityManager, transaction);
            }
        }
        entityManager.flush();
        entityManager.clear();
        transaction.commit();
        entityManager.close();
    }

    @Override
    public PageResponse<DTO> customSearch(Map<String, Object> searchMap) {
        QueryLocal.searchParamater.set(JSONObject.parseObject(JSONObject.toJSONString(searchMap)));
        Set<String> keys = searchMap.keySet();
        Table<String, Operator, Object> searchTable = HashBasedTable.create();

        /*
            查询参数结构： operator_fieldName:value
            例如： ==> EQ_userName:'测试' 或者 EQ_user.userName:'测试'
         */
        keys.stream().filter((key) -> key.contains("_")).forEachOrdered((key) -> {
            Operator operator = Operator.valueOf(key.split("_")[0]);
            String name = key.split("_")[1];
            searchTable.put(name, operator, searchMap.get(key));
        });

        if (searchMap.get("page") == null) {
            // order 排序集合
            List<Sort.Order> orders = this.buildOrders(searchMap.get("order"));
            List result;
            if (orders.size() == 0) {
                if (searchTable.size() == 0) {
                    result = this.baseDao.findAll();
                } else {
                    result = this.baseDao.findAll(QueryUtils.buildCondition(searchTable));
                }
            } else if (searchTable.size() == 0) {
                result = this.baseDao.findAll(Sort.by(orders));
            } else {
                result = this.baseDao.findAll(QueryUtils.buildCondition(searchTable), Sort.by(orders));
            }

            return PageResponse.builder().total((long)result.size()).data(result).build();
        } else {
            int pageIndex = Integer.valueOf(searchMap.get("page").toString());
            int rows = searchMap.get("rows") == null ? 10 : Integer.valueOf(searchMap.get("rows").toString());
            Pageable page = this.getPage(pageIndex, rows, this.buildOrders(searchMap.get("order")));
            Page<PO> pageResult = this.findPage((Table)searchTable, page);
            List<DTO> content = baseParser.parseListPo2Dto(pageResult.getContent());
            return PageResponse.builder().total(pageResult.getTotalElements()).data(content).build();
        }
    }

    @Override
    public long customSearchCount(HashMap<String, Object> searchMap) {
        Set<String> keys = searchMap.keySet();
        Table<String, Operator, Object> searchTable = HashBasedTable.create();
        keys.stream().filter((key) -> {
            return key.contains("_");
        }).forEachOrdered((key) -> {
            Operator operator = Operator.valueOf(key.split("_")[0]);
            String name = key.split("_")[1];
            searchTable.put(name, operator, searchMap.get(key));
        });
        return this.baseDao.count(QueryUtils.buildCondition(searchTable));
    }

    @Override
    public DTO findOne(ID id) {
        Optional<PO> t = this.baseDao.findById(id);
        return baseParser.parsePo2Dto(t.orElse(null));
    }

    @Override
    public DTO findOneByProp(String key, String value) {
        Specification specification = propertySpec(key, value);
        Optional<PO> po = baseDao.findOne(specification);
        return po.isPresent() ? baseParser.parsePo2Dto(po.get()) : null;
    }

    @Override
    public DTO findOneByProps(Map<String, String> params) {
        Specification specification = propertySpec(params);
        Optional<PO> po = baseDao.findOne(specification);
        return po.isPresent() ? baseParser.parsePo2Dto(po.get()) : null;
    }

    @Override
    public List<DTO> findByIds(List<ID> ids) {
        return baseParser.parseListPo2Dto(this.baseDao.findAllById(ids));
    }

    @Override
    public List<DTO> findAll() {
        return baseParser.parseListPo2Dto(this.baseDao.findAll());
    }

    @Override
    public List<DTO> findAll(Table<String, Operator, Object> table) {
        return table != null && !table.isEmpty() ? baseParser.parseListPo2Dto(this.baseDao.findAll(QueryUtils.buildCondition(table))) : baseParser.parseListPo2Dto(this.baseDao.findAll());
    }

    @Override
    public ID getId() {
        return (ID) UUID.randomUUID().toString();
    }

    public List<DTO> findListByProperty(String propertyName, Object propertyValue) {
        return baseParser.parseListPo2Dto(this.baseDao.findAll(this.propertySpec(propertyName, propertyValue)));
    }

    public Page<PO> findPage(Table<String, Operator, Object> conditionTable, Pageable pageable) {
        return conditionTable != null && !conditionTable.isEmpty() ? this.baseDao.findAll(QueryUtils.buildCondition(conditionTable), pageable) : this.baseDao.findAll(pageable);
    }

    protected Pageable getPage(Integer page, Integer rows, List<Sort.Order> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return this.getNativeSqlPage(page, rows);
        } else {
            page = page == null ? 0 : page - 1;
            rows = rows == null ? 10 : rows;
            Sort sort = Sort.by(orders);
            return PageRequest.of(page, rows, sort);
        }
    }

    protected Pageable getNativeSqlPage(Integer page, Integer rows) {
        page = page == null ? 0 : page - 1;
        rows = rows == null ? 10 : rows;
        return PageRequest.of(page, rows);
    }

    private Specification<PO> propertySpec(Map<String, String> params) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList();
            params.forEach((key, value) -> {
                Path path = root;

                String[] keys = StringUtils.split(key, ".");
                String[] keyArr = keys;
                int keyLength = keys.length;

                for(int i = 0; i < keyLength; ++i) {
                    String k = keyArr[i];
                    path = path.get(k);
                }

                if (null != value) {
                    predicates.add(criteriaBuilder.equal(path, value));
                } else {
                    predicates.add(path.isNull());
                }
            });
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<PO> propertySpec(String propertyName, Object propertyValue) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Path path = root;
            String[] keys = StringUtils.split(propertyName, ".");
            String[] keyArr = keys;
            int keyLength = keys.length;

            for(int i = 0; i < keyLength; ++i) {
                String k = keyArr[i];
                path = path.get(k);
            }

            List<Predicate> list = new ArrayList();
            if (null != propertyValue) {
                list.add(criteriaBuilder.equal(path, propertyValue));
            } else {
                list.add(path.isNull());
            }

            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
    }

    private List<Sort.Order> buildOrders(Object orderParam) {
        List<Sort.Order> orders = new ArrayList();
        if (orderParam != null) {
            LinkedHashMap<String, String> orderObj = (LinkedHashMap) JSONObject.parseObject(JSONObject.toJSONString(orderParam), LinkedHashMap.class);
            Set<String> orderSet = orderObj.keySet();
            for (String order : orderSet) {
                orders.add(new Sort.Order(Sort.Direction.valueOf(orderObj.get(order)), order));
            }
        }
        return orders;
    }

    private void flush(EntityManager entityManager, EntityTransaction transaction) {
        entityManager.flush();
        entityManager.clear();
        transaction.commit();
        transaction.begin();
    }

    private void setId(Object o) {
        try {
            Field field = o.getClass().getDeclaredField("id");
            field.setAccessible(true);
            if (null == field.get(o) && "java.lang.String".equals(field.getType().getName())) {
                field.set(o, getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setId(List objs) {
        objs.forEach(obj -> setId(obj));
    }
}
