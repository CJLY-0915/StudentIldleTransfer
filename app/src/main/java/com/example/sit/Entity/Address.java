package com.example.sit.Entity;

/**
 * @Author: Iced
 * @Date: 2022/5/1 19:17
 */
public class Address {
    private int id;
    private int user_id;
    private String number;
    private String name;
    private String address;
    private String created_at;
    private String updated_at;

    public Address(int id, int user_id, String number, String name, String address) {
        this.id = id;
        this.user_id = user_id;
        this.number = number;
        this.name = name;
        this.address = address;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
