package com.aptech.eproject2_prosmiles.RepositoryKhanh;

import com.aptech.eproject2_prosmiles.Conectivity.MySQLConnection;
import com.aptech.eproject2_prosmiles.IGeneric.DentalRepository;
import com.aptech.eproject2_prosmiles.Model.Entity.Payment;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Model.Enum.EPaymentType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PaymentDAO implements DentalRepository<Payment> {
    public static Connection conn = MySQLConnection.getConnection();
    public static List<Payment> payments;
    public static List<Prescription> prescriptions;

    @Override
    public List<Payment> getAll() {
        String sql = "SELECT * FROM Payment";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Payment payment = new Payment();
                payment.setId(rs.getInt("id"));
                payment.setBillNumber(rs.getString("bill_number"));
                payment.setPrescription(prescriptions.get(rs.getInt("prescription_id")));
                payment.setPaymentType(EPaymentType.valueOf(rs.getString("payment_type")));
                payment.setTotalAmount(rs.getDouble("total_amount"));
                payment.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
                payment.setUpdatedAt(LocalDateTime.parse(rs.getString("updated_at")));
                payments.add(payment);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return payments;
    }

    @Override
    public Payment getById(int id) {
        String sql = "SELECT * FROM payment WHERE id = ?";
        Payment payment = new Payment();
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                payment.setId(rs.getInt("id"));
                payment.setBillNumber(rs.getString("bill_number"));
                payment.setPrescription(prescriptions.get(rs.getInt("prescription_id")));
                payment.setPaymentType(EPaymentType.valueOf(rs.getString("payment_type")));
                payment.setTotalAmount(rs.getDouble("total_amount"));
                payment.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
                payment.setUpdatedAt(LocalDateTime.parse(rs.getString("updated_at")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return payment;
    }

    @Override
    public Payment findByName(String name) {
        String sql = "SELECT * FROM payment WHERE name = ?";
        Payment payment = new Payment();
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                payment.setId(rs.getInt("id"));
                payment.setBillNumber(rs.getString("bill_number"));
                payment.setPrescription(prescriptions.get(rs.getInt("prescription_id")));
                payment.setPaymentType(EPaymentType.valueOf(rs.getString("payment_type")));
                payment.setTotalAmount(rs.getDouble("total_amount"));
                payment.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
                payment.setUpdatedAt(LocalDateTime.parse(rs.getString("updated_at")));
                payment.setIsDeleted(EIsDeleted.valueOf(rs.getString("is_deleted")));
                payments.add(payment);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return payment;
    }

    @Override
    public Payment save(Payment entity) {
        String sql = "insert into payment values (?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getBillNumber());
            ps.setInt(3, entity.getPrescription().getId());
            ps.setString(4, entity.getPaymentType().getValue());
            ps.setDouble(5, entity.getTotalAmount());
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
    public Payment update(Payment entity) {
        String sql = "update payment set id=?,bill_number=?,prescription_id=?,payment_type=?,total_amount=?,created_at=?,updated_at=? where id=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getBillNumber());
            ps.setInt(3, entity.getPrescription().getId());
            ps.setString(4, entity.getPaymentType().getValue());
            ps.setDouble(5, entity.getTotalAmount());
            ps.setString(6, entity.getCreatedAt().toString());
            ps.setString(7, entity.getUpdatedAt().toString());
            ps.setInt(8, entity.getId());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public boolean delete(Payment entity) {
        String sql = "update payment set is_deleted=1 where id=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getId());
            if(ps.executeUpdate()>0){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
