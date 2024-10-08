package com.aptech.eproject2_prosmiles.Repository;

import com.aptech.eproject2_prosmiles.Conectivity.MySQLConnection;
import com.aptech.eproject2_prosmiles.Global.Format;
import com.aptech.eproject2_prosmiles.IGeneric.DentalRepository;
import com.aptech.eproject2_prosmiles.Model.Entity.Patient;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Model.Entity.Staff;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Model.Enum.EStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrescriptionDAO implements DentalRepository<Prescription> {
    public static Connection conn = MySQLConnection.getConnection();
    public static ObservableList<Prescription> prescriptions = FXCollections.observableArrayList();


    @Override
    public ObservableList<Prescription> getAll() {
        try{
            String sql = "select p.id, p.patient_id, p.staff_id, " +
                    "p.description, p.status, p.created_at, " +
                    "p.updated_at, p.is_deleted " +
                    "from prescription p where 1=1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Prescription p = new Prescription();
                Patient pt = new Patient();
                Staff st = new Staff();

                p.setId(rs.getInt("id"));
                pt.setId(rs.getInt("patient_id"));
                p.setPatient(Optional.of(pt));
                st.setId(rs.getInt("staff_id"));
                p.setStaff(Optional.of(st));
                String status = rs.getString("status");
                p.setStatus(EStatus.fromString(status));

                p.setDescription(rs.getString("description"));

                Timestamp createTime = rs.getTimestamp("created_at");
                LocalDateTime createAt = createTime == null ? null : createTime.toLocalDateTime();
                p.setCreatedAt(createAt);

                Timestamp updateTime = rs.getTimestamp("updated_at");
                LocalDateTime updateAt = updateTime == null ? null : updateTime.toLocalDateTime();
                p.setUpdatedAt(updateAt);

                p.setIsDeleted(EIsDeleted.fromInt(rs.getInt("is_deleted")));
                prescriptions.add(p);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return prescriptions;
    }

    @Override
    public Prescription getById(int id) {
        String sql = "select p.id, p.patient_id, p.staff_id, " +
                "p.description, p.status, p.created_at, " +
                "p.updated_at, p.is_deleted" +
                " from prescription p where p.id = ?";
        Prescription p = new Prescription();
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Patient pt = new Patient();
                Staff st = new Staff();

                p.setId(rs.getInt("id"));
                pt.setId(rs.getInt("patient_id"));
                p.setPatient(Optional.of(pt));
                st.setId(rs.getInt("staff_id"));
                p.setStaff(Optional.of(st));

                p.setDescription(rs.getString("description"));
                p.setStatus(EStatus.fromString(rs.getString("status")));

                Timestamp createTime = rs.getTimestamp("created_at");
                LocalDateTime createAt = createTime == null ? null : createTime.toLocalDateTime();
                p.setCreatedAt(createAt);

                Timestamp updateTime = rs.getTimestamp("updated_at");
                LocalDateTime updateAt = updateTime == null ? null : updateTime.toLocalDateTime();
                p.setUpdatedAt(updateAt);

                p.setIsDeleted(EIsDeleted.fromInt(rs.getInt("is_deleted")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return p;
    }

    @Override
    public ObservableList<Prescription> findByName(String name) {
        String sql = "select p.id, p.patient_id, p.staff_id, " +
                "p.description, p.status, p.created_at, p.updated_at" +
                " from prescription p where p.name = ?";
        Prescription p = new Prescription();
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Patient pt = new Patient();
                Staff st = new Staff();

                p.setId(rs.getInt("id"));
                pt.setId(rs.getInt("patient_id"));
                p.setPatient(Optional.of(pt));
                st.setId(rs.getInt("staff_id"));
                p.setStaff(Optional.of(st));

                p.setDescription(rs.getString("description"));
                p.setStatus(EStatus.valueOf(rs.getString("status")));
                Timestamp createTime = rs.getTimestamp("created_at");
                LocalDateTime createAt = createTime == null ? null : createTime.toLocalDateTime();
                p.setCreatedAt(createAt);

                Timestamp updateTime = rs.getTimestamp("updated_at");
                LocalDateTime updateAt = updateTime == null ? null : updateTime.toLocalDateTime();
                p.setUpdatedAt(updateAt);

                p.setIsDeleted(EIsDeleted.fromInt(rs.getInt("is_deleted")));
                prescriptions.add(p);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return prescriptions;
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
            ps.setString(5, entity.getStatus().getStatus());
            ps.setString(6, entity.getCreatedAt().toString());
            ps.setString(7, entity.getUpdatedAt().toString());
            ps.setInt(8, entity.getIsDeleted().getValue());
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
            ps.setString(5, entity.getStatus().getStatus());
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

}
