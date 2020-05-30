/*
 * Author: Andliage Pox
 * Date: 2020-05-30
 */

package com.andliage.library.dao;

import com.andliage.library.entity.Book;
import com.andliage.library.entity.Hold;
import com.andliage.library.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class HoldDAO extends BaseDAO {
    public void saveHold(Hold hold) {
        template.save(hold);
        template.update(hold.getUser());
        template.update(hold.getBook());
        template.flush();
    }

    public void deleteHold(User user, Book book) {
        String hql = "from Hold h where h.user = ? and h.book = ?";
        List<Hold> list = (List<Hold>) template.find(hql, user, book);
        for (Hold hold: list) {
            template.delete(hold);
        }
        template.update(user);
        template.update(book);
        template.flush();
    }
}
