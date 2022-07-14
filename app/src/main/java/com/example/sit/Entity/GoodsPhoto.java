package com.example.sit.Entity;

import java.util.Date;

/**
 * @Author: Iced
 * @Date: 2022/4/22 18:34
 */
public class GoodsPhoto {
    private int id;
    private int goods_id;
    private String photo_url;
    private String created_at;
    private String updated_at;

    public GoodsPhoto() {}

    public GoodsPhoto(int id, int goods_id, String photo_url, String created_at, String updated_at) {
        this.id = id;
        this.goods_id = goods_id;
        this.photo_url = photo_url;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
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
        return "GoodsPhoto{" +
                "id=" + id +
                ", goods_id=" + goods_id +
                ", photo_url='" + photo_url + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
