/*
 * Author: Andliage Pox
 * Date: 2020-05-21
 */

package com.andliage.library.dao;

import com.andliage.library.entity.Book;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class BookDAO extends BaseDAO {
    public List<Book> getBookList(int page, int sortType, int listType, String searchText) {
        return template.find("from Book");
    }
}
