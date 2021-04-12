package com.flance.jdbc.jpa.simple.common.jdbc.ext;

import com.flance.jdbc.jpa.simple.dao.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

/**
 * 扩展 repository
 * @author jhf
 * @param <T>
 * @param <ID>
 */
public class ExtJpaRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, Serializable> implements BaseRepository<T, Serializable> {

    private final EntityManager entityManager;
    private final JpaEntityInformation<T,?> entityInformation;

    public ExtJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
        super(entityInformation, em);
        this.entityInformation = entityInformation;
        this.entityManager = em;
    }

    @Override
    protected <S extends T> Page<S> readPage(TypedQuery<S> query, Class<S> domainClass, Pageable pageable, Specification<S> spec) {
        if (pageable.isPaged()) {
            query.setFirstResult((int)pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        return PageableExecutionUtils.getPage(query.getResultList(), pageable, () -> executeCountQuery(this.getCountQuery(spec, domainClass)));
    }

    private static long executeCountQuery(TypedQuery<Long> query) {
        Assert.notNull(query, "TypedQuery must not be null!");
        List<Long> totals = query.getResultList();
        long total = 0L;
        if (totals != null && totals.size() > 1) {
            total = totals.size();
        }
        if (totals != null && totals.size() == 1) {
            total = totals.get(0);
        }
        return total;
    }
}
