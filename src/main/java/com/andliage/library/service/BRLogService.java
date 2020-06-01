/*
 * Author: Andliage Pox
 * Date: 2020-05-31
 */

package com.andliage.library.service;

import com.andliage.library.dao.BRLogDAO;
import com.andliage.library.entity.BRLog;
import com.andliage.library.entity.Book;
import com.andliage.library.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BRLogService {
    /**
     * 管理员每页借还日志容量
     */
    public static final int COUNT_PER_PAGE = 10;

    private BRLogDAO brLogDAO;

    public Map<String, Object> list(int page) {
        Map<String, Object> res = new HashMap<>();
        long count = brLogDAO.getBRLogCount();
        List<BRLog> list = brLogDAO.findBRLogs(page);
        List<Map<String, Object>> logMaps = new ArrayList<>(list.size());

        for (BRLog log: list) {
            Map<String, Object> map = new HashMap<>();
            User user = log.getUser();
            Book book = log.getBook();
            String content = "用户 <b>" + user.getUsername() +
                    "</b> 进行了 <b>" + log.typeString() +
                    "</b> 书籍 <b>《" + book.getName() +
                    "》</b> [" + book.getId() + "]";
            map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(log.getTime()));
            map.put("content", content);
            logMaps.add(map);
        }

        res.put("pages", (int) Math.ceil((double) count / COUNT_PER_PAGE));
        res.put("logs", logMaps);
        return res;
    }

    @Autowired
    public void setBrLogDAO(BRLogDAO brLogDAO) {
        this.brLogDAO = brLogDAO;
    }
}
