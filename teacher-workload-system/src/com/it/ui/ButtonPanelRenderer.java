package com.it.ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 *自定义按钮面板渲染器
 */

// 创建ButtonPanelRenderer类, 用于显示和编辑按钮面板

// TableCellRenderer 和 AbstractCellEditor 是用于控制表格单元格显示和编辑行为的核心接口/类。
// TableCellRenderer 控制单元格显示样式，TableCellEditor 控制单元格编辑行为。

// 创建ButtonPanelRenderer类 实现TableCellRenderer接口
// 用于在表格的单元格内显示操作按钮
class ButtonPanelRenderer implements TableCellRenderer {
    private JPanel panel; // 面板
    private JButton editBtn;// 修改按钮
    private JButton deleteBtn;//  删除按钮

    /** 构造方法 */
    public ButtonPanelRenderer() {
        //  1. 创建面板
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        // 2. 创建编辑和删除按钮
        editBtn = new JButton("修改");
        deleteBtn = new JButton("删除");
        // 3. 设置按钮样式
        editBtn.setMargin(new Insets(0, 5, 0, 5));
        deleteBtn.setMargin(new Insets(0, 5, 0, 5));
        // 4. 将按钮添加到面板中
        panel.add(editBtn);
        panel.add(deleteBtn);
    }

    /**
     * 返回一个包含两个按钮的面板
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return panel;
    }
}

// 创建ButtonPanelEditor类 实现TableCellEditor接口
// 用于在表格单元格中编辑按钮面板
class ButtonPanelEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private JButton editBtn;
    private JButton deleteBtn;
    private int editedRow; // 记录当前正在编辑的行号,-1表示没有行正在被编辑
    
    public ButtonPanelEditor(JTable table, MainFrame frame) {
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        editBtn = new JButton("修改");
        deleteBtn = new JButton("删除");
        
        // 设置按钮样式
        editBtn.setMargin(new Insets(0, 5, 0, 5));
        deleteBtn.setMargin(new Insets(0, 5, 0, 5));
        
        // 修改按钮事件
        editBtn.addActionListener(e -> {
            fireEditingStopped();
            frame.editWorkload(editedRow);
        });
        
        // 删除按钮事件
        deleteBtn.addActionListener(e -> {
            fireEditingStopped();
            int confirm = JOptionPane.showConfirmDialog(table, "确定要删除吗？", "确认删除",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.deleteWorkload(editedRow);
            }
        });
        
        panel.add(editBtn);
        panel.add(deleteBtn);
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        editedRow = row;
        return panel;
    }
    
    @Override
    public Object getCellEditorValue() {
        return ""; // 返回值不重要，因为我们直接处理按钮事件
    }


}