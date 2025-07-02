package com.it.ui;

import com.it.pojo.Workload;
import com.it.util.DataUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class WorkloadCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JTextField editorComponent;
    private int column;
    private Workload workload;

    public WorkloadCellEditor(int column) {
        this.column = column;
        this.editorComponent = new JTextField();
        editorComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    stopCellEditing();
                }
            }
        });
    }

    @Override
    public Object getCellEditorValue() {
        String value = editorComponent.getText();

        // 根据列更新对应字段
        switch (column) {
            case 1: // 教师姓名
                workload.setTeacher(value);
                break;
            case 2: // 工作日期
                workload.setWorkDate(value);
                break;
            case 3: // 工作小时数
                try {
                    float hours = Float.parseFloat(value);
                    workload.setHours(hours);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "请输入有效的数字！");
                    workload.setHours(0);
                }
                break;
            case 4: // 描述
                workload.setDescription(value);
                break;
        }

        DataUtil.saveWorkload(workload); // 保存修改
        return value;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        // 获取当前行的数据
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        String id = (String) model.getValueAt(row, 0);
        workload = DataUtil.getWorkloadById(id);

        // 设置输入框值
        switch (column) {
            case 1:
                editorComponent.setText(workload.getTeacher());
                break;
            case 2:
                editorComponent.setText(workload.getWorkDate());
                break;
            case 3:
                editorComponent.setText(String.valueOf(workload.getHours()));
                break;
            case 4:
                editorComponent.setText(workload.getDescription());
                break;
        }

        return editorComponent;
    }
}
