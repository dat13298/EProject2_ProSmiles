package com.aptech.eproject2_prosmiles.Repository;

import com.aptech.eproject2_prosmiles.Conectivity.MySQLConnection;
import com.aptech.eproject2_prosmiles.IGeneric.DentalRepository;
import com.aptech.eproject2_prosmiles.Model.Entity.Patient;
import com.aptech.eproject2_prosmiles.Model.Enum.EGender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;

public class PatientDAO implements DentalRepository<Patient> {
    private static final Connection conn = MySQLConnection.getConnection();

    @Override
    public ObservableList<Patient> getAll() {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        String query = "SELECT * FROM patient WHERE is_deleted = 0";
        try (PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                patients.add(new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        EGender.fromValue(rs.getString("gender")),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime(),
                        rs.getInt("is_deleted") == 0 ? EIsDeleted.INACTIVE : EIsDeleted.ACTIVE));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    @Override
    public Patient getById(int id) {
        String query = "SELECT * FROM patient WHERE id = ? AND is_deleted = 0";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        EGender.fromValue(rs.getString("gender")),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime(),
                        rs.getInt("is_deleted") == 0 ? EIsDeleted.INACTIVE : EIsDeleted.ACTIVE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ObservableList<Patient> findByName(String name) {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        String query = "SELECT * FROM patient WHERE name LIKE ? AND is_deleted = 0";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, "%" + name + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                patients.add(new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        EGender.fromValue(rs.getString("gender")),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime(),
                        rs.getInt("is_deleted") == 0 ? EIsDeleted.INACTIVE : EIsDeleted.ACTIVE));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    @Override
    public Patient save(Patient entity) {
        String query = "INSERT INTO patient (name, gender, phone, address, age, email, created_at, updated_at, is_deleted) VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW(), 0)";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, entity.getName());
            pst.setString(2, entity.getGender().name());
            pst.setString(3, entity.getPhone());
            pst.setString(4, entity.getAddress());
            pst.setInt(5, entity.getAge());
            pst.setString(6, entity.getEmail());
            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                return entity; // Ideally, you should return the created entity with ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Patient update(Patient entity) {
        String query = "UPDATE patient SET name = ?, gender = ?, phone = ?, address = ?, age = ?, email = ?, updated_at = NOW() WHERE id = ? AND is_deleted = 0";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, entity.getName());
            pst.setString(2, entity.getGender().name());
            pst.setString(3, entity.getPhone());
            pst.setString(4, entity.getAddress());
            pst.setInt(5, entity.getAge());
            pst.setString(6, entity.getEmail());
            pst.setInt(7, entity.getId());
            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                return entity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Patient entity) {
        String query = "UPDATE patient SET is_deleted = 1, deleted_at = NOW() WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, entity.getId());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
