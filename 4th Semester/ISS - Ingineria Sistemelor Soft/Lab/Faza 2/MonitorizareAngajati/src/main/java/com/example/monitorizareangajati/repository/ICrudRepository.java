package com.example.monitorizareangajati.repository;

import com.example.monitorizareangajati.domain.Entity;

import java.util.Optional;

public interface ICrudRepository <ID, E extends Entity<ID>>{
    Optional<E> findOne(ID id);
    Iterable<E> findAll();
    Optional<E> save(E entity);
    Optional<E> update(E entity);
    Optional<E> delete(ID id);
}
