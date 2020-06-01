/*
 * Author: Andliage Pox
 * Date: 2020-05-21
 */

package com.andliage.library.service;

import com.andliage.library.dao.*;
import com.andliage.library.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {
    /**
     * 用户每页书目容量
     */
    public static final int COUNT_PER_PAGE_USER = 6;

    /**
     * 管理员每页书目容量
     */
    public static final int COUNT_PER_PAGE_ADMIN = 10;

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
    private OPLogDAO opLogDAO;
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

        res.put("pages", (int) Math.ceil((double) count / COUNT_PER_PAGE_USER));
        res.put("books", bookMaps);
        return res;
    }

    public Map<String, Object> listForAdmin(int page, String searchText) {
        Map<String, Object> res = new HashMap<>();
        long count = bookDAO.getBookCountForAdmin(searchText);
        List<Book> list = bookDAO.findBooksForAdmin(page, searchText);
        List<Map<String, Object>> bookMaps = new ArrayList<>(list.size());

        for (Book book: list) {
            Map<String, Object> map = book.toMap();
            bookMaps.add(map);
        }

        res.put("pages", (int) Math.ceil((double) count / COUNT_PER_PAGE_ADMIN));
        res.put("books", bookMaps);
        return res;
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

        res.put("pages", (int) Math.ceil((double) count / COUNT_PER_PAGE_USER));
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

    public Map<String, Object> detail(int bookId) {
        Book book = bookDAO.findBookById(bookId);
        Map<String, Object> res = book.toMap();
        res.put("users", book.getHoldUsers().stream().map(User::getUsername).collect(Collectors.toList()));
        return res;
    }

    public Map<String, Object> add(
            int hot, int count,
            String name, String author, String intro, String adminName
    ) {
        Map<String, Object> res = new HashMap<>();

        Book book = new Book();
        book.setHot(hot);
        book.setCount(count);
        book.setName(name);
        book.setAuthor(author);
        book.setIntro(intro);
        book.setCreateTime(new Timestamp(System.currentTimeMillis()));
        bookDAO.saveBook(book);

        Admin admin = accountDAO.findAdminByName(adminName);
        String opLogContent = "进行了 <b>添加</b> 书籍 [" + book.getId() + "] :" +
                "<br><b>书名</b>: <b>" + name +
                "</b><br><b>作者</b>: <b>" + author +
                "</b><br><b>热度</b>: <b>" + hot +
                "</b><br><b>库存</b>: <b>" + count +
                "</b><br><b>简介</b>: <b>" + intro;
        saveNewOPLog(admin, opLogContent);

        res.put("code", 1);
        return res;
    }

    public Map<String, Object> update(
            int bookId, int hot, int count,
            String name, String author, String intro, String adminName
    ) {
        Map<String, Object> res = new HashMap<>();
        Book book = bookDAO.findBookById(bookId);
        Admin admin = accountDAO.findAdminByName(adminName);
        StringBuilder opLogContent = new StringBuilder("进行了 <b>更新</b> 书籍 [" + bookId + "] 信息：");

        if (!book.getName().equals(name)) {
            opLogContent.append("<br>将 <b>书名</b> 由 <b>").append(book.getName())
                    .append("</b> 修改为 <b>").append(name).append("</b>");
            book.setName(name);
        }

        if (!book.getAuthor().equals(author)) {
            opLogContent.append("<br>将 <b>作者</b> 由 <b>").append(book.getAuthor())
                    .append("</b> 修改为 <b>").append(author).append("</b>");
            book.setAuthor(author);
        }

        if (book.getHot() != hot) {
            opLogContent.append("<br>将 <b>热度</b> 由 <b>").append(book.getHot())
                    .append("</b> 修改为 <b>").append(hot).append("</b>");
            book.setHot(hot);
        }

        if (book.getCount() != count) {
            if (count < book.getHoldUsers().size()) {
                res.put("code", 0);
                res.put("msg", "库存不能小于已借阅人数");
                return res;
            }
            opLogContent.append("<br>将 <b>库存</b> 由 <b>").append(book.getCount())
                    .append("</b> 修改为 <b>").append(count).append("</b>");
            book.setCount(count);
        }

        if (!book.getIntro().equals(intro)) {
            opLogContent.append("<br>将 <b>简介</b> 由 <b>").append(book.getIntro())
                    .append("</b> 修改为 <b>").append(intro).append("</b>");
            book.setIntro(intro);
        }

        saveNewOPLog(admin, opLogContent.toString());
        bookDAO.updateBook(book);

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

    private void saveNewOPLog(Admin admin, String content) {
        OPLog log = new OPLog();
        log.setAdmin(admin);
        log.setContent(content);
        log.setTime(new Timestamp(System.currentTimeMillis()));
        opLogDAO.saveOPLog(log);
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
    public void setOpLogDAO(OPLogDAO opLogDAO) {
        this.opLogDAO = opLogDAO;
    }

    @Autowired
    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }
}
