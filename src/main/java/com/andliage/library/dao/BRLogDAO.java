/*
 * Author: Andliage Pox
 * Date: 2020-05-30
 */

package com.andliage.library.dao;

import com.andliage.library.entity.BRLog;
import com.andliage.library.entity.User;
import com.andliage.library.service.BRLogService;
import com.andliage.library.service.UserService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class BRLogDAO extends BaseDAO {
    public List<BRLog> findBRLogs(int page) {
        Session session = template.getSessionFactory().openSession();
        String hql = "from BRLog order by id desc";
        Query query = session.createQuery(hql);
        query.setFirstResult((page - 1) * BRLogService.COUNT_PER_PAGE);
        query.setMaxResults(BRLogService.COUNT_PER_PAGE);
        List<BRLog> list = query.list();
        session.close();
        return list;
    }

    public long getBRLogCount() {
        Session session = template.getSessionFactory().openSession();
        String hql = "select count(*) from BRLog";
        Query query = session.createQuery(hql);
        long count = (long) query.uniqueResult();
        session.close();
        return count;
    }

    public void saveBRLog(BRLog log) {
        template.save(log);
        template.flush();
    }
}
