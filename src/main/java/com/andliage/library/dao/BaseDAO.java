/*
 * Author: Andliage Pox
 * Date: 2020-05-12
 */

package com.andliage.library.dao;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class BaseDAO extends HibernateDaoSupport {
    protected HibernateTemplate template;

    @Resource(name="sessionFactory")
    @SuppressWarnings("unused")
    public void init(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
        template = getHibernateTemplate();
    }
}
