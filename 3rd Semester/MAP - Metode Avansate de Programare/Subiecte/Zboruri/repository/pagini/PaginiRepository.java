package com.example.zboruri.repository.pagini;

import com.example.zboruri.domain.Entity;
import com.example.zboruri.repository.RepositoryOptional;

public interface PaginiRepository <ID, E extends Entity<ID>> extends RepositoryOptional<ID, E> {
    Page<E> findAllOnPage(Pageable pageable);
}
