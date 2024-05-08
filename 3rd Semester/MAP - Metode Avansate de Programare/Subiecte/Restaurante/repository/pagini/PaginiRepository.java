package com.example.restaurante.repository.pagini;


import com.example.restaurante.domain.Entity;
import com.example.restaurante.repository.RepositoryOptional;

public interface PaginiRepository <ID, E extends Entity<ID>> extends RepositoryOptional<ID, E> {
    Page<E> findAllOnPage(Pageable pageable);
}

