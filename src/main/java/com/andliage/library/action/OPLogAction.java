/*
 * Author: Andliage Pox
 * Date: 2020-06-01
 */

package com.andliage.library.action;

import com.andliage.library.service.OPLogService;
import org.apache.struts2.StrutsStatics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
@Scope("prototype")
public class OPLogAction extends BaseAction {
    private OPLogService service;

    public String list() {
        HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        int page = Integer.parseInt(request.getParameter("page"));

        jsonMap = service.list(page);
        return SUCCESS;
    }

    @Autowired
    public void setService(OPLogService service) {
        this.service = service;
    }
}
