package com.aptech.eproject2_prosmiles.Repository;

import com.aptech.eproject2_prosmiles.Conectivity.MySQLConnection;
import com.aptech.eproject2_prosmiles.IGeneric.DentalRepository;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Model.Entity.PrescriptionDetail;
import com.aptech.eproject2_prosmiles.Model.Entity.Service;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class PrescriptionDetailDAO implements DentalRepository<PrescriptionDetail> {
    public static Connection conn = MySQLConnection.getConnection();
    public static ObservableList<PrescriptionDetail> psd = FXCollections.observableArrayList();

    @Override
    public ObservableList<PrescriptionDetail> getAll() {
        ObservableList<PrescriptionDetail> prescriptionDetails = FXCollections.observableArrayList();
        String sql = "select pd.id, pd.service_id, pd.prescription_id,pd.unit," +
                "pd.quantity,pd.price,pd.created_at," +
                "pd.updated_at,pd.is_deleted " +
                "from prescription_detail pd where is_deleted = 0";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Prescription prescription = new Prescription();
                Service service = new Service();
                PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
                prescriptionDetail.setId(rs.getInt("id"));

                service.setId(rs.getInt("service_id"));
                prescriptionDetail.setService(Optional.of(service));

                prescription.setId(rs.getInt("prescription_id"));
                prescriptionDetail.setPrescription(Optional.of(prescription));

                prescriptionDetail.setUnit(rs.getString("unit"));
                prescriptionDetail.setQuantity(rs.getInt("quantity"));
                prescriptionDetail.setPrice(rs.getDouble("price"));

                Timestamp createTime = rs.getTimestamp("created_at");
                LocalDateTime createAt = createTime == null ? null : createTime.toLocalDateTime();
                prescriptionDetail.setCreatedAt(createAt);

                Timestamp updateTime = rs.getTimestamp("updated_at");
                LocalDateTime updateAt = updateTime == null ? null : updateTime.toLocalDateTime();
                prescriptionDetail.setUpdatedAt(updateAt);

                prescriptionDetail.setIsDeleted(EIsDeleted.fromInt(rs.getInt("is_deleted")));
                prescriptionDetails.add(prescriptionDetail);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        psd.clear();
        psd.addAll(prescriptionDetails);
        return psd;
    }

    @Override
    public PrescriptionDetail getById(int id) {
        String sql = "select pd.id, pd.service_id, pd.prescription_id,pd.unit," +
                "pd.quantity,pd.price,pd.created_at," +
                "pd.updated_at,pd.is_deleted from prescription_detail pd where pd.prescription_id = ? and is_deleted = 0";
        PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Prescription prescription = new Prescription();
                Service service = new Service();

                prescriptionDetail.setId(rs.getInt("id"));

                service.setId(rs.getInt("service_id"));
                prescriptionDetail.setService(Optional.of(service));

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
        // Assuming 'id' is auto-incremented, remove it from the SQL statement
        String sql = "INSERT INTO prescription_detail (service_id, prescription_id, unit, quantity, price, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);  // Use RETURN_GENERATED_KEYS to get the generated ID
            ps.setInt(1, entity.getService().getId());
            ps.setInt(2, entity.getPrescription().getId());
            ps.setString(3, entity.getUnit());
            ps.setInt(4, entity.getQuantity());
            ps.setDouble(5, entity.getPrice());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();

            // Retrieve the generated ID
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getInt(1));  // Set the generated ID back to the entity
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }


    @Override
    public PrescriptionDetail update(PrescriptionDetail entity) {
        String sql = "update prescription_detail set service_id=?,prescription_id=?,unit=?,quantity=?,price=?,created_at=? where id=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getService().getId());
            ps.setInt(2, entity.getPrescription().getId());
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


    public ObservableList<PrescriptionDetail> getPresDetailByPresId(int presId) {
        String sql = "select pd.id, pd.service_id, pd.prescription_id, pd.unit, pd.quantity,pd.price,pd.created_at,pd.updated_at,pd.is_deleted from prescription_detail pd where pd.prescription_id = ?";
        ObservableList<PrescriptionDetail> newPrescriptionDetails = FXCollections.observableArrayList();
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, presId);
            ResultSet rs = ps.executeQuery();
                while (rs.next())
                {
                    Prescription prescription = new Prescription();
                    Service service = new Service();
                    PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
                    prescriptionDetail.setId(rs.getInt("id"));

                    service.setId(rs.getInt("service_id"));
                    prescriptionDetail.setService(Optional.of(service));

                    prescription.setId(rs.getInt("prescription_id"));
                    prescriptionDetail.setPrescription(Optional.of(prescription));

                    prescriptionDetail.setUnit(rs.getString("unit"));
                    prescriptionDetail.setQuantity(rs.getInt("quantity"));
                    prescriptionDetail.setPrice(rs.getDouble("price"));

                    Timestamp createTime = rs.getTimestamp("created_at");
                    LocalDateTime createAt = createTime == null ? null : createTime.toLocalDateTime();
                    prescriptionDetail.setCreatedAt(createAt);

                    Timestamp updateTime = rs.getTimestamp("updated_at");
                    LocalDateTime updateAt = updateTime == null ? null : updateTime.toLocalDateTime();
                    prescriptionDetail.setUpdatedAt(updateAt);

                    prescriptionDetail.setIsDeleted(EIsDeleted.fromInt(rs.getInt("is_deleted")));
                    newPrescriptionDetails.add(prescriptionDetail);
                }

        }catch (SQLException e){
            e.printStackTrace();
        }
        psd.clear();
        psd.addAll(newPrescriptionDetails);
        return psd;
    }

}
