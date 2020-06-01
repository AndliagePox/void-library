/*
 * Author: Andliage Pox
 * Date: 2020-05-21
 */

package com.andliage.library.action;

import com.andliage.library.service.AccountService;
import org.apache.struts2.StrutsStatics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@Scope("prototype")
public class AccountAction extends BaseAction {
    private AccountService service;

    public String login() {
        HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        jsonMap = service.login(username, password);
        if ((int) jsonMap.get("code") == 1) {
            if ("admin".equals(jsonMap.get("type"))) {
                context.getSession().remove("curUser");
                context.getSession().put("curAdmin", username);
            } else {
                context.getSession().remove("curAdmin");
                context.getSession().put("curUser", username);
            }
        }
        return SUCCESS;
    }

    public String reg() {
        HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        jsonMap = service.reg(username, password);
        if ((int) jsonMap.get("code") == 1) {
            context.getSession().put("curUser", username);
        }
        return SUCCESS;
    }

    public String logout() {
        context.getSession().put("curUser", null);
        context.getSession().put("curAdmin", null);
        jsonMap = new HashMap<>();
        jsonMap.put("code", 1);
        return SUCCESS;
    }

    @Autowired
    public void setService(AccountService service) {
        this.service = service;
    }
}
