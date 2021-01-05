package com.flance.jdbc.jpa.simple.service;

import com.alibaba.fastjson.JSONObject;
import com.flance.jdbc.jpa.simple.common.jdbc.Operator;
import com.flance.jdbc.jpa.simple.common.jdbc.QueryLocal;
import com.flance.jdbc.jpa.simple.common.jdbc.QueryUtils;
import com.flance.jdbc.jpa.simple.common.jdbc.UpdateUtils;
import com.flance.jdbc.jpa.simple.common.request.PageResponse;
import com.flance.jdbc.jpa.simple.dao.BaseRepository;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
 * service基类
 * @author jhf
 * @param <T>   实体类型
 * @param <ID>  主键
 */
public  abstract class BaseService<T, ID extends Serializable> implements IService<T, ID, PageResponse<T>> {
    private BaseRepository<T, ID> baseDao;

    protected void setBaseDao(BaseRepository<T, ID> baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public T saveOne(T t) {
        return this.baseDao.save(t);
    }

    @Override
    public void saveBatch(List<T> list) {
        if (!CollectionUtils.isEmpty(list)) {
            setId(list);
            int length = list.size();
            int page = length / 200;
            int mod = length % 200;
            int i;
            for(i = 0; i < page; ++i) {
                this.baseDao.saveAll(list.subList(i * 200, (i + 1) * 200));
                this.baseDao.flush();
            }
            if (mod > 0) {
                this.baseDao.saveAll(list.subList(i * 200, length));
            }
            this.baseDao.flush();
        }
    }

    protected void insertBatch(EntityManagerFactory factory, List<T> list) {
        setId(list);
        int count = 0;
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        for (T t : list) {
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
        List<T> list = this.baseDao.findAllById(ids);
        this.deleteBatch(list);
    }

    @Override
    public void deleteEntity(T t) {
        this.baseDao.delete(t);
    }

    @Override
    public void deleteByProperty(String propertyName, Object propertyValue) {
        List<T> listByProperty = this.findListByProperty(propertyName, propertyValue);
        for (T entity : listByProperty) {
            this.deleteEntity(entity);
        }
    }

    @Override
    public T updateNotNull(T t, ID id) {
        T ft = this.findOne(id);
        Assert.notNull(ft, "查询不到该数据！[" + id + "]");
        UpdateUtils.copyNullProperties(ft, t);
        this.baseDao.save(t);
        return ft;
    }

    @Override
    public void updateBatch(EntityManagerFactory factory, List<T> list) {
        int count = 0;
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        for (T t : list) {
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
    public PageResponse<T> findPage(Map<String, Object> searchMap) {
        if (null == searchMap) {
            searchMap = Maps.newHashMap();
        }
        final Map<String, Object> finalMap = searchMap;
        QueryLocal.searchParamater.set(JSONObject.parseObject(JSONObject.toJSONString(finalMap)));
        Set<String> keys = finalMap.keySet();
        Table<String, Operator, Object> searchTable = HashBasedTable.create();

        /*
            查询参数结构： operator_fieldName:value
            例如： ==> EQ_userName:'测试' 或者 EQ_user.userName:'测试'
         */
        keys.stream().filter((key) -> key.contains("_")).forEachOrdered((key) -> {
            Operator operator = Operator.valueOf(key.split("_")[0]);
            String name = key.split("_")[1];
            searchTable.put(name, operator, finalMap.get(key));
        });

        if (finalMap.get("page") == null) {
            // order 排序集合
            List<Sort.Order> orders = this.buildOrders(finalMap.get("order"));
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
            int pageIndex = Integer.valueOf(finalMap.get("page").toString());
            int rows = finalMap.get("rows") == null ? 10 : Integer.valueOf(finalMap.get("rows").toString());
            Pageable page = this.getPage(pageIndex, rows, this.buildOrders(finalMap.get("order")));
            Page<T> pageResult = this.findPage((Table)searchTable, page);
            List<T> content = pageResult.getContent();
            return PageResponse.builder().total(pageResult.getTotalElements()).data(content).build();
        }
    }

    @Override
    public T findOne(ID id) {
        Optional<T> t = this.baseDao.findById(id);
        return t.orElse(null);
    }

    @Override
    public T findOneByProp(String key, Object value) {
        Specification specification = propertySpec(key, value);
        Optional<T> po = baseDao.findOne(specification);
        return po.isPresent() ? po.get() : null;
    }

    @Override
    public T findOneByProps(Map<String, Object> params) {
        Specification specification = propertySpec(params);
        Optional<T> po = baseDao.findOne(specification);
        return po.isPresent() ? po.get() : null;
    }

    @Override
    public List<T> findByIds(List<ID> ids) {
        return this.baseDao.findAllById(ids);
    }

    @Override
    public List<T> findAll() {
        return this.baseDao.findAll();
    }

    @Override
    public List<T> findAll(Map<String, Object> searchMap) {
        if (null == searchMap) {
            searchMap = Maps.newHashMap();
        }
        final Map<String, Object> finalMap = searchMap;
        Table<String, Operator, Object> table = HashBasedTable.create();
        finalMap.entrySet().stream().forEach(entry -> table.put(entry.getKey(), Operator.EQ, entry.getValue()));
        return findAll(table);
    }

    protected long customSearchCount(HashMap<String, Object> searchMap) {
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

    protected List<T> findListByProperty(String propertyName, Object propertyValue) {
        return this.baseDao.findAll(this.propertySpec(propertyName, propertyValue));
    }

    protected Page<T> findPage(Table<String, Operator, Object> conditionTable, Pageable pageable) {
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

    protected void deleteBatch(List<T> list) {
        this.baseDao.deleteInBatch(list);
    }

    protected Pageable getNativeSqlPage(Integer page, Integer rows) {
        page = page == null ? 0 : page - 1;
        rows = rows == null ? 10 : rows;
        return PageRequest.of(page, rows);
    }

    protected List<T> findAll(Table<String, Operator, Object> table) {
        return table != null && !table.isEmpty() ? this.baseDao.findAll(QueryUtils.buildCondition(table)) : this.baseDao.findAll();
    }

    protected ID getId() {
        return (ID) UUID.randomUUID().toString();
    }

    private Specification<T> propertySpec(Map<String, Object> params) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = Lists.newArrayList();
            params.forEach((key, value) -> {
                Path path = root;
                if (key.contains("_") && key.split("_").length > 0) {
                    key = key.split("_")[1];
                }
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

    private Specification<T> propertySpec(String propertyName, Object propertyValue) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Path path = root;
            String[] keys = StringUtils.split(propertyName, ".");
            String[] keyArr = keys;
            int keyLength = keys.length;

            for(int i = 0; i < keyLength; ++i) {
                String k = keyArr[i];
                path = path.get(k);
            }

            List<Predicate> list = Lists.newArrayList();
            if (null != propertyValue) {
                list.add(criteriaBuilder.equal(path, propertyValue));
            } else {
                list.add(path.isNull());
            }

            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
    }

    private List<Sort.Order> buildOrders(Object orderParam) {
        List<Sort.Order> orders = Lists.newArrayList();
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
