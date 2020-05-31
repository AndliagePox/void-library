/*
 * Author: Andliage Pox
 * Date: 2020-05-31
 */

package com.andliage.library.service;

import com.andliage.library.dao.AccountDAO;
import com.andliage.library.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    /**
     * 管理员每页用户容量
     */
    public static final int COUNT_PER_PAGE_ADMIN = 10;

    private AccountDAO accountDAO;

    public Map<String, Object> list(int page, String searchText) {
        Map<String, Object> res = new HashMap<>();
        long count = accountDAO.getUserCount(searchText);
        List<User> list = accountDAO.findUsers(page, searchText);
        List<Map<String, Object>> userMaps = new ArrayList<>(list.size());

        for (User user: list) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("username", user.getUsername());
            map.put("hold", user.getHoldBooks().size());
            map.put("maxHold", user.getMaxHold());
            userMaps.add(map);
        }

        res.put("pages", (int) Math.ceil((double) count / COUNT_PER_PAGE_ADMIN));
        res.put("users", userMaps);
        return res;
    }

    @Autowired
    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }
}
