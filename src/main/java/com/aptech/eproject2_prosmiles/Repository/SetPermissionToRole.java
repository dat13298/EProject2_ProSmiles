package com.aptech.eproject2_prosmiles.Repository;

import com.aptech.eproject2_prosmiles.Conectivity.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetPermissionToRole {
    private static final Connection conn = MySQLConnection.getConnection();

    public static boolean setPermissionToRole(int role_id, int permission_id) {
        boolean result = false;
        try {
            String sql = "INSERT INTO role_permission (role_id, permission_id) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, role_id);
            pstmt.setInt(2, permission_id);
            pstmt.executeUpdate();
            result = true;
        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
        return result;
    }
}
