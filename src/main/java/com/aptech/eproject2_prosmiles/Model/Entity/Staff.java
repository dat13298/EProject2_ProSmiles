package com.aptech.eproject2_prosmiles.Model.Entity;

import com.aptech.eproject2_prosmiles.Model.Enum.EItemType;
import com.aptech.eproject2_prosmiles.Model.Enum.IsDeleted;

import java.time.LocalDateTime;

public class Staff extends Item{
    private Role role;
    private Room room;
    private String phone;
    private String Password;
    private String address;
    private String email;
    private double salary;
    private int age;

    public Staff(int id, String name, Category category, LocalDateTime createAt, LocalDateTime updateAt, Role role, Room room, String phone, String password, String address, String email, double salary, int age) {
        super(id, name, category, EItemType.STAFF, createAt, updateAt, IsDeleted.ACTIVE);
        this.role = role;
        this.room = room;
        this.phone = phone;
        Password = password;
        this.address = address;
        this.email = email;
        this.salary = salary;
        this.age = age;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "role=" + role +
                ", room=" + room +
                ", phone='" + phone + '\'' +
                ", Password='" + Password + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", salary=" + salary +
                ", age=" + age +
                '}';
    }
}
