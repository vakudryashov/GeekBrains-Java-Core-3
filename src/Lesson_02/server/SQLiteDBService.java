package Lesson_02.server;

import java.sql.*;

public class SQLiteDBService {
    private static Connection conn = null;
    public Statement stmt;

    public SQLiteDBService() {
        if (conn == null){
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:chat.db");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet read(String sql){
        ResultSet result = null;
        try {
            result = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean write(String sql){
        boolean result = true;
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    public void close(){
        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
