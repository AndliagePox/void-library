/*
 * Author: Andliage Pox
 * Date: 2020-05-20
 */

package com.andliage.library.action;

import com.andliage.library.service.BookService;
import org.apache.struts2.StrutsStatics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
@Scope("prototype")
public class BookAction extends BaseAction {
    private BookService service;

    public String list() {
        HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        int page = Integer.parseInt(request.getParameter("page"));
        int sortType = Integer.parseInt(request.getParameter("sortType"));
        int listType = Integer.parseInt(request.getParameter("listType"));
        String searchText = request.getParameter("search");

        if (context.getSession().get("curUser") != null) {
            String username = (String) context.getSession().get("curUser");
            jsonMap = service.listForUser(page, sortType, listType, searchText, username);
        } else if (context.getSession().get("curAdmin") != null) {
            jsonMap = service.listForAdmin(page, searchText);
        } else {
            jsonMap = service.listForAnonymous(page, sortType, searchText);
        }
        return SUCCESS;
    }

    public String borrowBook() {
        HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        String username = (String) context.getSession().get("curUser");
        int bookId = Integer.parseInt(request.getParameter("bid"));

        jsonMap = service.borrowBook(username, bookId);
        return SUCCESS;
    }

    public String returnBook() {
        HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        String username = (String) context.getSession().get("curUser");
        int bookId = Integer.parseInt(request.getParameter("bid"));

        jsonMap = service.returnBook(username, bookId);
        return SUCCESS;
    }

    public String detail() {
        HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        int bookId = Integer.parseInt(request.getParameter("id"));

        jsonMap = service.detail(bookId);
        return SUCCESS;
    }

    public String add() {
        HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);

        String name = request.getParameter("name");
        String author = request.getParameter("author");
        String intro = request.getParameter("intro");
        int hot = Integer.parseInt(request.getParameter("hot"));
        int count = Integer.parseInt(request.getParameter("count"));
        String adminName = (String) context.getSession().get("curAdmin");

        jsonMap = service.add(hot, count, name, author, intro, adminName);
        return SUCCESS;
    }

    public String update() {
        HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        int bookId = Integer.parseInt(request.getParameter("id"));
        int hot = Integer.parseInt(request.getParameter("hot"));
        int count = Integer.parseInt(request.getParameter("count"));
        String name = request.getParameter("name");
        String author = request.getParameter("author");
        String intro = request.getParameter("intro");
        String adminName = (String) context.getSession().get("curAdmin");

        jsonMap = service.update(bookId, hot, count, name, author, intro, adminName);
        return SUCCESS;
    }

    @Autowired
    public void setService(BookService service) {
        this.service = service;
    }
}
