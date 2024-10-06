package com.aptech.eproject2_prosmiles.Repository;

import com.aptech.eproject2_prosmiles.Conectivity.MySQLConnection;
import com.aptech.eproject2_prosmiles.Global.Format;
import com.aptech.eproject2_prosmiles.IGeneric.DentalRepository;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Model.Entity.PrescriptionDetail;
import com.aptech.eproject2_prosmiles.Model.Entity.ServiceItem;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrescriptionDetailDAO implements DentalRepository<PrescriptionDetail> {
    public static Connection conn = MySQLConnection.getConnection();
    public static ObservableList<PrescriptionDetail> psd = FXCollections.observableArrayList();

    @Override
    public ObservableList<PrescriptionDetail> getAll() {
        String sql = "select pd.id, pd.service_item_id, pd.prescription_id,pd.unit," +
                "pd.quantity,pd.price,pd.created_at," +
                "pd.updated_at,pd.is_deleted " +
                "from prescription_detail pd where 1=1";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Prescription prescription = new Prescription();
                ServiceItem serviceItem = new ServiceItem();
                PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
                prescriptionDetail.setId(rs.getInt("id"));

                serviceItem.setId(rs.getInt("service_item_id"));
                prescriptionDetail.setServiceItem(Optional.of(serviceItem));

                prescription.setId(rs.getInt("prescription_id"));
                prescriptionDetail.setPrescription(Optional.of(prescription));

                prescriptionDetail.setUnit(rs.getString("unit"));
                prescriptionDetail.setQuantity(rs.getInt("quantity"));
                prescriptionDetail.setPrice(rs.getDouble("price"));

                Timestamp createTime = rs.getTimestamp("created_at");
                LocalDateTime createAt = createTime == null ? null : createTime.toLocalDateTime();
                prescriptionDetail.setCreatedAt(createAt);

                Timestamp updateTime = rs.getTimestamp("created_at");
                LocalDateTime updateAt = updateTime == null ? null : updateTime.toLocalDateTime();
                prescriptionDetail.setUpdatedAt(updateAt);

                prescriptionDetail.setIsDeleted(EIsDeleted.fromInt(rs.getInt("is_deleted")));
                psd.add(prescriptionDetail);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return psd;
    }

    @Override
    public PrescriptionDetail getById(int id) {
        String sql = "select pd.id, pd.service_item_id, pd.prescription_id,pd.unit," +
                "pd.quantity,pd.price,pd.created_at," +
                "pd.updated_at,pd.is_deleted from prescription_detail pd where pd.prescription_id = ?";
        PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Prescription prescription = new Prescription();
                ServiceItem serviceItem = new ServiceItem();

                prescriptionDetail.setId(rs.getInt("id"));

                serviceItem.setId(rs.getInt("service_item_id"));
                prescriptionDetail.setServiceItem(Optional.of(serviceItem));

                prescription.setId(rs.getInt("prescription_id"));
                prescriptionDetail.setPrescription(Optional.of(prescription));

                prescriptionDetail.setUnit(rs.getString("unit"));
                prescriptionDetail.setQuantity(rs.getInt("quantity"));
                prescriptionDetail.setPrice(rs.getDouble("price"));

                Timestamp createTime = rs.getTimestamp("created_at");
                LocalDateTime createAt = createTime == null ? null : createTime.toLocalDateTime();
                prescriptionDetail.setCreatedAt(createAt);

                Timestamp updateTime = rs.getTimestamp("created_at");
                LocalDateTime updateAt = updateTime == null ? null : updateTime.toLocalDateTime();
                prescriptionDetail.setUpdatedAt(updateAt);

                prescriptionDetail.setIsDeleted(EIsDeleted.fromInt(rs.getInt("is_deleted")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return prescriptionDetail;
    }

    @Override
    public ObservableList<PrescriptionDetail> findByName(String name) {
        return null;
    }

    @Override
    public PrescriptionDetail save(PrescriptionDetail entity) {
        String sql = "insert into prescription_detail values (?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getServiceItem().get().getId());
            ps.setInt(2, entity.getPrescription().get().getId());
            ps.setString(3, entity.getUnit());
            ps.setInt(4, entity.getQuantity());
            ps.setDouble(5, entity.getPrice());
            ps.setString(6, entity.getCreatedAt().toString());
            ps.setString(7, entity.getUpdatedAt().toString());
            ps.setString(8, entity.getIsDeleted().toString());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public PrescriptionDetail update(PrescriptionDetail entity) {
        String sql = "update prescription_detail set service_item_id=?,prescription_id=?,unit=?,quantity=?,price=?,created_at=? where id=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getServiceItem().get().getId());
            ps.setInt(2, entity.getPrescription().get().getId());
            ps.setString(3, entity.getUnit());
            ps.setInt(4, entity.getQuantity());
            ps.setDouble(5, entity.getPrice());
            ps.setString(6, entity.getCreatedAt().toString());
            ps.setInt(7, entity.getId());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public boolean delete(PrescriptionDetail entity) {
        String sql = "update prescription_detail set is_deleted=1 where id = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getId());
            if(ps.executeUpdate() > 0) {
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
