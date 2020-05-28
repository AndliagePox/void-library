/*
 * Author: Andliage Pox
 * Date: 2020-05-21
 */

package com.andliage.library.dao;

import com.andliage.library.entity.Admin;
import com.andliage.library.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class AccountDAO extends BaseDAO {
    public User findUserByName(String username) {
        String hql = "from User u where u.username = ?";
        List<User> list = template.find(hql, username);
        return list.isEmpty() ? null : list.get(0);
    }

    public Admin findAdminByName(String username) {
        String hql = "from Admin a where a.username = ?";
        List<Admin> list = template.find(hql, username);
        return list.isEmpty() ? null : list.get(0);
    }

    public void saveNewUser(User user) {
        template.save(user);
        template.flush();
    }
}
