/*
 * Author: Andliage Pox
 * Date: 2020-05-21
 */

package com.andliage.library.service;

import com.andliage.library.dao.AccountDAO;
import com.andliage.library.entity.Admin;
import com.andliage.library.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
    public static final int NEW_USER_MAX_HOLD = 2;

    private AccountDAO dao;

    public Map<String, Object> login(String username, String password) {
        Map<String, Object> res = new HashMap<>();
        res.put("code", 0);
        res.put("msg", "用户名不存在");
        User user = dao.findUserByName(username);
        if (user != null) {
            if (user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
                res.put("code", 1);
                res.put("type", "user");
                res.put("msg", "ok");
            } else {
                res.put("msg", "密码错误");
            }
            return res;
        }
        Admin admin = dao.findAdminByName(username);
        if (admin != null) {
            if (admin.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
                res.put("code", 1);
                res.put("type", "admin");
                res.put("msg", "ok");
            } else {
                res.put("msg", "密码错误");
            }
            return res;
        }
        return res;
    }

    public Map<String, Object> reg(String username, String password) {
        Map<String, Object> res = new HashMap<>();
        res.put("code", 0);
        res.put("msg", "用户名已被占用");
        User user = dao.findUserByName(username);
        Admin admin = dao.findAdminByName(username);
        if (user == null && admin == null) {
            user = new User();
            user.setUsername(username);
            user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
            user.setMaxHold(NEW_USER_MAX_HOLD);
            user.setCreateTime(new Timestamp(System.currentTimeMillis()));
            dao.saveNewUser(user);
            res.put("code", 1);
            res.put("msg", "ok");
        }
        return res;
    }

    @Autowired
    public void setDao(AccountDAO dao) {
        this.dao = dao;
    }
}
