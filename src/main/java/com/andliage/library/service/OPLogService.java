/*
 * Author: Andliage Pox
 * Date: 2020-06-01
 */

package com.andliage.library.service;

import com.andliage.library.dao.OPLogDAO;
import com.andliage.library.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OPLogService {
    /**
     * 管理员每页操作日志容量
     */
    public static final int COUNT_PER_PAGE = 10;

    private OPLogDAO opLogDAO;

    public Map<String, Object> list(int page) {
        Map<String, Object> res = new HashMap<>();
        long count = opLogDAO.getOPLogCount();
        List<OPLog> list = opLogDAO.findOPLogs(page);
        List<Map<String, Object>> logMaps = new ArrayList<>(list.size());

        for (OPLog log: list) {
            Map<String, Object> map = new HashMap<>();
            Admin admin = log.getAdmin();
            String content = "管理员 <b>" + admin.getUsername() + "</b> " + log.getContent();
            map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(log.getTime()));
            map.put("content", content);
            logMaps.add(map);
        }

        res.put("pages", (int) Math.ceil((double) count / COUNT_PER_PAGE));
        res.put("logs", logMaps);
        return res;
    }

    @Autowired
    public void setOpLogDAO(OPLogDAO opLogDAO) {
        this.opLogDAO = opLogDAO;
    }
}
