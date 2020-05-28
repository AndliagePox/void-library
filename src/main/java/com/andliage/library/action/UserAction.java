/*
 * Author: Andliage Pox
 * Date: 2020-05-21
 */

package com.andliage.library.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
@Scope("prototype")
public class UserAction extends ActionSupport {
    public Map<String, Object> jsonMap;
    private final ActionContext context = ActionContext.getContext();

    public String cur() {
        jsonMap = new HashMap<>();
        jsonMap.put("name", context.getSession().get("curUser"));
        return SUCCESS;
    }
}
