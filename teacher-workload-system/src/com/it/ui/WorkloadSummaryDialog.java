package com.it.ui;

import com.it.pojo.WorkloadSummary;
import com.it.util.DataUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class WorkloadSummaryDialog extends JDialog {

    public WorkloadSummaryDialog(JFrame parent) {
        super(parent, "教师工时统计", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);

        // 表格模型
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"教师姓名", "月份", "总工时"}, 0
        );
        JTable table = new JTable(tableModel);

        // 加载数据
        List<WorkloadSummary> summaries = DataUtil.getMonthlyWorkloadSummaries();
        for (WorkloadSummary summary : summaries) {
            tableModel.addRow(new Object[]{
                    summary.getTeacher(),
                    summary.getMonth(),
                    summary.getTotalHours()
            });
        }

        // 添加滚动面板
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 关闭按钮
        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
