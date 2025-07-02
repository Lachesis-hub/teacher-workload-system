package com.it.ui;



import com.it.pojo.User;
import com.it.pojo.Workload;
import com.it.util.DataUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 主界面 -- MainFrame 实现教师工作量管理系统的主界面
 */

public class MainFrame extends JFrame {
    private User currentUser;
    private DefaultTableModel tableModel;
    private JTable workloadTable;

    // 登录页面
    public MainFrame(User user) {
        this.currentUser = user;
        setTitle("教师工作量管理 - ");
        setSize(800, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //todo: 创建数据表格

        //构建工具栏
        JMenuBar  menuBar = new JMenuBar();
        //包含两个子菜单，数据处理、其他操作
        JMenu dataMenu = new JMenu("数据处理");
        //数据处理菜单包含两个菜单项，添加、刷新
        JMenuItem addItem = new JMenuItem("添加");
        addItem.addActionListener(e -> addWorkload());
        JMenuItem refreshItem = new JMenuItem("刷新");
        refreshItem.addActionListener(e -> refreshData());
        dataMenu.add(addItem);
        dataMenu.add(refreshItem);

        // 数据处理菜单中增加“月度统计”
        if ("admin".equals(currentUser.getRole())) {
            JMenuItem summaryItem = new JMenuItem("月度统计");
            summaryItem.addActionListener(e -> showWorkloadSummary());
            dataMenu.add(summaryItem);
        }


        //其他操作菜单包含一个菜单项，退出
        JMenu otherMenu = new JMenu("其他操作");
        // 在工具栏菜单“其他操作”中增加“修改密码”
        JMenuItem changePasswordItem = new JMenuItem("修改密码");
        changePasswordItem.addActionListener(e -> showChangePasswordDialog());
        otherMenu.add(changePasswordItem);
        JMenuItem exitItem = new JMenuItem("退出");
        exitItem.addActionListener(e -> {
            this.dispose(); //关闭当前页面
            new LoginFrame().setVisible(true); //打开登录页面
        });
        otherMenu.add(exitItem);
        //将菜单添加到工具栏
        menuBar.add(dataMenu);
        menuBar.add(otherMenu);
        //将工具栏添加到窗口
        this.setJMenuBar(menuBar);




        // 创建数据表格
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "教师姓名", "日期", "工作耗时", "工作内容描述", "操作"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 1 && column <= 4;
            }
        };

        workloadTable = new JTable(tableModel);
        workloadTable.setRowHeight(25);

//  设置第1~4列为可编辑
        for (int i = 1; i <= 4; i++) {
            workloadTable.getColumnModel().getColumn(i).setCellEditor(new WorkloadCellEditor(i));
        }

        //设置操作列的渲染器和编辑器
        TableColumn buttonColumn = workloadTable.getColumnModel().getColumn(5);
        buttonColumn.setCellRenderer(new ButtonPanelRenderer());
        buttonColumn.setCellEditor(new ButtonPanelEditor(workloadTable,this));
        workloadTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.add(new JScrollPane(workloadTable), BorderLayout.CENTER);
        refreshData();

// 表格标题，如果是管理员则显示所有教师工作量，否则只显示当前用户所负责的教师工作量
// 新增功能 搜索框 加入搜索功能 根据关键词模糊查询工作量（教师名、日期、描述）合并入标题
// 创建顶部面板，包含标题和搜索框
        JPanel topPanel = new JPanel(new BorderLayout());

// 标题部分
        JLabel titleLabel;
        if ("admin".equals(currentUser.getRole())) {
            titleLabel = new JLabel("所有教师工作量", SwingConstants.CENTER);
        } else {
            titleLabel = new JLabel("我的工作量", SwingConstants.CENTER);
        }
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        topPanel.add(titleLabel, BorderLayout.CENTER);

// 搜索框部分
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // 右对齐
        JTextField searchField = new JTextField(30);
        searchField.setText("输入关键字搜索...");
        searchPanel.add(new JLabel("搜索:"));
        searchPanel.add(searchField);
        topPanel.add(searchPanel, BorderLayout.EAST); // 放在右边

// 添加到窗口
        this.add(topPanel, BorderLayout.NORTH);

