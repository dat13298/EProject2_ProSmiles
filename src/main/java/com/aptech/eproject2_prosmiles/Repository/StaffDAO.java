package com.aptech.eproject2_prosmiles.Repository;

import com.aptech.eproject2_prosmiles.Conectivity.MySQLConnection;
import com.aptech.eproject2_prosmiles.Global.AppProperties;
import com.aptech.eproject2_prosmiles.Global.DialogHelper;
import com.aptech.eproject2_prosmiles.IGeneric.DentalRepository;
import com.aptech.eproject2_prosmiles.Model.Entity.Role;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Model.Enum.EGender;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class StaffDAO implements DentalRepository<Staff> {
    private static final Connection conn = MySQLConnection.getConnection();
    private ObservableList<Staff> staffs = FXCollections.observableArrayList();

    /*GET STAFF BY PHONE OR EMAIL*/
    public static Staff getStaffByPhoneOrEmail(Staff staff) {
        Staff foundStaff = null;
        try {
            if (conn == null) {
                throw new SQLException("Failed to connect Database");
            }

            String sql = "";
            if (staff.getPhone() != null) {
                sql = "SELECT s.id, s.password, s.first_name, s.last_name, s.email, s.phone, s.otp " +
                        "FROM staff s WHERE s.phone = ?";
            }
            if (staff.getEmail() != null) {
                sql = "SELECT s.id, s.password, s.first_name, s.last_name, s.email, s.phone, s.otp " +
                        "FROM staff s WHERE s.email = ?";
            }
            PreparedStatement pstmt = conn.prepareStatement(sql);
            if (staff.getPhone() != null) {
                pstmt.setString(1, staff.getPhone());
            }
            if (staff.getEmail() != null) {
                pstmt.setString(1, staff.getEmail());
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                foundStaff = new Staff();
                foundStaff.setId(rs.getInt("id"));
                foundStaff.setPassword(rs.getString("password"));
                foundStaff.setEmail(rs.getString("email"));
                foundStaff.setPhone(rs.getString("phone"));
                foundStaff.setFirstName(rs.getString("first_name"));
                foundStaff.setLastName(rs.getString("last_name"));
                foundStaff.setOtp(rs.getString("otp"));
            }
        } catch (SQLException e) {
            DialogHelper.showNotificationDialog("Error", "Server connection failed: " + e.getMessage());
        }

        return foundStaff;
    }

    /*GET ALL*/
    @Override
    public ObservableList<Staff> getAll() {
        ObservableList<Staff> staffList = FXCollections.observableArrayList();
        try {
            if (conn == null) {
                throw new SQLException("Failed to connect to database");
            }
            String sql = "SELECT " +
                    "s.id, s.role_id, s.first_name, s.last_name, " +
                    "s.gender, s.phone, s.password, s.address, s.email, " +
                    "s.age, s.image_path, s.created_at, s.updated_at, s.is_deleted " +
                    "FROM staff s " +
                    "WHERE s.is_deleted = 0 LIMIT 100";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                staffList.add(setPropertiesStaff(rs));
            }

        }catch (SQLException e){
            DialogHelper.showNotificationDialog("Error", "Server connection failed");
        }
        staffs.clear();
        staffs.addAll(staffList);
        return staffs;
    }

    /*GET BY ID*/
    @Override
    public Staff getById(int id) {
        try {
            if (conn == null) {
                throw new SQLException("Failed to connect to database");
            }
            String sql = "SELECT " +
                    "s.id, s.role_id, s.first_name, s.last_name, " +
                    "s.gender, s.phone, s.password, s.address, s.email, " +
                    "s.age, s.image_path, s.created_at, s.updated_at, s.is_deleted " +
                    "FROM staff s " +
                    "WHERE s.id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return setPropertiesStaff(rs);
            }
        }catch (SQLException e){
            DialogHelper.showNotificationDialog("Error", "Server connection failed");
        }
        return null;
    }

    /*GET BY STAFF'S NAME*/
    @Override
    public ObservableList<Staff> findByName(String name) {
        String[] keywords = name.split("");
        return FXCollections.observableArrayList(
                staffs.stream()
                        .filter(staff -> {
                            for (String keyword : keywords) {
                                if (staff.getFirstName().toLowerCase().contains(keyword)
                                        || staff.getLastName().toLowerCase().contains(keyword)) {
                                    return true;
                                }
                            }
                            return false;
                        }).toList()
        );
    }

    /*ADD*/
    @Override
    public Staff save(Staff entity) {
        try {
            if (conn == null) {
                throw new SQLException("Failed to connect to database");
            }
            String sql = "INSERT INTO staff (role_id, first_name, last_name, gender, phone, password, address, email, " +
                    "age, image_path, created_at) " +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, entity.getRole().getId());
            pstmt.setString(2, entity.getFirstName());
            pstmt.setString(3, entity.getLastName());
            pstmt.setString(4, entity.getEGender().getGender());
            pstmt.setString(5, entity.getPhone());
            pstmt.setString(6, entity.getPassword());
            pstmt.setString(7, entity.getAddress());
            pstmt.setString(8, entity.getEmail());
            pstmt.setInt(9, entity.getAge());
            pstmt.setString(10, entity.getImagePath());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(11, Timestamp.valueOf(now));
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            DialogHelper.showConfirmationDialog("Error", "Failed to create staff");
        }
        return entity;
    }

    @Override
    public Staff update(Staff entity) {
        try {
            if (conn == null) {
                throw new SQLException("Failed to connect to database");
            }
            String sql = "UPDATE staff s " +
                    "SET s.role_id = ?, " +
                    "s.first_name = ?, " +
                    "s.last_name = ?, " +
                    "s.gender = ?, " +
                    "s.phone = ?, " +
                    "s.password = ?, " +
                    "s.address = ?, " +
                    "s.email = ?, " +
                    "s.age = ?, " +
                    "s.image_path = ?, " +
                    "s.updated_at = ? " +
                    "WHERE s.id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, entity.getRole().getId());
            pstmt.setString(2, entity.getFirstName());
            pstmt.setString(3, entity.getLastName());
            pstmt.setString(4, entity.getEGender().getGender());
            pstmt.setString(5, entity.getPhone());
            pstmt.setString(6, entity.getPassword());
            pstmt.setString(7, entity.getAddress());
            pstmt.setString(8, entity.getEmail());
            pstmt.setInt(9, entity.getAge());
            pstmt.setString(10, entity.getImagePath());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(11, Timestamp.valueOf(now));
            pstmt.setInt(12, entity.getId());
            pstmt.executeUpdate();
            DialogHelper.showNotificationDialog("Notification", "Updated staff");
        }catch (SQLException e){
            System.out.println(e.getMessage());
            DialogHelper.showNotificationDialog("Error", "Failed to update staff");
        }
        return null;
    }

    @Override
    public boolean delete(Staff entity) {
        try {
            if (conn == null) {
                throw new SQLException("Failed to connect to database");
            }
            String sql = "UPDATE staff s " +
                    "SET s.is_deleted = ? " +
                    "WHERE s.id = ? AND s.email <> ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, entity.getIsDeleted().getValue());
            pstmt.setInt(2, entity.getId());
            String email = AppProperties.getProperty("staff.email");
            pstmt.setString(3, email);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            } else {
                System.out.println("No staff found with the given ID or email is the same.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            DialogHelper.showNotificationDialog("Error", "Failed to delete staff");
            return false;
        }
    }


    /*SET PROPERTIES STAFF*/
    private Staff setPropertiesStaff(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setId(rs.getInt("id"));
        int role_id = rs.getInt("role_id");
        Role role = new Role();
        role.setId(role_id);
        staff.setRole(Optional.of(role));
        staff.setFirstName(rs.getString("first_name"));
        staff.setLastName(rs.getString("last_name"));
        staff.setEGender(EGender.fromValue(rs.getString("gender")));
        staff.setPhone(rs.getString("phone"));
        staff.setPassword(rs.getString("password"));
        staff.setAddress(rs.getString("address"));
        staff.setEmail(rs.getString("email"));
        staff.setAge(rs.getInt("age"));
        staff.setImagePath(rs.getString("image_path"));
        staff.setIsDeleted(EIsDeleted.fromInt(rs.getInt("is_deleted")));
//        convert datetime
        Timestamp timestamp = rs.getTimestamp("created_at");
        LocalDateTime created_at = timestamp == null ? null : timestamp.toLocalDateTime();
        staff.setCreatedAt(created_at);
        Timestamp timestamp2 = rs.getTimestamp("updated_at");
        LocalDateTime updatedAt = timestamp2 == null ? null : timestamp2.toLocalDateTime();
        staff.setUpdatedAt(updatedAt);
        return staff;
    }

    public void updateOtp(Staff staff) {
        try {
            if (conn == null) {
                throw new SQLException("Failed to connect to database");
            }
            String sql = "UPDATE staff SET otp = ? WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, staff.getOtp());
            pstmt.setString(2, staff.getEmail());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            DialogHelper.showNotificationDialog("Error", "Failed to update OTP: " + e.getMessage());
        }
    }

    public void updatePasswordById(int id, String newPassword) {
        try {
            if (conn == null) {
                throw new SQLException("Failed to connect to database");
            }
            String sql = "UPDATE staff SET password = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            DialogHelper.showNotificationDialog("Success", "Password updated successfully.");
        } catch (SQLException e) {
            DialogHelper.showNotificationDialog("Error", "Failed to update password: " + e.getMessage());
        }
    }
}
