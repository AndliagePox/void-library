/*
 * Author: Andliage Pox
 * Date: 2020-05-21
 */

package com.andliage.library.action;

import com.andliage.library.service.UserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.StrutsStatics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@Scope("prototype")
public class UserAction extends ActionSupport {
    public Map<String, Object> jsonMap;
    private UserService service;

    private final ActionContext context = ActionContext.getContext();

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

    @Autowired
    public void setService(UserService service) {
        this.service = service;
    }
}
