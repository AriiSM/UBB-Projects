package com.example.clienti_locatie_horel_rezervare.repository;

import com.example.clienti_locatie_horel_rezervare.domain.Entity;

import java.util.Map;
import java.util.Optional;
public interface RepositoryOptional<ID, E extends Entity<ID>> {

    Optional<E> findOne(ID id);

    Iterable<E> findAll();

    Optional<E> save(E entity);

    Optional<E> delete(ID id);

    Optional<E> update(E entity);

    Iterable<E> changeEntities(Map<ID, E> entities);

}
