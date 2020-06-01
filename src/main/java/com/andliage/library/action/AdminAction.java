/*
 * Author: Andliage Pox
 * Date: 2020-05-31
 */

package com.andliage.library.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;

@Controller
@Scope("prototype")
public class AdminAction extends BaseAction {
    public String cur() {
        jsonMap = new HashMap<>();
        jsonMap.put("name", context.getSession().get("curAdmin"));
        return SUCCESS;
    }
}
