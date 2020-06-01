/*
 * Author: Andliage Pox
 * Date: 2020-05-21
 */

package com.andliage.library.dao;

import com.andliage.library.entity.Admin;
import com.andliage.library.entity.Hold;
import com.andliage.library.entity.User;
import com.andliage.library.service.UserService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class AccountDAO extends BaseDAO {
    public List<User> findUsers(int page, String searchText) {
        if (searchText == null) searchText = "";
        Session session = template.getSessionFactory().openSession();
        String hql = "from User u where u.username like :search";
        Query query = session.createQuery(hql);
        query.setString("search", "%" + searchText + "%");
        query.setFirstResult((page - 1) * UserService.COUNT_PER_PAGE_ADMIN);
        query.setMaxResults(UserService.COUNT_PER_PAGE_ADMIN);
        List<User> list = query.list();
        session.close();
        return list;
    }

    public long getUserCount(String searchText) {
        if (searchText == null) searchText = "";
        Session session = template.getSessionFactory().openSession();
        String hql = "select count(*) from User u where u.username like :search";
        Query query = session.createQuery(hql);
        query.setString("search", "%" + searchText + "%");
        long count = (long) query.uniqueResult();
        session.close();
        return count;
    }

    public User findUserById(int id) {
        String hql = "from User u where u.id= ?";
        List<User> list = (List<User>) template.find(hql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public User findUserByName(String username) {
        String hql = "from User u where u.username = ?";
        List<User> list = (List<User>) template.find(hql, username);
        return list.isEmpty() ? null : list.get(0);
    }

    public Admin findAdminByName(String username) {
        String hql = "from Admin a where a.username = ?";
        List<Admin> list = (List<Admin>) template.find(hql, username);
        return list.isEmpty() ? null : list.get(0);
    }

    public void saveUser(User user) {
        template.save(user);
        template.flush();
    }

    public void updateUser(User user) {
        template.update(user);
        template.flush();
    }

    public void deleteUser(User user) {
        String hql = "from Hold h where h.user = ?";
        List<Hold> holds = (List<Hold>) template.find(hql, user);
        for (Hold hold: holds) {
            template.delete(hold);
        }
        template.delete(user);
        template.flush();
    }
}
