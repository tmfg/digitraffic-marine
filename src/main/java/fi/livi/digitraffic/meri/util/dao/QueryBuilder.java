package fi.livi.digitraffic.meri.util.dao;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * T - what is returned
 * K - what is queried
 */
public class QueryBuilder<T, K> {
    private final EntityManager entityManager;
    private final CriteriaBuilder cb;
    private final CriteriaQuery<T> query;
    private final Root<K> root;
    private final List<Predicate> predicateList = new ArrayList();

    public QueryBuilder(final EntityManager entityManager, final Class<T> tclazz, final Class<K> kclazz) {
        this.entityManager = entityManager;
        this.cb = entityManager.getCriteriaBuilder();
        this.query = cb.createQuery(tclazz);
        root = query.from(kclazz);
    }

    public void equals(final String attribute, final Object o) {
        equals(root.get(attribute), o);
    }

    public void equals(final Expression<?> e, final Object o) {
        predicateList.add(equalPredicate(e, o));
    }

    public Predicate equalPredicate(final Expression e, final Object o) {
        return cb.equal(e, o);
    }

    public <E extends Comparable<? super E>> void gte(final Expression<? extends E> e, final E value) {
        predicateList.add(cb.greaterThanOrEqualTo(e, value));
    }

    public <E extends Comparable<? super E>> void lte(final Expression<? extends E> e, final E value) {
        predicateList.add(cb.lessThanOrEqualTo(e, value));
    }

    public <E extends Comparable<? super E>> void lt(final Expression<? extends E> e, final E value) {
        predicateList.add(cb.lessThan(e, value));
    }

    public <Z> void in(final String attribute, final List<Z> inList) {
        in(root.get(attribute), inList);
    }

    public <Z> void in(final Path<Object> path, final List<Z> inList) {
        predicateList.add(inPredicate(path, inList));
    }

    public <S> void in(final String attribute, final Subquery<S> subquery) {
        predicateList.add(cb.in(root.get(attribute)).value(subquery));
    }

    public <Z> Predicate inPredicate(final Path<Object> path, final List<Z> inList) {
        return cb.in(path).value(inList);
    }

    public <Z> void notIn(final String attribute, final List<Z> inList) {
        notIn(root.get(attribute), inList);
    }

    public <Z> void notIn(final Path<Object> path, final List<Z> inList) {
        predicateList.add(inPredicate(path, inList).not());
    }

    public void like(final String attribute, final String pattern) {
        predicateList.add(cb.like(root.get(attribute), pattern));
    }

    public <E> Expression<E> function(final String functionName, final Class<E> eClazz, final Expression<?>... expressions) {
        return cb.function(functionName, eClazz, expressions);
    }

    public <E> Path<E> get(final String attribute) {
        return root.get(attribute);
    }

    public <E> Expression<E> literal(final E value) {
        return cb.literal(value);
    }

    public Expression<String> lower(final String attribute) {
        return cb.lower(root.get(attribute));
    }

    public List<T> getResults(final String attribute) {
        query.select(root.get(attribute))
             .where(predicateList.toArray(new Predicate[] {}));

        return entityManager.createQuery(query).getResultList();
    }

    public List<T> getResults(final Selection path) {
        query.select(path)
            .where(predicateList.toArray(new Predicate[] {}));

        return entityManager.createQuery(query).getResultList();
    }

    public List<T> getResults() {
        return getResults(root);
    }

    public <S> Subquery<S> subquery(final Class<S> sClass) {
        return query.subquery(sClass);
    }

    public <J> Join<K, J> join(final String attribute) {
        return root.join(attribute);
    }
}
