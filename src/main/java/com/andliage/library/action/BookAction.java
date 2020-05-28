/*
 * Author: Andliage Pox
 * Date: 2020-05-20
 */

package com.andliage.library.action;

import com.andliage.library.service.BookService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.StrutsStatics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@Scope("prototype")
public class BookAction extends ActionSupport {
    public Map<String, Object> jsonMap;
    private BookService service;

    private final ActionContext context = ActionContext.getContext();

    public String list() {
        HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        jsonMap = service.list(
                1, 1, 1, ""
        );
        return SUCCESS;
    }

    @Autowired
    public void setService(BookService service) {
        this.service = service;
    }
}
