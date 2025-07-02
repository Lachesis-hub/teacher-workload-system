
package com.it.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.it.pojo.User;
import com.it.pojo.Workload;
import com.it.pojo.WorkloadSummary;
import java.util.List;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.util.ArrayList;
import java.util.List;

public class DataUtil {
    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    public static User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 保存用户
     *
     * @param user
     */

    public static void saveUser(User user) {
        String sql = "INSERT INTO users(id, username, password, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将账户ArrayList<User>---->ArrayList<String>
     *
     * @return
     */


    //获取所有的用户列表
    public static ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * 获取所有的老师列表
     * @return
     */
    /**
     * 获取所有的老师列表（角色为 teacher 的用户）
     */
    public static ArrayList<User> getAllTeachers() {
        ArrayList<User> teachers = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'teacher'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                teachers.add(new User(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teachers;
    }


    /**
     * 将ArrayList<String>---->ArrayList<User>
     *
     * @param usersStrs
     * @return
     */
    /**
     * 保存工作量到文件
     *
     * @param workload
     */
    public static void saveWorkload(Workload workload) {
        String sql = "INSERT INTO workloads(id, teacher, work_date, hours, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection()) {
        // 如果 workload 的 id 为空，则生成 UUID
        if (workload.getId() == null || workload.getId().isEmpty()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            workload.setId(IdUtil.simpleUUID());
            stmt.setString(1, workload.getId());
            stmt.setString(2, workload.getTeacher());
            stmt.setString(3, workload.getWorkDate());
            stmt.setFloat(4, workload.getHours());
            stmt.setString(5, workload.getDescription());
            stmt.executeUpdate();
        }else{
            // id不为空则更新现有记录
            sql = "update workloads set teacher = ? , work_date = ?  , hours = ?, description = ? where id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, workload.getTeacher());
            stmt.setString(2, workload.getWorkDate());
            stmt.setFloat(3, workload.getHours());
            stmt.setString(4, workload.getDescription());
            stmt.setString(5, workload.getId());
            stmt.executeUpdate();
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //通过用户名获取工作量列表
    public static ArrayList<Workload> getWorkloadsByTeacherName(String teacherName) {
        ArrayList<Workload> workloads = new ArrayList<>();
        String sql = "SELECT * FROM workloads WHERE teacher = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, teacherName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                workloads.add(new Workload(
                        rs.getString("id"),
                        rs.getString("teacher"),
                        rs.getString("work_date"),
                        rs.getFloat("hours"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workloads;
    }


    //获取所有的工作量列表
    public static ArrayList<Workload> getAllWorkloads() {
        ArrayList<Workload> workloads = new ArrayList<>();
        String sql = "SELECT * FROM workloads";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                workloads.add(new Workload(
                        rs.getString("id"),
                        rs.getString("teacher"),
                        rs.getString("work_date"),
                        rs.getFloat("hours"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workloads;
    }


    //通过workloadId删除工作量
    public static void deleteWorkload(String workloadId) {
        String sql = "DELETE FROM workloads WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, workloadId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //通过workid获取工作量详情
    public static Workload getWorkloadById(String workloadId) {
        String sql = "SELECT * FROM workloads WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, workloadId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Workload(
                        rs.getString("id"),
                        rs.getString("teacher"),
                        rs.getString("work_date"),
                        rs.getFloat("hours"),
                        rs.getString("description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    //修改工作量
    public static void editWorkload(Workload workload)                            {
        String sql = "UPDATE workloads SET teacher = ?, work_date = ?, hours = ?, description = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, workload.getTeacher());
            stmt.setString(2, workload.getWorkDate());
            stmt.setFloat(3, workload.getHours());
            stmt.setString(4, workload.getDescription());
            stmt.setString(5, workload.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // 搜索功能 根据关键词模糊查询工作量（教师名、日期、描述）

    public static ArrayList<Workload> searchWorkloads(String keyword) {
        ArrayList<Workload> workloads = new ArrayList<>();
        String sql = "SELECT * FROM workloads WHERE teacher LIKE ? OR work_date LIKE ? OR description LIKE ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                workloads.add(new Workload(
                        rs.getString("id"),
                        rs.getString("teacher"),
                        rs.getString("work_date"),
                        rs.getFloat("hours"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workloads;
    }

    /**
     * 修改用户密码
     *
     * @param userId   用户ID
     * @param password 新密码
     */
    public static void updatePassword(String userId, String password) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, password);
            stmt.setString(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取每月每位教师的总工时
     *
     * @return 工时统计列表
     */
    public static List<WorkloadSummary> getMonthlyWorkloadSummaries() {
        List<WorkloadSummary> summaries = new ArrayList<>();
        String sql = "SELECT teacher, SUBSTRING(work_date, 1, 7) AS month, SUM(hours) AS total_hours FROM workloads GROUP BY teacher, month";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                summaries.add(new WorkloadSummary(
                        rs.getString("teacher"),
                        rs.getString("month"),
                        rs.getFloat("total_hours")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return summaries;
    }


}




