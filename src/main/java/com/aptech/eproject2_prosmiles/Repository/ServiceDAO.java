package com.aptech.eproject2_prosmiles.Repository;

import com.aptech.eproject2_prosmiles.Conectivity.MySQLConnection;
import com.aptech.eproject2_prosmiles.IGeneric.DentalRepository;
import com.aptech.eproject2_prosmiles.Model.Entity.Service;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class ServiceDAO implements DentalRepository<Service> {
    private final Connection conn = MySQLConnection.getConnection();
    private ObservableList<Service> services = FXCollections.observableArrayList();
    @Override
    public ObservableList<Service> getAll() {
        ObservableList<Service> serviceList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT " +
                    "sv.id, sv.p_id, sv.name, sv.description" +
                    ", sv.image_path" +
                    ", sv.created_at, sv.deleted_at, sv.is_deleted" +
                    " FROM service sv WHERE 1 = 1 LIMIT 100";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                serviceList.add(setPropertiesService(rs));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        services.clear();
        services.addAll(serviceList);
        return services;
    }

    @Override
    public Service getById(int id) {
        try{
            String sql = "SELECT " +
                    "sv.id, sv.p_id, sv.name, sv.description" +
                    ", sv.image_path" +
                    ", sv.created_at, sv.deleted_at, sv.is_deleted" +
                    "  FROM service sv" +
                    " WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return setPropertiesService(rs);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public ObservableList<Service> findByName(String name) {
        String[] keywords = name.split("");
        return FXCollections.observableArrayList(
                services.stream()
                        .filter(service -> {
                            for (String keyword : keywords) {
                                if (service.getName().toLowerCase().contains(keyword)) {
                                    return true;
                                }
                            }
                            return false;
                        }).toList()
        );
    }

    @Override
    public Service save(Service entity) {
        try{
            String sql = "INSERT INTO service (p_id, name, description" +
                    ", image_path, created_at, is_deleted) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, entity.getpId());
            pstmt.setString(2, entity.getName());
            pstmt.setString(3, entity.getDescription());
            pstmt.setString(4, entity.getImagePath());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(5, Timestamp.valueOf(now));
            pstmt.setString(6, entity.getIsDeleted().toString());
            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Override
    public Service update(Service entity) {
        try{
            String sql = "UPDATE service sv SET sv.p_id = ?, sv.name = ?" +
                    ", sv.description = ?, sv.image_path = ?" +
                    ", is_deleted = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, entity.getpId());
            pstmt.setString(2, entity.getName());
            pstmt.setString(3, entity.getDescription());
            pstmt.setString(4, entity.getImagePath());
            pstmt.setString(5, entity.getIsDeleted().toString());
            pstmt.setInt(6, entity.getId());
            pstmt.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Override
    public boolean delete(Service entity) {
        try{
            String sql = "UPDATE service sv SET sv.is_deleted = ? WHERE sv.id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, entity.getIsDeleted().getValue());
            pstmt.setInt(2, entity.getId());
            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return false;
    }

    private Service setPropertiesService(ResultSet rs) throws SQLException {
        Service service = new Service();
        service.setId(rs.getInt("id"));
        service.setpId(rs.getInt("p_id"));
        service.setName(rs.getString("name"));
        service.setDescription(rs.getString("description"));
        service.setImagePath(rs.getString("image_path"));

        Timestamp timestamp = rs.getTimestamp("created_at");
        LocalDateTime create_at = timestamp == null ? null : timestamp.toLocalDateTime();
        service.setCreatedAt(create_at);

        Timestamp timestamp2 = rs.getTimestamp("deleted_at");
        LocalDateTime deleteAt = timestamp2 == null ? null : timestamp2.toLocalDateTime();
        service.setUpdatedAt(deleteAt);

        service.setIsDeleted(EIsDeleted.fromInt(rs.getInt("is_deleted")));

        return service;
    }
}
