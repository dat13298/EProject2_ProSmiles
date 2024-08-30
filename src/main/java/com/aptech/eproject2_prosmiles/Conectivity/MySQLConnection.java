package com.aptech.eproject2_prosmiles.Conectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    private static String url = "jdbc:mysql://localhost:3306/prosmiles";
    private static String user = "root";
    private static String password = "eProject2";
    public static Connection conn = null;

    public MySQLConnection(Connection conn) {
        MySQLConnection.conn = conn;
    }

    public static Connection getConnection() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    MySQLConnection.url,
                    MySQLConnection.user,
                    MySQLConnection.password
            );
        }catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static void closeConnection() {
        try{
            conn.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
