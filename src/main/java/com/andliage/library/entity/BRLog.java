/*
 * Author: Andliage Pox
 * Date: 2020-05-12
 */

package com.andliage.library.entity;

import java.sql.Timestamp;

public class BRLog {
    private int id;
    private Integer type;
    private Timestamp time;
    private User user;
    private Book book;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
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

        BRLog brLog = (BRLog) o;

        if (id != brLog.id) return false;
        if (type != null ? !type.equals(brLog.type) : brLog.type != null) return false;
        return time != null ? time.equals(brLog.time) : brLog.time == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }

}
