package com.aptech.eproject2_prosmiles.Model.Entity;

import com.aptech.eproject2_prosmiles.Model.Enum.EItemType;
import com.aptech.eproject2_prosmiles.Model.Enum.IsDeleted;

import java.time.LocalDateTime;

public abstract class Item {
    private int id;
    private String name;
    private Category category;
    private EItemType itemType;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private IsDeleted itemDeleted;

    public Item(int id, String name,Category category, EItemType itemType, LocalDateTime createAt, LocalDateTime updateAt, IsDeleted itemDeleted) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.itemType = itemType;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.itemDeleted = itemDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EItemType getItemType() {
        return itemType;
    }

    public void setItemType(EItemType itemType) {
        this.itemType = itemType;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public IsDeleted getItemDeleted() {
        return itemDeleted;
    }

    public void setItemDeleted(IsDeleted itemDeleted) {
        this.itemDeleted = itemDeleted;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", itemType=" + itemType +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", itemDeleted=" + itemDeleted +
                '}';
    }
}
