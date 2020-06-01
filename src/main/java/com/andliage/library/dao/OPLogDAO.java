/*
 * Author: Andliage Pox
 * Date: 2020-06-01
 */

package com.andliage.library.dao;

import com.andliage.library.entity.OPLog;
import com.andliage.library.service.OPLogService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class OPLogDAO extends BaseDAO {
    public List<OPLog> findOPLogs(int page) {
        Session session = template.getSessionFactory().openSession();
        String hql = "from OPLog order by id desc";
        Query query = session.createQuery(hql);
        query.setFirstResult((page - 1) * OPLogService.COUNT_PER_PAGE);
        query.setMaxResults(OPLogService.COUNT_PER_PAGE);
        List<OPLog> list = query.list();
        session.close();
        return list;
    }

    public long getOPLogCount() {
        Session session = template.getSessionFactory().openSession();
        String hql = "select count(*) from OPLog";
        Query query = session.createQuery(hql);
        long count = (long) query.uniqueResult();
        session.close();
        return count;
    }

    public void saveOPLog(OPLog log) {
        template.save(log);
        template.update(log.getAdmin());
        template.flush();
    }
}
