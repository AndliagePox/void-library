/*
 * Author: Andliage Pox
 * Date: 2020-05-21
 */

package com.andliage.library.dao;

import com.andliage.library.entity.Book;
import com.andliage.library.entity.User;
import com.andliage.library.service.BookService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class BookDAO extends BaseDAO {
    public Book findBookById(int id) {
        String hql = "from Book b where b.id = ?";
        List<Book> list = (List<Book>) template.find(hql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public Integer nextId() {
        String hql = "select max(b.id) from Book b";
        List<Integer> list = (List<Integer>) template.find(hql);
        return list.get(0) + 1;
    }

    public void updateBook(Book book) {
        template.update(book);
        template.flush();
    }

    public void saveBook(Book book) {
        template.save(book);
        template.flush();
    }

    public List<Book> findBooksForAnonymous(int page, int sortType, String searchText) {
        if (searchText == null) searchText = "";
        Session session = template.getSessionFactory().openSession();
        String hql = "from Book b " +
                "where b.name like :search " +
                "or b.author like :search " +
                "or b.intro like :search " +
                "order by ";
        if (sortType == 1) {
            hql += "b.hot desc ";
        } else {
            hql += "b.createTime desc ";
        }
        Query query = session.createQuery(hql);
        query.setString("search", "%" + searchText + "%");
        query.setFirstResult((page - 1) * BookService.COUNT_PER_PAGE_USER);
        query.setMaxResults(BookService.COUNT_PER_PAGE_USER);
        List<Book> list = query.list();
        session.close();
        return list;
    }

    public long getBookCountForAnonymous(String searchText) {
        if (searchText == null) searchText = "";
        Session session = template.getSessionFactory().openSession();
        String hql = "select count(*) from Book b " +
                "where b.name like :search " +
                "or b.author like :search " +
                "or b.intro like :search";
        Query query = session.createQuery(hql);
        query.setString("search", "%" + searchText + "%");
        long count = (long) query.uniqueResult();
        session.close();
        return count;
    }

    public List<Book> findBooksForUser(
            int page,
            int sortType,
            int listType,
            String searchText,
            User user
    ) {
        if (searchText == null) searchText = "";
        Session session = template.getSessionFactory().openSession();
        String hql = "from Book b " +
                "where (b.name like :search " +
                "or b.author like :search " +
                "or b.intro like :search) ";
        if (listType == 2) {
            hql += "and b in (select h.book from Hold h where h.user = :user) ";
        }
        if (sortType == 1) {
            hql += "order by b.hot desc ";
        } else {
            hql += "order by b.createTime desc ";
        }
        Query query = session.createQuery(hql);
        query.setString("search", "%" + searchText + "%");
        if (listType == 2) {
            query.setEntity("user", user);
        }
        query.setFirstResult((page - 1) * BookService.COUNT_PER_PAGE_USER);
        query.setMaxResults(BookService.COUNT_PER_PAGE_USER);
        List<Book> list = query.list();
        session.close();
        return list;
    }

    public long getBookCountForUser(
            int listType,
            String searchText,
            User user
    ) {
        if (searchText == null) searchText = "";
        Session session = template.getSessionFactory().openSession();
        String hql = "select count(*) from Book b " +
                "where (b.name like :search " +
                "or b.author like :search " +
                "or b.intro like :search) ";
        if (listType == 2) {
            hql += "and b in (select h.book from Hold h where h.user = :user) ";
        }
        Query query = session.createQuery(hql);
        query.setString("search", "%" + searchText + "%");
        if (listType == 2) {
            query.setEntity("user", user);
        }
        long count = (long) query.uniqueResult();
        session.close();
        return count;
    }

    public List<Book> findBooksForAdmin(int page, String searchText) {
        if (searchText == null) searchText = "";
        Session session = template.getSessionFactory().openSession();
        String hql = "from Book b " +
                "where b.name like :search " +
                "or b.author like :search " +
                "or b.intro like :search ";
        Query query = session.createQuery(hql);
        query.setString("search", "%" + searchText + "%");
        query.setFirstResult((page - 1) * BookService.COUNT_PER_PAGE_ADMIN);
        query.setMaxResults(BookService.COUNT_PER_PAGE_ADMIN);
        List<Book> list = query.list();
        session.close();
        return list;
    }

    public long getBookCountForAdmin(String searchText) {
        return getBookCountForAnonymous(searchText);
    }
}
