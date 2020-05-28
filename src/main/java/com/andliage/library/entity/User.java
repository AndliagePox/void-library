/*
 * Author: Andliage Pox
 * Date: 2020-05-12
 */

package com.andliage.library.entity;

import java.sql.Timestamp;

public class User {
    private int id;
    private String username;
    private String password;
    private Integer maxHold;
    private Timestamp createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMaxHold() {
        return maxHold;
    }

    public void setMaxHold(Integer maxHold) {
        this.maxHold = maxHold;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (maxHold != null ? !maxHold.equals(user.maxHold) : user.maxHold != null) return false;
        return createTime != null ? createTime.equals(user.createTime) : user.createTime == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (maxHold != null ? maxHold.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }
}
