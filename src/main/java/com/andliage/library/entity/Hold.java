/*
 * Author: Andliage Pox
 * Date: 2020-05-12
 */

package com.andliage.library.entity;

public class Hold {
    private int id;
    private User user;
    private Book book;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hold hold = (Hold) o;

        return id == hold.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
