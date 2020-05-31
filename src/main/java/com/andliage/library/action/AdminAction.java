/*
 * Author: Andliage Pox
 * Date: 2020-05-31
 */

package com.andliage.library.action;

import com.opensymphony.xwork2.ActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

import static com.opensymphony.xwork2.Action.SUCCESS;

@Controller
@Scope("prototype")
public class AdminAction {
    public Map<String, Object> jsonMap;
    private final ActionContext context = ActionContext.getContext();

    public String cur() {
        jsonMap = new HashMap<>();
        jsonMap.put("name", context.getSession().get("curAdmin"));
        return SUCCESS;
    }
}
