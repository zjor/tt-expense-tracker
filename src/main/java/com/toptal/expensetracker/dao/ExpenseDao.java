package com.toptal.expensetracker.dao;

import com.toptal.expensetracker.model.Expense;
import com.toptal.expensetracker.model.Expense_;
import com.toptal.expensetracker.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Sergey Royz
 * Date: 31.12.2014
 */
@Repository
public class ExpenseDao {

    @Inject
    private EntityManager em;

    @Transactional
    public Expense create(User user, String description, String comment, BigDecimal amount, Date timestamp) {
        Expense e = new Expense(user, description, comment, amount, timestamp);
        em.persist(e);
        return e;
    }

    @Transactional
    public void update(Expense expense) {
        if (get(expense.getId()) == null) {
            throw new NoResultException("Entity (ID: " + expense.getId() + ") was not found");
        }
        em.merge(expense);
    }

    @Transactional
    public void delete(String id) {
        Expense expense = get(id);
        if (expense == null) {
            throw new NoResultException("Entity (ID: " + expense.getId() + ") was not found");
        }
        em.remove(expense);
    }

    public Expense get(String id) {
        return em.find(Expense.class, id);
    }

    /**
     * Fetches expenses within (after, before] time range sorted be timestamp in descending order.
     * Filters expenses by string provided in filter parameter by description and comment fields.
     * If one of the time bounds is null then infinity is assumed
     * @param userId
     * @param after
     * @param before
     * @param limit
     * @param filter
     * @return
     */
    public List<Expense> fetch(String userId, Date after, Date before, Integer limit, String filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Expense> query = cb.createQuery(Expense.class);
        Root<Expense> root = query.from(Expense.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get(Expense_.userId), userId));

        if (after != null) {
            predicates.add(cb.lessThan(root.get(Expense_.timestamp), after));
        }

        if (before != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(Expense_.timestamp), before));
        }

        if (filter != null) {
            predicates.add(cb.or(
                    cb.like(root.get(Expense_.description), filter),
                    cb.like(root.get(Expense_.comment), filter)));
        }

        query.select(root)
                .where(predicates.toArray(new Predicate[]{}))
                .orderBy(cb.desc(root.get(Expense_.timestamp)));

        TypedQuery<Expense> typedQuery = em.createQuery(query);
        if (limit != null) {
            typedQuery.setMaxResults(limit);
        }
        return typedQuery.getResultList();
    }

}
