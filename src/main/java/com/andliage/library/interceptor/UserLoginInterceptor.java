/*
 * Author: Andliage Pox
 * Date: 2020-05-30
 */

package com.andliage.library.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import java.util.HashMap;
import java.util.Map;

public class UserLoginInterceptor extends AbstractInterceptor {
    public Map<String, Object> jsonMap;

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        if (actionInvocation.getInvocationContext().getSession().get("curUser") == null) {
            jsonMap = new HashMap<>();
            jsonMap.put("code", 0);
            actionInvocation.getInvocationContext().put("jsonMap", jsonMap);
            return "userNotLogin";
        } else {
            return actionInvocation.invoke();
        }
    }
}
