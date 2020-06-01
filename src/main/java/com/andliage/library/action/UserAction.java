/*
 * Author: Andliage Pox
 * Date: 2020-05-21
 */

package com.andliage.library.action;

import com.andliage.library.service.UserService;
import org.apache.struts2.StrutsStatics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@Scope("prototype")
public class UserAction extends BaseAction {
    private UserService service;

    public String cur() {
        jsonMap = new HashMap<>();
        jsonMap.put("name", context.getSession().get("curUser"));
        return SUCCESS;
    }

    public String list() {
        HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        int page = Integer.parseInt(request.getParameter("page"));
        String searchText = request.getParameter("search");

        jsonMap = service.list(page, searchText);
        return SUCCESS;
    }

    public String detail() {
        HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        int userId = Integer.parseInt(request.getParameter("id"));

        jsonMap = service.detail(userId);
        return SUCCESS;
    }

    public String update() {
        HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        int userId = Integer.parseInt(request.getParameter("id"));
        int hold = Integer.parseInt(request.getParameter("hold"));
        String pwd = request.getParameter("pwd");
        String adminName = (String) context.getSession().get("curAdmin");

        jsonMap = service.update(userId, hold, pwd, adminName);
        return SUCCESS;
    }

    @Autowired
    public void setService(UserService service) {
        this.service = service;
    }
}
