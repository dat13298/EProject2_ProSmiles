package com.aptech.eproject2_prosmiles.IGeneric;

import java.util.List;

public interface DentalRepository<T>{
    List<T> getAll();
    T getById(int id);
    T findByName(String name);
    T save(T entity);
    T update(T entity);
    boolean delete(T entity);
}
