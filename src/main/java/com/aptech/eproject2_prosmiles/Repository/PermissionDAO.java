package com.aptech.eproject2_prosmiles.Repository;

import com.aptech.eproject2_prosmiles.Conectivity.MySQLConnection;
import com.aptech.eproject2_prosmiles.IGeneric.DentalRepository;
import com.aptech.eproject2_prosmiles.Model.Entity.Permission;
import com.aptech.eproject2_prosmiles.Model.Entity.Role;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class PermissionDAO implements DentalRepository<Permission> {
    private final Connection conn = MySQLConnection.getConnection();
    private ObservableList<Permission> permissions = FXCollections.observableArrayList();

    /*GET ALL*/
    @Override
    public ObservableList<Permission> getAll() {
        ObservableList<Permission> listPermission = FXCollections.observableArrayList();
        try {
            String sql = "SELECT p.id, p.title, p.slug, p.description, p.is_deleted, p.create_at, p.update_at" +
                    " FROM permission p WHERE 1=1 LIMIT 100";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
//                add to List
                listPermission.add(setPropertiesPermission(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        reset values
        permissions.clear();
        permissions.addAll(listPermission);
        return permissions;
    }

    /*GET BY ID*/
    @Override
    public Permission getById(int id) {
        try {
            String sql = "SELECT p.id, p.title, p.slug, p.description, p.is_deleted, p.create_at, p.update_at" +
                    " FROM permission p WHERE p.id = ? LIMIT 100";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return setPropertiesPermission(rs);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    /*GET BY PERMISSION'S NAME*/
    @Override
    public ObservableList<Permission> findByName(String name) {
        String[] keywords = name.split("");
        return FXCollections.observableArrayList(
                permissions.stream()
                        .filter(permission -> {
                            for (String keyword : keywords) {
                                if (permission.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                                    return true;
                                }
                            }
                            return false;
                        })
                        .toList()
        );
    }

    /*ADD*/
    @Override
    public Permission save(Permission entity) {
        try {
            String sql = "INSERT INTO permission (title, slug, description, create_at) " +
                    " VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, entity.getTitle());
            pstmt.setString(2, entity.getSlug());
            pstmt.setString(3, entity.getDescription());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(4, Timestamp.valueOf(now));
            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return entity;
    }

    /*EDIT*/
    @Override
    public Permission update(Permission entity) {
        try {
            String sql = "UPDATE permission p " +
                    "SET p.title = ?, p.slug = ?, p.description = ?, p.update_at = ? " +
                    "WHERE id = ?";
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
    public boolean delete(Permission entity) {
        boolean result = false;
        try {
            String sql = "UPDATE permission p " +
                    "SET P.is_deleted = ?, p.update_at = ? " +
                    "WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, entity.getIsDeleted().getValue());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(2, Timestamp.valueOf(now));
            pstmt.setInt(3, entity.getId());
            pstmt.executeUpdate();
            result = true;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return result;
    }

    /*SET PROPERTIES PERMISSION*/
    private static Permission setPropertiesPermission(ResultSet rs) throws SQLException {
        Permission permission = new Permission();
        permission.setId(rs.getInt("id"));
        permission.setTitle(rs.getString("title"));
        permission.setSlug(rs.getString("slug"));
        permission.setDescription(rs.getString("description"));
        permission.setIsDeleted(EIsDeleted.valueOf(rs.getString("is_deleted")));
//                convert Datetime
        Timestamp timestamp = rs.getTimestamp("create_at");
        LocalDateTime createAt = timestamp == null ? null : timestamp.toLocalDateTime();
        permission.setCreatedAt(createAt);
        Timestamp timestamp2 = rs.getTimestamp("update_at");
        LocalDateTime updateAt = timestamp2 == null ? null : timestamp2.toLocalDateTime();
        permission.setUpdatedAt(updateAt);
        return permission;
    }

    /*GET PERMISSION BY ROLE*/
    private ObservableList<Permission> getPermissionByRole(Role role) {
        ObservableList<Permission> permissionListByRole = FXCollections.observableArrayList();
        try {
            String sql = "SELECT p.id, p.title, p.slug, p.description, p.create_at, p.update_at " +
                    "FROM permission p " +
                    "INNER JOIN role_permission rp ON p.id = rp.permission_id " +
                    "INNER JOIN role r ON r.id = rp.role_id " +
                    "WHERE r.id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, role.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                permissionListByRole.add(setPropertiesPermission(rs));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return permissionListByRole;
    }
}
