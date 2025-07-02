package com.it;

import com.it.pojo.User;
import com.it.ui.LoginFrame;

import java.sql.SQLOutput;

/**
 * @author 陈辉
 * @data 2025 17:20
 */
// 启动类
public class App {
    public static void main(String[] args) {

        //创建登录界面
        new LoginFrame();

        /* System.out.println("Hello");

        //测试:创建对象
        //无参构造 创建对象
        User user1 = new User();

        user1.setId("gs-001");
        user1.setUsername("amy");
        user1.setPassword("123456");
        user1.setRole("teacher");

        System.out.println(user1.getId());
        System.out.println(user1.getUsername());
        System.out.println(user1.getPassword());
        System.out.println(user1.getRole());

        System.out.println("----------------------------------");

        //有参构造 创建对象
        User user2 = new User("gs-002","tom","123456","admin");

        System.out.println(user2.getId());
        System.out.println(user2.getUsername());
        System.out.println(user2.getPassword());
        System.out.println(user2.getRole()); */
    }
}
