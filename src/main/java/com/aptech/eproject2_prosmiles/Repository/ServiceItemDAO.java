package com.aptech.eproject2_prosmiles.Repository;

import com.aptech.eproject2_prosmiles.Conectivity.MySQLConnection;
import com.aptech.eproject2_prosmiles.IGeneric.DentalRepository;
import com.aptech.eproject2_prosmiles.Model.Entity.Service;
import com.aptech.eproject2_prosmiles.Model.Entity.ServiceItem;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ServiceItemDAO implements DentalRepository<ServiceItem> {
    public static Connection conn = MySQLConnection.getConnection();
    public static ObservableList<ServiceItem> serviceItems = FXCollections.observableArrayList();

    public static List<Service> serviceList ;
    @Override
    public ObservableList<ServiceItem> getAll() {
        ObservableList<ServiceItem> serviceItemsList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT si.id, si.service_id, si.name, si.price, si.unit, si.quantity, si.description" +
                    ",si.dosage, si.usage_instruction, si.created_at, si.updated_at, si.is_deleted FROM service_item si " +
                    "WHERE 1=1 LIMIT 100";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                serviceItemsList.add(setPropertiesServiceItem(rs));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        serviceItems.clear();
        serviceItems.addAll(serviceItemsList);
        return serviceItems;
    }

    @Override
    public ServiceItem getById(int id) {
        try{
            String sql = "SELECT si.id, si.service_id, si.name, si.price, si.unit, si.quantity, si.description" +
                    ",si.dosage, si.usage_instruction, si.created_at, si.updated_at, si.is_deleted " +
                    "FROM service_item si " +
                    "WHERE si.id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return setPropertiesServiceItem(rs);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public ObservableList<ServiceItem> findByName(String name) {
        String[] keywords = name.split("");
        return FXCollections.observableArrayList(
                serviceItems.stream()
                        .filter(serviceItem -> {
                            for (String keyword : keywords) {
                                if (serviceItem.getName().toLowerCase().contains(keyword)){
                                    return true;
                                }
                            }
                            return false;
                        }).toList()
        );
    }

    @Override
    public ServiceItem save(ServiceItem entity) {

        try{
            String sql = "INSERT INTO service_item (service_id, name, price, unit, quantity, description, dosage, usage_instruction, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, entity.getService().get().getId());
            pstmt.setString(2, entity.getName());
            pstmt.setDouble(3, entity.getPrice());
            pstmt.setString(4, entity.getUnit());
            pstmt.setInt(5, entity.getQuantity());
            pstmt.setString(6, entity.getDescription());
            pstmt.setString(7, entity.getDosage());
            pstmt.setString(8, entity.getUsageInstruction());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(9, Timestamp.valueOf(now));
            pstmt.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Override
    public ServiceItem update(ServiceItem entity) {
        try{
            String sql = "UPDATE service_item si " +
                    "SET si.service_id = ?, si.name = ?, si.price = ?, si.unit = ?, si.quantity = ?" +
                    ", si.description = ?, si.dosage = ?, si.usage_instruction = ?, si.update_at = ? " +
                    "WHERE si.id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, entity.getService().get().getId());
            pstmt.setString(2, entity.getName());
            pstmt.setDouble(3, entity.getPrice());
            pstmt.setString(4, entity.getUnit());
            pstmt.setInt(5, entity.getQuantity());
            pstmt.setString(6, entity.getDescription());
            pstmt.setString(7, entity.getDosage());
            pstmt.setString(8, entity.getUsageInstruction());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(9, Timestamp.valueOf(now));
            pstmt.setInt(10, entity.getId());
            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Override
    public boolean delete(ServiceItem entity) {
        try{
            String sql = "UPDATE service_item si SET si.is_deleted=? WHERE si.id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, entity.getIsDeleted().getValue());
            pstmt.setInt(2, entity.getId());
            pstmt.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return false;
    }

    private ServiceItem setPropertiesServiceItem(ResultSet rs) throws SQLException {
        Service service = new Service();
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setId(rs.getInt("id"));
        service.setId(rs.getInt("service_id"));
        serviceItem.setService(Optional.of(service));
        serviceItem.setName(rs.getString("name"));
        serviceItem.setPrice(rs.getInt("price"));
        serviceItem.setUnit(rs.getString("unit"));
        serviceItem.setQuantity(rs.getInt("quantity"));
        serviceItem.setDescription(rs.getString("description"));
        serviceItem.setDosage(rs.getString("dosage"));
        serviceItem.setUsageInstruction(rs.getString("usage_instruction"));

        Timestamp timestamp = rs.getTimestamp("created_at");
        LocalDateTime create_at = timestamp == null ? null : timestamp.toLocalDateTime();
        serviceItem.setCreatedAt(create_at);

        Timestamp timestamp2 = rs.getTimestamp("updated_at");
        LocalDateTime updateAt = timestamp2 == null ? null : timestamp2.toLocalDateTime();
        serviceItem.setUpdatedAt(updateAt);

        serviceItem.setIsDeleted(EIsDeleted.fromInt(rs.getInt("is_deleted")));
        return serviceItem;
    }
}
