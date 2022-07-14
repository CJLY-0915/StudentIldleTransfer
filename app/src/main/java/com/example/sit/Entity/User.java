package com.example.sit.Entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Iced
 * @Date: 2022/3/27 12:55
 */
public class User implements Serializable {
    private int id;
    private String account;
    private String nickname;
    private int sex;
    private Date birthday;
    private String introduction;
    private String university;
    private String avatar_url;
    private String created_at;
    private String updated_at;

    public User () {}

    public User(int id, String account, String nickname, int sex, Date birthday, String introduction, String university, String avatar_url, String created_at, String updated_at) {
        this.id = id;
        this.account = account;
        this.nickname = nickname;
        this.sex = sex;
        this.birthday = birthday;
        this.introduction = introduction;
        this.university = university;
        this.avatar_url = avatar_url;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", introduction='" + introduction + '\'' +
                ", university='" + university + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
