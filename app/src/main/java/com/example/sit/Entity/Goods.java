package com.example.sit.Entity;

import java.util.ArrayList;

/**
 * @Author: Iced
 * @Date: 2022/3/27 12:59
 */
public class Goods {
    private int id;
    private int user_id;
    private String introduction;
    private int price;
    private int status;
    private String created_at;
    private String updated_at;

    public Goods() {}

    public Goods(int id, int user_id, String introduction, int price, int status, String created_at, String updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.introduction = introduction;
        this.price = price;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
        return "Goods{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", introduction='" + introduction + '\'' +
                ", price=" + price +
                ", status=" + status +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
