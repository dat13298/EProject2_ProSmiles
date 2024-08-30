package com.aptech.eproject2_prosmiles.RepositoryKhanh;

import com.aptech.eproject2_prosmiles.Conectivity.MySQLConnection;
import com.aptech.eproject2_prosmiles.IGeneric.DentalRepository;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Model.Entity.PrescriptionDetail;
import com.aptech.eproject2_prosmiles.Model.Entity.ServiceItem;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDetailDAO implements DentalRepository<PrescriptionDetail> {
    public static Connection conn = MySQLConnection.getConnection();
    List<PrescriptionDetail> psd = new ArrayList<PrescriptionDetail>();
    public static List<ServiceItem> serviceItems;
    public static List<Prescription> prescriptions;

    @Override
    public List<PrescriptionDetail> getAll() {
        String sql = "select * from prescription_detail";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
                prescriptionDetail.setId(rs.getInt("id"));
                prescriptionDetail.setServiceItem(serviceItems.get(rs.getInt("service_item_id")));
                prescriptionDetail.setPrescription(prescriptions.get(rs.getInt("prescription_id")));
                prescriptionDetail.setUnit(rs.getString("unit"));
                prescriptionDetail.setQuantity(rs.getInt("quantity"));
                prescriptionDetail.setPrice(rs.getDouble("price"));
                prescriptionDetail.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
                prescriptionDetail.setUpdatedAt(LocalDateTime.parse(rs.getString("updated_at")));
                prescriptionDetail.setIsDeleted(EIsDeleted.valueOf(rs.getString("is_deleted")));
                psd.add(prescriptionDetail);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return psd;
    }

    @Override
    public PrescriptionDetail getById(int id) {
        String sql = "select * from prescription_detail where id = ?";
        PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                prescriptionDetail.setId(psd.size() + 1);
                prescriptionDetail.setServiceItem(serviceItems.get(rs.getInt("service_item_id")));
                prescriptionDetail.setPrescription(prescriptions.get(rs.getInt("prescription_id")));
                prescriptionDetail.setUnit(rs.getString("unit"));
                prescriptionDetail.setQuantity(rs.getInt("quantity"));
                prescriptionDetail.setPrice(rs.getDouble("price"));
                prescriptionDetail.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
                prescriptionDetail.setUpdatedAt(LocalDateTime.parse(rs.getString("updated_at")));
                prescriptionDetail.setIsDeleted(EIsDeleted.valueOf(rs.getString("is_deleted")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return prescriptionDetail;
    }

    @Override
    public PrescriptionDetail findByName(String name) {
        String sql = "select * from prescription_detail where prescription_name = ?";
        PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                prescriptionDetail.setId(psd.size() + 1);
                prescriptionDetail.setServiceItem(serviceItems.get(rs.getInt("service_item_id")));
                prescriptionDetail.setPrescription(prescriptions.get(rs.getInt("prescription_id")));
                prescriptionDetail.setUnit(rs.getString("unit"));
                prescriptionDetail.setQuantity(rs.getInt("quantity"));
                prescriptionDetail.setPrice(rs.getDouble("price"));
                prescriptionDetail.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
                prescriptionDetail.setUpdatedAt(LocalDateTime.parse(rs.getString("updated_at")));
                prescriptionDetail.setIsDeleted(EIsDeleted.valueOf(rs.getString("is_deleted")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return prescriptionDetail;
    }

    @Override
    public PrescriptionDetail save(PrescriptionDetail entity) {
        String sql = "insert into prescription_detail values (?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getServiceItem().getId());
            ps.setInt(2, entity.getPrescription().getId());
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
            ps.setInt(1, entity.getServiceItem().getId());
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


}
