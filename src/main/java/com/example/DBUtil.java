package com.example;
import com.example.User;
import java.sql.*;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/FORUM_BACKEND?useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root123";
    private static Connection conn = null;
    private DBUtil() { 
     try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("数据库连接成功！");
        } catch (Exception e) {
            System.err.println("数据库连接失败: " + e.getMessage());
            throw new RuntimeException("数据库初始化失败", e);
        }
    }
    public static void registUser(User NewUser) throws SQLException
    {
            if (conn == null) {
        System.out.println("数据库连接未建立！");
        return;
    }
    
    String sql = "INSERT INTO users (username, email, password, status) VALUES (?, ?, ?, 1)";
    
    try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        // 设置参数
        pstmt.setString(1, NewUser.getUsername());
        pstmt.setString(2, NewUser.getEmail());
        pstmt.setString(3, NewUser.getPassword()); // 这里已经是SHA256加密后的密码
        
        // 执行插入
        int affectedRows = pstmt.executeUpdate();
        
        if (affectedRows > 0) {
            // 获取自增的uid
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedUid = rs.getInt(1);
                    NewUser.setUid(generatedUid);
                    System.out.println("用户注册成功！UID: " + generatedUid);
                }
            }
        } else {
            System.out.println("用户注册失败！");
        }
        
    } catch (SQLException e) {
        // 处理可能的异常（如用户名或邮箱重复）
        if (e.getErrorCode() == 1062) { // MySQL重复键错误码
            String errorMessage = e.getMessage();
            if (errorMessage.contains("username")) {
                System.out.println("注册失败：用户名已存在！");
            } else if (errorMessage.contains("email")) {
                System.out.println("注册失败：邮箱已被注册！");
            } else {
                System.out.println("注册失败：用户信息冲突！");
            }
        } else {
            e.printStackTrace();
        }
    }
    }
        private static class Holder {
        private static final DBUtil instance = new DBUtil();
    }
    
    // 获取单例实例
    public static DBUtil getInstance() {
        return Holder.instance;
    }
    
    // 获取数据库连接
    public Connection getConnection() {
        return conn;
    }
    
    // 关闭资源的方法
    public void close(Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
