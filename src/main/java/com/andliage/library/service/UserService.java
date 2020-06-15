/*
 * Author: Andliage Pox
 * Date: 2020-05-31
 */

package com.andliage.library.service;

import com.andliage.library.dao.AccountDAO;
import com.andliage.library.dao.OPLogDAO;
import com.andliage.library.entity.Admin;
import com.andliage.library.entity.Book;
import com.andliage.library.entity.OPLog;
import com.andliage.library.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    /**
     * 管理员每页用户容量
     */
    public static final int COUNT_PER_PAGE_ADMIN = 10;

    private OPLogDAO opLogDAO;
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

    public Map<String, Object> detail(int userId) {
        User user = accountDAO.findUserById(userId);
        Map<String, Object> res = new HashMap<>();
        res.put("name", user.getUsername());
        res.put("hold", user.getMaxHold());
        res.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(user.getCreateTime()));
        res.put("books", user.getHoldBooks().stream().map((Book book) ->
            "《" + book.getName() + "》 [" + book.getId() + "]"
        ).collect(Collectors.toList()));
        return res;
    }

    public Map<String, Object> update(
            int userId, int hold,
            String pwd, String adminName
    ) {
        Map<String, Object> res = new HashMap<>();
        User user = accountDAO.findUserById(userId);
        Admin admin = accountDAO.findAdminByName(adminName);

        StringBuilder opLogContent = new StringBuilder("进行了 <b>更新</b> 用户 <b>" +
                user.getUsername() + "</b> [" + userId + "] 信息：");

        if (pwd != null && !pwd.equals("")) {
            opLogContent.append("<br><b>修改了用户密码</b>");
            user.setPassword(DigestUtils.md5DigestAsHex(pwd.getBytes()));
        }

        if (user.getMaxHold() != hold) {
            if (hold < user.getHoldBooks().size()) {
                res.put("code", 0);
                res.put("msg", "最大借阅数不得小于已借阅数");
                return res;
            }
            opLogContent.append("<br>将 <b>最大借阅数</b> 由 <b>").append(user.getMaxHold())
                    .append("</b> 修改为 <b>").append(hold).append("</b>");
            user.setMaxHold(hold);
        }

        saveNewOPLog(admin, opLogContent.toString());
        accountDAO.updateUser(user);

        res.put("code", 1);
        return res;
    }

    public Map<String, Object> delete(int userId, String adminName) {
        Map<String, Object> res = new HashMap<>();

        User user = accountDAO.findUserById(userId);
        Admin admin = accountDAO.findAdminByName(adminName);

        try {
            accountDAO.deleteUser(user);
        } catch (Exception e) {
            res.put("code", 0);
            res.put("msg", "用户已进行过借还操作，不可删除");
            return res;
        }

        String content = "进行了 <b>删除</b> 用户 <b>" + user.getUsername() + "</b> [" + userId + "] :";
        saveNewOPLog(admin, content);

        res.put("code", 1);
        return res;
    }

    private void saveNewOPLog(Admin admin, String content) {
        OPLog log = new OPLog();
        log.setAdmin(admin);
        log.setContent(content);
        log.setTime(new Timestamp(System.currentTimeMillis()));
        opLogDAO.saveOPLog(log);
    }

    @Autowired
    public void setOpLogDAO(OPLogDAO opLogDAO) {
        this.opLogDAO = opLogDAO;
    }

    @Autowired
    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }
}
