/*
 * Author: Andliage Pox
 * Date: 2020-05-21
 */

package com.andliage.library.service;

import com.andliage.library.dao.BookDAO;
import com.andliage.library.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {
    private BookDAO dao;

    public Map<String, Object> list(int page, int sortType, int listType, String searchText) {
        Map<String, Object> res = new HashMap<>();
        List<Book> list = dao.getBookList(page, sortType, listType, searchText);
        res.put("pages", 5);
        res.put("books", list);
        return res;
    }

    @Autowired
    public void setDao(BookDAO dao) {
        this.dao = dao;
    }
}
