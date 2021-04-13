package com.flance.jdbc.jpa.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * sql拼接
 * @author jhf
 */
public class QueryUtils {


    public static<T> Specification<T> buildCondition(Table<String, Operator, Object> conditionTable) {
        return (root, query, builder) -> {
            JSONArray andArray = null;
            JSONObject searchParamater = QueryLocal.searchParamater.get();
            // 用于条件分组把数组中的字段用and分组拼接，"AND":[["enterprise.enterpriseName"],["name", "jszhm"],["safeEducation.id"]]
            if (searchParamater != null) {
                andArray = searchParamater.getJSONArray("AND");
            }

            if (andArray == null) {
                List<Predicate> where = getWhere(root, query, builder, (Set)null, conditionTable);
                // 当where条件为空时(可能为错误条件被过滤)，builder.or 会添加 0 = 1 导致查询不出数据; builder.and 会添加 1 = 1
                if (where.size() == 0) {
                    return builder.and(where.toArray(new Predicate[0]));
                }
                return builder.or(where.toArray(new Predicate[0]));
            } else {
                List<Predicate> allList = Lists.newArrayList();
                ArrayList<Predicate> andList = getWhereList(andArray, root, query, builder, conditionTable);
                Predicate p = builder.and(andList.toArray(new Predicate[andList.size()]));
                allList.add(p);
                return builder.or(allList.toArray(new Predicate[allList.size()]));
            }
        };
    }

    private static ArrayList<Predicate> getWhereList(JSONArray andArray, Root root, CriteriaQuery<?> query, CriteriaBuilder builder, Table<String, Operator, Object> conditionTable) {
        ArrayList<Predicate> list = Lists.newArrayList();

        for(int i = 0; i < andArray.size(); ++i) {
            JSONArray array = andArray.getJSONArray(i);
            HashSet<String> set = Sets.newHashSet();

            for(int j = 0; j < array.size(); ++j) {
                set.add(array.getString(j));
            }

            List<Predicate> where = getWhere(root, query, builder, set, conditionTable);
            Predicate p = builder.or(where.toArray(new Predicate[where.size()]));
            list.add(p);
        }

        return list;
    }

