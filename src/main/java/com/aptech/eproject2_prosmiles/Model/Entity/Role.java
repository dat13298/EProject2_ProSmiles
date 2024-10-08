package com.aptech.eproject2_prosmiles.Model.Entity;

import com.aptech.eproject2_prosmiles.Model.Enum.EIsDeleted;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Role {
    private int id;
    private String title;
    private String slug;
    private String description;
    private EIsDeleted isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Permission> permissions;

    public Role() {;}
    public Role(int id, String title, String slug, String description, EIsDeleted isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.permissions = new ArrayList<>();
    }
    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EIsDeleted getActive() {
        return isDeleted;
    }

    public void setActive(EIsDeleted isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return title;
    }
}
