package com.toptal.expensetracker.service;

import com.toptal.expensetracker.model.User;
import com.toptal.expensetracker.model.User_;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * @author: Sergey Royz
 * Date: 27.12.2014
 */
@Slf4j
@Service
public class UserService {

    @Inject
    private EntityManager em;

    @Transactional
    public User create(String email, String password) {
        //TODO: hash & salt
        User user = new User(email, password, password, new Date());
        em.persist(user);
        return user;
    }

    public User findByEmail(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(cb.equal(root.get(User_.email), email));
        List<User> result = em.createQuery(query).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    @Transactional
    public void remove(User user) {
        em.remove(em.contains(user) ? user : em.merge(user));
    }


}