    private static List<Predicate> getWhere(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder, Set<String> keyset, Table<String, Operator, Object> conditionTable) {
        List<Predicate> where = Lists.newArrayList();

        Multimap<Operator, Predicate> criteriacMap = null;

        try {
            criteriacMap = buildCriteria(root, query, builder, conditionTable, keyset);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        assert criteriacMap != null;

        Collection<Predicate> oc = criteriacMap.get(Operator.OR);
        if (oc != null && oc.size() > 0) {
            where.add(builder.or(oc.toArray(new Predicate[0])));
        }

        Collection<Predicate> ac = criteriacMap.get(Operator.AND);
        if (ac != null && ac.size() > 0) {
            where.add(builder.and(ac.toArray(new Predicate[0])));
        }

        return where;
    }

    private static Multimap<Operator, Predicate> buildCriteria(Root root, CriteriaQuery<?> query, CriteriaBuilder builder, Table<String, Operator, Object> conditionTable, Set<String> filters) throws ClassNotFoundException {
        Multimap<Operator, Predicate> criteriacMap = ArrayListMultimap.create();
        Iterator<Operator> iterator = conditionTable.columnKeySet().iterator();

        label:
        while(iterator.hasNext()) {
            Operator op = iterator.next();
            Map<String, Object> keyValue = conditionTable.column(op);
            Iterator<String> keyIterator = keyValue.keySet().iterator();
            label2:
            while(true) {
                while(true) {
                    String key;
                    do {
                        do {
                            if (!keyIterator.hasNext()) {
                                continue label;
                            }

                            key = keyIterator.next();
                        } while(!StringUtils.isNotEmpty(key));
                    } while(filters != null && !filters.contains(key));

                    String[] keys = StringUtils.split(key, ".");
                    Path path = root;
                    // 格式 例：EQ_selfCheckItems::checkStatus 表示 list属性中的值作为条件 left join
                    if (key.contains(":")) {
                        String listName = key.split(":")[0];
                        Join join = root.join(root.getModel().getList(listName, (Class)QueryLocal.classLocal.get()), JoinType.LEFT);
                        path = join.get(key.split(":")[2]);
                    } else {
                        String[] keyArr = keys;
                        int keyLength = keys.length;

                        for(int i = 0; i < keyLength; ++i) {
                            String k = keyArr[i];
                            try {
                                path = path.get(k);
                            } catch (IllegalArgumentException e) {
                                continue label2;
                            }
                        }
                    }

                    Object value = keyValue.get(key);
                    switch(op) {
                        case NE:
                            criteriacMap.put(Operator.AND, builder.notEqual(path, processTime(value)));
                            break;
                        case EQ:
                            criteriacMap.put(Operator.AND, builder.equal(path, processTime(value)));
                            break;
                        case LIKE:
                            criteriacMap.put(Operator.AND, builder.like(path.as(String.class), "%" + processSpecial(ObjectUtils.toString(value)) + "%", '/'));
                            break;
                        case LLIKE:
                            criteriacMap.put(Operator.AND, builder.like(path.as(String.class), "%" + processSpecial(ObjectUtils.toString(value)), '/'));
                            break;
                        case RLIKE:
                            criteriacMap.put(Operator.AND, builder.like(path.as(String.class), processSpecial(ObjectUtils.toString(value)) + "%", '/'));
                            break;
                        case GT:
                            criteriacMap.put(Operator.AND, builder.greaterThan(path, (Comparable)processTime(value)));
                            break;
                        case LT:
                            criteriacMap.put(Operator.AND, builder.lessThan(path, (Comparable)processTime(value)));
                            break;
                        case GTE:
                            criteriacMap.put(Operator.AND, builder.greaterThanOrEqualTo(path, (Comparable)processTime(value)));
                            break;
                        case LTE:
                            criteriacMap.put(Operator.AND, builder.lessThanOrEqualTo(path, (Comparable)processTime(value)));
                            break;
                        case IN:
                            CriteriaBuilder.In in = builder.in(path);
                            if (!(value instanceof Collection)) {
                                throw new RuntimeException("值必须是集合类型");
                            }

                            Collection values = (Collection)value;
                            Iterator vi = values.iterator();

                            while(vi.hasNext()) {
                                in.value(vi.next());
                            }

                            criteriacMap.put(Operator.AND, in);
                            break;
                        case BETWEEN:
                            if (!(value instanceof Comparable[])) {
                                throw new RuntimeException("值必须是数组类型");
                            }

                            Comparable[] valueArray = (Comparable[])((Comparable[])value);
                            criteriacMap.put(Operator.AND, builder.between(path, valueArray[0], valueArray[1]));
                            break;
                        case NULL:
                            criteriacMap.put(Operator.AND, builder.isNull(path));
                            break;
                        case NOTNULL:
                            criteriacMap.put(Operator.AND, builder.isNotNull(path));
                            break;
                        case ORNE:
                            criteriacMap.put(Operator.OR, builder.notEqual(path, processTime(value)));
                            break;
                        case OREQ:
                            criteriacMap.put(Operator.OR, builder.equal(path, processTime(value)));
                            break;
                        case ORLIKE:
                            criteriacMap.put(Operator.OR, builder.like(path.as(String.class), "%" + processSpecial(ObjectUtils.toString(value)) + "%", '/'));
                            break;
                        case ORGT:
                            criteriacMap.put(Operator.OR, builder.greaterThan(path, (Comparable)processTime(value)));
                            break;
                        case ORLT:
                            criteriacMap.put(Operator.OR, builder.lessThan(path, (Comparable)processTime(value)));
                            break;
                        case ORGTE:
                            criteriacMap.put(Operator.OR, builder.greaterThanOrEqualTo(path, (Comparable)processTime(value)));
                            break;
                        case ORLTE:
                            criteriacMap.put(Operator.OR, builder.lessThanOrEqualTo(path, (Comparable)processTime(value)));
                            break;
                        case ORIN:
                            CriteriaBuilder.In oin = builder.in(path);
                            if (!(value instanceof Collection)) {
                                throw new RuntimeException("值必须是集合类型");
                            }

                            Collection ovalues = (Collection)value;
                            Iterator ovi = ovalues.iterator();

                            while(ovi.hasNext()) {
                                oin.value(ovi.next());
                            }

                            criteriacMap.put(Operator.OR, oin);
                            break;
                        case ORBETWEEN:
                            if (!(value instanceof Comparable[])) {
                                throw new RuntimeException("值必须是数组类型");
                            }

                            Comparable[] ovalueArray = (Comparable[]) value;
                            criteriacMap.put(Operator.OR, builder.between(path, ovalueArray[0], ovalueArray[1]));
                            break;
                        case ORNULL:
                            criteriacMap.put(Operator.OR, builder.isEmpty(path));
                            break;
                        case ORNOTNULL:
                            criteriacMap.put(Operator.OR, builder.isNotEmpty(path));
                        case GROUPBY:
                            query.groupBy(path);
                        default:
                            break;
                    }
                }
            }
        }

        QueryLocal.classLocal.remove();
        return criteriacMap;
    }

    private static Object processSpecial(Object value) {
        if (!(value instanceof String)) {
            return value;
        } else {
            String str = (String)value;
            return str.replaceAll("%", "/%").replaceAll("_", "/_");
        }
    }

    private static Object processTime(Object value) {
        if (value instanceof Date) {
            return value;
        } else if (value.toString().contains("T")) {
            try {
                return DateUtil.parse(value.toString(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            } catch (Exception e) {
                return value;
            }
        } else if (value.toString().contains(":") && value.toString().contains("-")) {
            try {
                return DateUtil.parse(value.toString(), "yyyy-MM-dd HH:mm:ss");
            } catch (Exception e) {
                return value;
            }
        } else {
            return value;
        }
    }

}
