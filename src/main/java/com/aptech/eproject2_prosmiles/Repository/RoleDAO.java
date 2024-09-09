package com.aptech.eproject2_prosmiles.Repository;

import com.aptech.eproject2_prosmiles.Conectivity.MySQLConnection;
import com.aptech.eproject2_prosmiles.IGeneric.DentalRepository;
import com.aptech.eproject2_prosmiles.Model.Entity.Role;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class RoleDAO implements DentalRepository<Role> {
    private final Connection conn = MySQLConnection.getConnection();
    private ObservableList<Role> roles = FXCollections.observableArrayList();
    /*GET ALL*/
    @Override
    public ObservableList<Role> getAll() {
        ObservableList<Role> listRole = FXCollections.observableArrayList();
        try {
            String sql = "SELECT r.id, r.title, r.slug, r.description, r.is_deleted, r.created_at, r.updated_at " +
                    "FROM role r WHERE 1=1 LIMIT 100";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                listRole.add(setPropertiesRole(rs));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        roles.clear();
        roles.addAll(listRole);
        return roles;
    }

    /*GET BY ID*/
    @Override
    public Role getById(int id) {
        try {
            String sql = "SELECT r.id, r.title, r.slug, r.description, r.is_deleted, r.created_at, r.updated_at " +
                    "FROM role r WHERE r.id=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return setPropertiesRole(rs);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    /*GET BY NAME*/
    @Override
    public ObservableList<Role> findByName(String name) {
        String[] keywords = name.split("");
        return FXCollections.observableArrayList(
                roles.stream()
                        .filter(role -> {
                            for (String keyword : keywords) {
                                if (role.getTitle().toLowerCase().contains(keyword.toLowerCase())){
                                    return true;
                                }
                            }
                            return false;
                        }).toList()
        );
    }

    /*ADD*/
    @Override
    public Role save(Role entity) {
        try {
            String sql = "INSERT INTO role (title, slug, description, created_at) " +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, entity.getTitle());
            pstmt.setString(2, entity.getSlug());
            pstmt.setString(3, entity.getDescription());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(4, Timestamp.valueOf(now));
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return entity;
    }

    /*EDIT*/
    @Override
    public Role update(Role entity) {
        try {
            String sql = "UPDATE role r " +
                    "SET r.title = ?, r.slug = ?, r.description = ?, r.updated_at = ? " +
                    "WHERE r.id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, entity.getTitle());
            pstmt.setString(2, entity.getSlug());
            pstmt.setString(3, entity.getDescription());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(4, Timestamp.valueOf(now));
            pstmt.setInt(5, entity.getId());
            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return entity;
    }

    /*DELETE*/
    @Override
    public boolean delete(Role entity) {
        boolean result = false;
        try {
            String sql = "UPDATE role r " +
                    "SET r.updated_at = ? , r.is_deleted = ?" +
                    "WHERE r.id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(2, entity.getId());
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /*SET PROPERTIES ROLE*/
    private static Role setPropertiesRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setId(rs.getInt("id"));
        role.setTitle(rs.getString("title"));
        role.setSlug(rs.getString("slug"));
        role.setDescription(rs.getString("description"));
        role.setActive(EIsDeleted.ACTIVE);
//        convert Datetime
        Timestamp timestamp = rs.getTimestamp("created_at");
        LocalDateTime createdAt = timestamp != null ? timestamp.toLocalDateTime() : null;
        role.setCreatedAt(createdAt);
        if(rs.getTimestamp("updated_at") != null){
            Timestamp timestamp2 = rs.getTimestamp("updated_at");
            LocalDateTime updatedAt = timestamp2 != null ? timestamp2.toLocalDateTime() : null;
            role.setUpdatedAt(updatedAt);
        }
        return role;
    }
}
