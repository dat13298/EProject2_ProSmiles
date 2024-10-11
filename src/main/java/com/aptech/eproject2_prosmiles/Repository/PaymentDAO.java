package com.aptech.eproject2_prosmiles.Repository;

import com.aptech.eproject2_prosmiles.Conectivity.MySQLConnection;
import com.aptech.eproject2_prosmiles.IGeneric.DentalRepository;
import com.aptech.eproject2_prosmiles.Model.Entity.Payment;
import com.aptech.eproject2_prosmiles.Model.Entity.Prescription;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;
import com.aptech.eproject2_prosmiles.Model.Enum.EPaymentType;
import com.aptech.eproject2_prosmiles.Model.Enum.EStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class PaymentDAO implements DentalRepository<Payment> {
    public static Connection conn = MySQLConnection.getConnection();
    public static ObservableList<Payment> payments = FXCollections.observableArrayList();

    @Override
    public ObservableList<Payment> getAll() {
        String sql = "select p.id, p.bill_number, p.prescription_id, " +
                "p.payment_type, p.total_amount,p.created_at, p.updated_at, p.is_deleted " +
                "from payment p where is_deleted = 0";

        ObservableList<Payment> newPayments = FXCollections.observableArrayList();
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Prescription prescription = new Prescription();
                Payment payment = new Payment();
                payment.setId(rs.getInt("id"));
                payment.setBillNumber(rs.getString("bill_number"));

                prescription.setId(rs.getInt("prescription_id"));
                payment.setPrescription(Optional.of(prescription));

                payment.setPaymentType(EPaymentType.fromValue(rs.getString("payment_type")));
                payment.setTotalAmount(rs.getDouble("total_amount"));

                Timestamp timestamp = rs.getTimestamp("created_at");
                LocalDateTime createAt = timestamp == null ? null : timestamp.toLocalDateTime();
                payment.setCreatedAt(createAt);

                Timestamp timestamp2 = rs.getTimestamp("updated_at");
                LocalDateTime updateAt = timestamp2 == null ? null : timestamp2.toLocalDateTime();
                payment.setUpdatedAt(updateAt);

                payment.setIsDeleted(EIsDeleted.fromInt(rs.getInt("is_deleted")));
                newPayments.add(payment);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        payments.clear();
        payments.addAll(newPayments);

        return payments;
    }

    @Override
    public Payment getById(int id) {
        String sql = "select p.id, p.bill_number, p.prescription_id" +
                "p.payment_type, p.total_amount, p.created_at, p.updated_at,p.is_deleted from prescription p where id = ?";
        Payment payment = new Payment();
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Prescription prescription = new Prescription();

                payment.setId(rs.getInt("id"));
                payment.setBillNumber(rs.getString("bill_number"));
                prescription.setId(rs.getInt("prescription_id"));
                payment.setPrescription(Optional.of(prescription));
                payment.setPaymentType(EPaymentType.fromValue(rs.getString("payment_type")));
                payment.setTotalAmount(rs.getDouble("total_amount"));

                Timestamp timestamp = rs.getTimestamp("create_at");
                LocalDateTime createAt = timestamp == null ? null : timestamp.toLocalDateTime();
                payment.setCreatedAt(createAt);

                Timestamp timestamp2 = rs.getTimestamp("update_at");
                LocalDateTime updateAt = timestamp2 == null ? null : timestamp2.toLocalDateTime();
                payment.setUpdatedAt(updateAt);

                payment.setIsDeleted(EIsDeleted.fromInt(rs.getInt("is_deleted")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return payment;
    }

    @Override
    public ObservableList<Payment> findByName(String name) {
        String sql = "select p.id, p.bill_number, p.prescription_id" +
                "p.payment_type, p.total_amount, p. created_at, p.updated_at, p.is_deleted from payment p WHERE name = ?";
        Payment payment = new Payment();
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Prescription prescription = new Prescription();
                payment.setId(rs.getInt("id"));
                payment.setBillNumber(rs.getString("bill_number"));
                prescription.setId(rs.getInt("prescription_id"));
                payment.setPrescription(Optional.of(prescription));
                payment.setPaymentType(EPaymentType.valueOf(rs.getString("payment_type")));
                payment.setTotalAmount(rs.getDouble("total_amount"));

                Timestamp timestamp = rs.getTimestamp("create_at");
                LocalDateTime createAt = timestamp == null ? null : timestamp.toLocalDateTime();
                payment.setCreatedAt(createAt);

                Timestamp timestamp2 = rs.getTimestamp("update_at");
                LocalDateTime updateAt = timestamp2 == null ? null : timestamp2.toLocalDateTime();
                payment.setUpdatedAt(updateAt);

                payment.setIsDeleted(EIsDeleted.fromInt(rs.getInt("is_deleted")));

                payments.add(payment);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    @Override
    public Payment save(Payment entity) {
        String sql = "insert into payment (bill_number, prescription_id, payment_type, total_amount, created_at , updated_at) values (?,?,?,?,?, ?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getBillNumber());
            ps.setInt(2, entity.getPrescription().getId());
            ps.setString(3, entity.getPaymentType().getValue());
            ps.setDouble(4, entity.getTotalAmount());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Payment update(Payment entity) {
        String sql = "update payment set id=?,bill_number=?,prescription_id=?,payment_type=?,total_amount=?,cre, updated_at=? where id=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getBillNumber());
            ps.setInt(3, entity.getPrescription().getId());
            ps.setString(4, entity.getPaymentType().getValue());
            ps.setDouble(5, entity.getTotalAmount());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(7, entity.getId());
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
