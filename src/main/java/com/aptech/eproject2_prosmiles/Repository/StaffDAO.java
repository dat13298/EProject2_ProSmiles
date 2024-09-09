package com.aptech.eproject2_prosmiles.Repository;

import com.aptech.eproject2_prosmiles.Conectivity.MySQLConnection;
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
        try {
            String sql = "SELECT " +
                    "s.id, s.password " +
                    "FROM staff s " +
                    "WHERE s.phone = ? OR s.email = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, staff.getPhone());
            pstmt.setString(2, staff.getEmail());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                staff.setId(rs.getInt("id"));
                staff.setPassword(rs.getString("password"));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return staff;
    }

    /*GET ALL*/
    @Override
    public ObservableList<Staff> getAll() {
        ObservableList<Staff> staffList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT " +
                    "s.id, s.role_id, s.first_name, s.last_name, " +
                    "s.gender, s.phone, s.password, s.address, s.email, " +
                    "s.age, s.image_path, s.created_at, s.updated_at, s.is_deleted " +
                    "FROM staff s " +
                    "WHERE 1=1 LIMIT 100";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                staffList.add(setPropertiesStaff(rs));
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        staffs.clear();
        staffs.addAll(staffList);
        return staffs;
    }

    /*GET BY ID*/
    @Override
    public Staff getById(int id) {
        try {
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Override
    public Staff update(Staff entity) {
        try {
            String sql = "UPDATE staff s " +
                    "SET s.role_id = ? " +
                    "s.first_name = ? " +
                    "s.last_name = ? " +
                    "s.gender = ? " +
                    "s.phone = ? " +
                    "s.password = ? " +
                    "s.address = ? " +
                    "s.email = ? " +
                    "s.age = ? " +
                    "s.image_path = ? " +
                    "s.update_at = ? " +
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
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean delete(Staff entity) {
        try {
            String sql = "UPDATE staff s " +
                    "SET s.is_deleted = ? " +
                    "WHERE s.id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, entity.getIsDeleted().getValue());
            pstmt.setInt(2, entity.getId());
            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return false;
    }

    /*SET PROPERTIES STAFF*/
    private Staff setPropertiesStaff(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setId(rs.getInt("id"));
        int role_id = rs.getInt("role_id");// Retrieve the role ID from the ResultSet
        Role role = new Role();
        role.setId(role_id);// Set the ID of the Role object
        staff.setRole(Optional.of(role));// Wrap the Role in an Optional and set it to the Staff object
        staff.setFirstName(rs.getString("first_name"));
        staff.setLastName(rs.getString("last_name"));
        staff.setEGender(EGender.valueOf(rs.getString("gender")));
        staff.setPhone(rs.getString("phone"));
        staff.setPassword(rs.getString("password"));
        staff.setAddress(rs.getString("address"));
        staff.setEmail(rs.getString("email"));
        staff.setAge(rs.getInt("age"));
        staff.setImagePath(rs.getString("image_path"));
        staff.setIsDeleted(EIsDeleted.valueOf(rs.getString("is_deleted")));
//        convert datetime
        Timestamp timestamp = rs.getTimestamp("create_at");
        LocalDateTime create_at = timestamp == null ? null : timestamp.toLocalDateTime();
        staff.setCreatedAt(create_at);
        Timestamp timestamp2 = rs.getTimestamp("update_at");
        LocalDateTime updateAt = timestamp2 == null ? null : timestamp2.toLocalDateTime();
        staff.setUpdatedAt(updateAt);
        return staff;
    }
}
