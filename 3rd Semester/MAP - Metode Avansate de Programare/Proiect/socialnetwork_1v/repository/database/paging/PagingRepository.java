package com.example.socialnetwork_1v.repository.database.paging;

import com.example.socialnetwork_1v.domain.Entity;
import com.example.socialnetwork_1v.repository.Repository;
import com.example.socialnetwork_1v.repository.RepositoryOptional;

public interface PagingRepository<ID, E extends Entity<ID>> extends RepositoryOptional<ID, E> {
    Page<E> findAllOnPage(Pageable pageable);
}