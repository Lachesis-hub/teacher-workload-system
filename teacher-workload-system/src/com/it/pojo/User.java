package com.it.pojo;

//todo: 用户模型
public class User {
    //成员变量
    private String id;
    private String username;
    private String password;
    private String role;


    //构造方法
    //无参构造
    public User() {
    }

    //带参构造
    public User(String id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    //封装原则，针对私有成员变量，提供对应get/set方法（成员方法）
    //右键 Generate（生成） getter and setter ctrl+A 全选 一键生成get/set方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}

