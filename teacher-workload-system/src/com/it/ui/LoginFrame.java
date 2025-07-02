package com.it.ui;

import com.it.pojo.User;
import com.it.util.DataUtil;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;

// 登录界面,用于登录
public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        // 1.准备最外层面板容器：JPanel
        JPanel panel = new JPanel(new GridLayout(4,1,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // 2.添加用户名相关组件：JLabel + JTextField
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernameField = new JTextField(15);
        userPanel.add(new JLabel("用户名："));
        userPanel.add(usernameField);
        panel.add(userPanel);

        // 3.添加密码相关组件：JLabel + JPasswordField
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
// 创建密码框和显示/隐藏按钮
         passwordField = new JPasswordField(15);
        JButton togglePasswordBtn = new JButton("👁️");

// 使用 JPanel 包裹，方便布局
        passwordPanel.add(new JLabel("密 码："));
        passwordPanel.add(passwordField);
        passwordPanel.add(togglePasswordBtn); // 添加按钮到面板中

// 添加按钮点击事件
        togglePasswordBtn.addActionListener(e -> {
            if (passwordField.getEchoChar() == '•') {
                // 当前是密码模式，切换为明文
                passwordField.setEchoChar((char) 0); // 显示字符
                togglePasswordBtn.setText("👁️/");
            } else {
                // 当前是明文，切换回密码模式
                passwordField.setEchoChar('•'); // 隐藏字符
                togglePasswordBtn.setText("👁️");
            }
        });

        panel.add(passwordPanel);

        // 4.添加登录按钮：JButton
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("登录");
        JButton registerButton = new JButton("注册");

        // 登录按钮点击事件
        loginButton.addActionListener(e -> login());

        // 注册按钮点击事件
        registerButton.addActionListener(e -> register());

        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        panel.add(buttonPanel);

        // 设置面板为窗体内容
        this.add(panel);

        // 设置窗体属性
        this.setTitle("教师工作量管理系统-登录");
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // 新增：按下 Enter 键触发登录功能
        usernameField.addActionListener(e -> login());
        passwordField.addActionListener(e -> login());
    }


    // 登录功能
    private void login() {
        //1.拿到用户录入的用户名、密码
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        //2，校验用户信息是否有效
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空！");
            return;
        }

        //3.根据用户查询数据库，获取用户信息；查不到说明当前用户信息存在错误,给出提示
        User user = DataUtil.getUserByUsername(username);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "用户不存在！");
            return;
        }

        //4.校验密码是否有效
        if (!password.equals(user.getPassword())) {
            JOptionPane.showMessageDialog(this, "密码错误！");
            return;
        }

        //5.如果用户信息有效，则跳转到主界面 -- MainFrame
        JOptionPane.showMessageDialog(this, "登录成功！");
        this.dispose();
        new MainFrame(user).setVisible(true);
        }

    // 注册功能
    private void register() {
        //1.拿到用户录入的用户名、密码
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        //2.校验用户信息是否有效
        //2.1.用户名和密码不能为空
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空！");
            return;
        }

        //2.2.用户名不能重复
        if (DataUtil.getUserByUsername(username) != null) {
            JOptionPane.showMessageDialog(this, "用户名已存在！");
            return;
        }

        //3.当前用户信息封装为User对象，保存到数据库中
        User user1 = new User();
        user1.setId(String.valueOf(System.currentTimeMillis()));
        user1.setUsername(username);
        user1.setPassword(password);
        user1.setRole("teacher");
        DataUtil.saveUser(user1);

        //4.弹“注册成功”的提示信息
        JOptionPane.showMessageDialog(this, "注册成功！");
    }


}
