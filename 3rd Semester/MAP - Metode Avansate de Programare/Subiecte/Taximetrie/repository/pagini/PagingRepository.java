package com.example.taximetrie.repository.pagini;

import com.example.taximetrie.domain.Entity;
import com.example.taximetrie.repository.RepositoryOptional;

public interface PagingRepository <ID, E extends Entity<ID>> extends RepositoryOptional<ID, E> {
    Page<E> findAllOnPage(Pageable pageable);
}
