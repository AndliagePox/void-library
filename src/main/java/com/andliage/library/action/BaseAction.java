/*
 * Author: Andliage Pox
 * Date: 2020-06-01
 */

package com.andliage.library.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import java.util.Map;

public class BaseAction extends ActionSupport {
    public Map<String, Object> jsonMap;

    protected final ActionContext context = ActionContext.getContext();
}
