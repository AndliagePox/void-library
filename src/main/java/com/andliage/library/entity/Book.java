/*
 * Author: Andliage Pox
 * Date: 2020-05-12
 */

package com.andliage.library.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class Book {
    private int id;
    private String name;
    private String author;
    private String refer;
    private String intro;
    private Integer count;
    private Integer status;
    private Integer hot;
    private Timestamp createTime;
    private Set<User> holdUsers = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Set<User> getHoldUsers() {
        return holdUsers;
    }

    public void setHoldUsers(Set<User> holdUsers) {
        this.holdUsers = holdUsers;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("author", author);
        map.put("intro", intro);
        map.put("hot", hot);
        map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (id != book.id) return false;
        if (!Objects.equals(name, book.name)) return false;
        if (!Objects.equals(author, book.author)) return false;
        if (!Objects.equals(refer, book.refer)) return false;
        if (!Objects.equals(count, book.count)) return false;
        return Objects.equals(status, book.status);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (refer != null ? refer.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