        // 添加搜索监听器 为搜索字段添加文档监听器，以响应文本变化
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            // 文本插入时调用
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterWorkloads(searchField.getText());
            }

            // 文本移除时调用
            @Override
            public void removeUpdate(DocumentEvent e) {
                filterWorkloads(searchField.getText());
            }

            // 文本改变时调用
            @Override
            public void changedUpdate(DocumentEvent e) {
                filterWorkloads(searchField.getText());
            }


            /*
             根据关键字过滤工作量数据
             @param keyword 搜索字段的文本，用作过滤的关键字
             */
            private void filterWorkloads(String keyword) {
                tableModel.setRowCount(0); // 清空当前表格数据

                List<Workload> results = new ArrayList<>();

                // 如果关键字为空，根据当前用户角色加载所有或特定用户的工作量数据
                if (keyword.isEmpty()) {
                    if ("admin".equals(currentUser.getRole())) {
                        results = DataUtil.getAllWorkloads();
                    } else {
                        results = DataUtil.getWorkloadsByTeacherName(currentUser.getUsername());
                    }
                } else {
                    // 如果关键字不为空，搜索匹配关键字的工作量数据
                    results = DataUtil.searchWorkloads(keyword);
                }

                // 将过滤后的数据添加到表格模型中
                for (Workload workload : results) {
                    tableModel.addRow(new Object[]{
                            workload.getId(),
                            workload.getTeacher(),
                            workload.getWorkDate(),
                            workload.getHours(),
                            workload.getDescription(),
                            ""
                    });
                }
            }
        });


        //加载数据
        refreshData();
    }

    /**
     * 刷新工作量表格数据
     */
    public void refreshData() {
        tableModel.setRowCount(0);

        if ("admin".equals(currentUser.getRole())) {
            for (Workload workload : DataUtil.getAllWorkloads()) {
                tableModel.addRow(new Object[]{
                        workload.getId(),
                        workload.getTeacher(),
                        workload.getWorkDate(),
                        workload.getHours(),
                        workload.getDescription(),
                        ""
                });
            }
        } else {
            for (Workload workload : DataUtil.getWorkloadsByTeacherName(currentUser.getUsername())) {
                tableModel.addRow(new Object[]{
                        workload.getId(),
                        workload.getTeacher(),
                        workload.getWorkDate(),
                        workload.getHours(),
                        workload.getDescription(),
                        ""
                });
            }
        }
    }


    //  添加工作量
    private void addWorkload() {
        WorkloadDialog dialog = new WorkloadDialog(this,null,currentUser);
        dialog.setVisible(true);
    }

    // 修改工作量
    public void editWorkload(int selectedRow) {
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(this, "请先选择要编辑的记录");
            return;
        }

        String workloadId = (String) tableModel.getValueAt(selectedRow,0);
        Workload workload = DataUtil.getWorkloadById(workloadId);

        WorkloadDialog dialog = new WorkloadDialog(this,workload,currentUser);
        dialog.setVisible(true);
    }

    // 删除工作量
    public void deleteWorkload(int selectedRow) {
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(this, "请先选择要删除的记录");
            return;
        }

        String workloadId = (String) tableModel.getValueAt(selectedRow,0);
        DataUtil.deleteWorkload(workloadId);
        refreshData();
        JOptionPane.showMessageDialog(this, "删除成功");
    }

    // 修改密码
    private void showChangePasswordDialog() {
        // 创建弹窗面板
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JPasswordField oldPasswordField = new JPasswordField(15);
        JPasswordField newPasswordField = new JPasswordField(15);
        JPasswordField confirmPasswordField = new JPasswordField(15);

        panel.add(new JLabel("旧密码："));
        panel.add(oldPasswordField);
        panel.add(new JLabel("新密码："));
        panel.add(newPasswordField);
        panel.add(new JLabel("确认新密码："));
        panel.add(confirmPasswordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "修改密码", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            // 校验输入
            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "所有字段不能为空！");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "两次输入的新密码不一致！");
                return;
            }

            if (!oldPassword.equals(currentUser.getPassword())) {
                JOptionPane.showMessageDialog(this, "旧密码错误！");
                return;
            }

            // 更新密码
            DataUtil.updatePassword(currentUser.getId(), newPassword);
            JOptionPane.showMessageDialog(this, "密码修改成功！");
        }
    }

    private void showWorkloadSummary() {
        new WorkloadSummaryDialog(this);
    }

}