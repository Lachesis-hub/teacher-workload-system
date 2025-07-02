package com.it.ui;

import com.it.pojo.User;
import com.it.pojo.Workload;
import com.it.util.DataUtil;
import cn.hutool.core.util.IdUtil;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.it.util.DataUtil.saveWorkload;

//  教师工作信息表单,用于显示和处理教师工作信息的表单
public class WorkloadDialog extends JDialog {
    private boolean save = false; // 保存标准,表示工作信息是否成功保存
    private Workload workload; //  当前工作信息
    private User currentUser; //  当前用户
    private MainFrame parent; //  父窗口

    // 输入框,用于输入工作日期/小时数/教师名/描述
    private JTextField dateField;
    private JTextField hoursField;
    private JTextField teacherField;
    private JTextField idField;
    private JTextArea descArea;


    // 构造函数,创建一个教师工作信息对话框
    public WorkloadDialog(JFrame parent,  Workload workload,  User currentUser) {
        super(parent, workload == null ? "添加教师工作信息" : "编辑教师工作信息", true);

        this.parent = (MainFrame) parent; //  保存父窗口
        this.currentUser = currentUser; //  保存当前用户
        this.workload = workload == null ? new Workload() : workload; // 保存当前工作信息

        setSize(600, 400);
        setLocationRelativeTo(parent);

        // 创建并添加表单面板
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // 表单面板,用于输入工作信息
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.add(new JLabel("教师名称："));
        teacherField = new JTextField();
        // 如果当前用户不是管理员，则只显示当前用户名，不可编辑
        if (!currentUser.getUsername().equals("admin")){
            teacherField.setText(currentUser.getUsername());
            teacherField.setEditable(false);
            formPanel.add(teacherField, BorderLayout.CENTER);
        } else {
            JComboBox<String> teacherComboBox = new JComboBox<>();
            for (User user : DataUtil.getAllUsers()) {
                teacherComboBox.addItem(user.getUsername());
            }
            teacherField = new JTextField();
            teacherComboBox.addActionListener(e -> {
                String  selectedTeacher = (String) teacherComboBox.getSelectedItem();
                teacherField.setText(selectedTeacher);
            });

            // 如果是编辑模式，则显示 ID
            if (workload != null && workload.getId() != null) {
                idField = new JTextField(workload.getId());
                idField.setEditable(false); // 只读
            } else {
                idField = new JTextField(IdUtil.simpleUUID()); // 自动生成 UUID
                idField.setEditable(false);
            }

            formPanel.add(new JLabel("ID："));
            formPanel.add(idField);


            String selectedTeacher = (String) teacherComboBox.getSelectedItem();
            teacherField.setText(selectedTeacher);
            formPanel.add(teacherComboBox);
            formPanel.add(teacherField);
        }

        // 工作日期,如果当前工作信息不为空，则显示当前工作日期
        formPanel.add(new JLabel("工作日期(yyyy-mm-dd："));
        dateField = new JTextField();
        if (workload != null && workload.getWorkDate() != null){
            dateField.setText(workload.getWorkDate());
        } else {
            dateField.setText(new SimpleDateFormat("yyyy-mm-dd").format(new Date()));
        }
        formPanel.add(dateField);

        // 小时数,如果当前工作信息不为空，则显示当前小时数
        formPanel.add(new JLabel("工作小时数："));
        hoursField = new JTextField();
        if (workload != null && workload.getHours() != 0){
            hoursField.setText(String.valueOf(workload.getHours()));
        }
        formPanel.add(hoursField);

        // 工作描述,如果当前工作信息不为空，则显示当前工作描述
        formPanel.add(new JLabel("工作描述："));
        descArea = new JTextArea(3,20);
        if (workload != null && workload.getDescription() != null){
            descArea.setText(workload.getDescription());
        }
        formPanel.add(new JScrollPane(descArea));
        panel.add(formPanel, BorderLayout.CENTER);

        // 按钮面板,包含保存和取消按钮
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> saveWorkload());
        JButton  cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel);
    }

    // 保存教师工作信息的方法,用于验证输入并保存工作信息,若保存成功则关闭表单
    private void saveWorkload() {
        try {
            String workDate = dateField.getText();
            float hours = Float.parseFloat(hoursField.getText());
            String description = descArea.getText();
            String teacher = teacherField.getText();

            workload.setTeacher(teacher);
            workload.setWorkDate(workDate);
            workload.setHours(hours);
            workload.setDescription(description);

            DataUtil.saveWorkload(workload);
            save = true;
            parent.refreshData();
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,"保存失败：" + ex.getMessage());
        }
    }
}
