/*
 * Author: Andliage Pox
 * Date: 2020-05-21
 */

package com.andliage.library.service;

import com.andliage.library.dao.AccountDAO;
import com.andliage.library.dao.BRLogDAO;
import com.andliage.library.dao.BookDAO;
import com.andliage.library.dao.HoldDAO;
import com.andliage.library.entity.BRLog;
import com.andliage.library.entity.Book;
import com.andliage.library.entity.Hold;
import com.andliage.library.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {
    /**
     * 用户每页书目容量
     */
    public static final int COUNT_PER_PAGE_USER = 6;

    /**
     * 操作状态码
     */
    public static final int OPTION_BORROW   = 1; // 借阅
    public static final int OPTION_RETURN   = 2; // 归还
    public static final int OPTION_NO_REP   = 3; // 无库存
    public static final int OPTION_HOLD_MAX = 4; // 满持有
    public static final int OPTION_LOGIN    = 5; // 登录

    /**
     * 借还日志类型码
     */
    public static final int BR_LOG_BORROW = 1; // 借阅
    public static final int BR_LOG_RETURN = 2; // 归还

    private BookDAO bookDAO;
    private HoldDAO holdDAO;
    private BRLogDAO brLogDAO;
    private AccountDAO accountDAO;

    public Map<String, Object> listForUser(
            int page,
            int sortType,
            int listType,
            String searchText,
            String username
    ) {
        Map<String, Object> res = new HashMap<>();
        User user = accountDAO.findUserByName(username);
        long count = bookDAO.getBookCountForUser(listType, searchText, user);
        List<Book> list = bookDAO.findBooksForUser(page, sortType, listType, searchText, user);
        List<Map<String, Object>> bookMaps = new ArrayList<>(list.size());

        for (Book book: list) {
            Map<String, Object> map = book.toMap();
            int option;
            if (user.getHoldBooks().contains(book)) {
                option = OPTION_RETURN;
            } else if (user.getHoldBooks().size() == user.getMaxHold()) {
                option = OPTION_HOLD_MAX;
            } else if (book.getCount() - book.getHoldUsers().size() > 0) {
                option = OPTION_BORROW;
            } else {
                option = OPTION_NO_REP;
            }
            map.put("option", option);
            bookMaps.add(map);
        }

        res.put("pages", Math.ceil((double) count / COUNT_PER_PAGE_USER));
        res.put("books", bookMaps);
        return res;
    }

    public Map<String, Object> listForAdmin() {
        return null;
    }

    public Map<String, Object> listForAnonymous(
            int page,
            int sortType,
            String searchText
    ) {
        Map<String, Object> res = new HashMap<>();
        long count = bookDAO.getBookCountForAnonymous(searchText);
        List<Book> list = bookDAO.findBooksForAnonymous(page, sortType, searchText);
        List<Map<String, Object>> bookMaps = new ArrayList<>(list.size());

        for (Book book: list) {
            Map<String, Object> map = book.toMap();
            map.put("option", OPTION_LOGIN);
            bookMaps.add(map);
        }

        res.put("pages", Math.ceil((double) count / COUNT_PER_PAGE_USER));
        res.put("books", bookMaps);
        return res;
    }
    
    public Map<String, Object> borrowBook(String username, int bookId) {
        Map<String, Object> res = new HashMap<>();
        User user = accountDAO.findUserByName(username);
        Book book = bookDAO.findBookById(bookId);

        if (book == null) {
            res.put("code", 0);
            res.put("msg", "该书不存在");
            return res;
        }

        if (user.getHoldBooks().size() == user.getMaxHold()) {
            res.put("code", 0);
            res.put("msg", "已达借阅上限");
            return res;
        }

        if (user.getHoldBooks().contains(book)) {
            res.put("code", 0);
            res.put("msg", "已持有该书");
            return res;
        }

        Hold hold = new Hold();
        hold.setUser(user);
        hold.setBook(book);
        holdDAO.saveHold(hold);

        saveNewBRLog(user, book, BR_LOG_BORROW);

        res.put("code", 1);
        return res;
    }

    public Map<String, Object> returnBook(String username, int bookId) {
        Map<String, Object> res = new HashMap<>();
        User user = accountDAO.findUserByName(username);
        Book book = bookDAO.findBookById(bookId);

        if (book == null) {
            res.put("code", 0);
            res.put("msg", "书本不存在");
            return res;
        }

        if (!user.getHoldBooks().contains(book)) {
            res.put("code", 0);
            res.put("msg", "未借阅该书");
            return res;
        }

        holdDAO.deleteHold(user, book);
        saveNewBRLog(user, book, BR_LOG_RETURN);

        res.put("code", 1);
        return res;
    }

    private void saveNewBRLog(User user, Book book, int type) {
        BRLog log = new BRLog();
        log.setUser(user);
        log.setBook(book);
        log.setType(type);
        log.setTime(new Timestamp(System.currentTimeMillis()));
        brLogDAO.saveBRLog(log);
    }

    @Autowired
    public void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Autowired
    public void setHoldDAO(HoldDAO holdDAO) {
        this.holdDAO = holdDAO;
    }

    @Autowired
    public void setBrLogDAO(BRLogDAO brLogDAO) {
        this.brLogDAO = brLogDAO;
    }

    @Autowired
    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }
}
