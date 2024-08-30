package com.aptech.eproject2_prosmiles.IGeneric;

import javafx.collections.ObservableList;

import java.util.List;

public interface DentalRepository<T>{
    ObservableList<T> getAll();
    T getById(int id);
    ObservableList<T> findByName(String name);
    T save(T entity);
    T update(T entity);
    boolean delete(T entity);
}
