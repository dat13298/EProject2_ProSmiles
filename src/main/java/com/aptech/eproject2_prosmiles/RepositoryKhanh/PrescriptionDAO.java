package com.aptech.eproject2_prosmiles.RepositoryKhanh;

import com.aptech.eproject2_prosmiles.Conectivity.MySQLConnection;
import com.aptech.eproject2_prosmiles.IGeneric.DentalRepository;
import com.aptech.eproject2_prosmiles.Model.Entity.Patient;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Model.Enum.EStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO implements DentalRepository<Prescription> {
    public static Connection conn = MySQLConnection.getConnection();
    public static List<Patient> patients;
    public static List<Staff> staffs;

    @Override
    public List<Prescription> getAll() {
        List<Prescription> res = new ArrayList<Prescription>();
        try{
            String sql = "select id, patient_id, staff_id,description, status, created_at, updated_at, is_deleted  from prescription";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Prescription p = new Prescription();
                p.setId(rs.getInt("id"));
                p.setPatient(findPatientById(rs.getInt("patient_id")));
                p.setStaff(findStaffById(rs.getInt("staff_id")));
                p.setDescription(rs.getString("description"));
                p.setStatus(EStatus.valueOf(rs.getString("status")));
                p.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
                p.setUpdatedAt(LocalDateTime.parse(rs.getString("updated_at")));
                p.setIsDeleted(EIsDeleted.valueOf(rs.getString("is_deleted")));
                res.add(p);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public Prescription getById(int id) {
        String sql = "select * from prescription where id = ?";
        Prescription p = new Prescription();
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p.setId(rs.getInt("id"));
                p.setPatient(findPatientById(rs.getInt("patient_id")));
                p.setStaff(findStaffById(rs.getInt("staff_id")));
                p.setDescription(rs.getString("description"));
                p.setStatus(EStatus.valueOf(rs.getString("status")));
                p.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
                p.setUpdatedAt(LocalDateTime.parse(rs.getString("updated_at")));
                p.setIsDeleted(EIsDeleted.valueOf(rs.getString("is_deleted")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return p;
    }

    @Override
    public Prescription findByName(String name) {
        String sql = "select * from prescription where name = ?";
        Prescription p = new Prescription();
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p.setId(rs.getInt("id"));
                p.setPatient(findPatientById(rs.getInt("patient_id")));
                p.setStaff(findStaffById(rs.getInt("staff_id")));
                p.setDescription(rs.getString("description"));
                p.setStatus(EStatus.valueOf(rs.getString("status")));
                p.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
                p.setUpdatedAt(LocalDateTime.parse(rs.getString("updated_at")));
                p.setIsDeleted(EIsDeleted.valueOf(rs.getString("is_deleted")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return p;
    }

    @Override
    public Prescription save(Prescription entity) {
        String sql = "insert into prescription values (?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getId());
            ps.setInt(2, entity.getPatient().getId());
            ps.setInt(3, entity.getStaff().getId());
            ps.setString(4, entity.getDescription());
            ps.setString(5, entity.getStatus().toString());
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
    public Prescription update(Prescription entity) {
        String sql = "update prescription set id=?, patient_id=?,staff_id=?,description=?,status=?,created_at=?,updated_at=? where id=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getId());
            ps.setInt(2, entity.getPatient().getId());
            ps.setInt(3, entity.getStaff().getId());
            ps.setString(4, entity.getDescription());
            ps.setString(5, entity.getStatus().toString());
            ps.setString(6, entity.getCreatedAt().toString());
            ps.setString(7, entity.getUpdatedAt().toString());
            ps.setInt(8, entity.getId());
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public boolean delete(Prescription entity) {
        String sql = "update prescription set is_deleted = 1 where id = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getId());
            if(ps.executeUpdate() > 0){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Staff findStaffById(int id) {
        return staffs.stream()
                .filter(s -> s.getId() == id)
                .findFirst().orElse(null);
    }

    public Patient findPatientById(int id) {
        return patients.stream()
                .filter(p -> p.getId() == id)
                .findFirst().orElse(null);
    }
}
