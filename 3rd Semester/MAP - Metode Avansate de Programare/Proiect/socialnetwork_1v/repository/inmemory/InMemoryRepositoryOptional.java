package com.example.socialnetwork_1v.repository.inmemory;

import com.example.socialnetwork_1v.repository.RepositoryOptional;
import com.example.socialnetwork_1v.domain.Entity;
import com.example.socialnetwork_1v.domain.validators.Validator;
import com.example.socialnetwork_1v.repository.database.paging.Page;
import com.example.socialnetwork_1v.repository.database.paging.Pageable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepositoryOptional<ID, E extends Entity<ID>> implements RepositoryOptional<ID, E> {
    private final Validator<E> validator;

    Map<ID, E> entities;

    public InMemoryRepositoryOptional(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entitatea nu poate fi nula");
        }
        validator.validate(entity);

        if (entities.get(entity.getId()) != null) {
            return Optional.of(entity);
        } else entities.put(entity.getId(), entity);

        return Optional.empty();
    }

    @Override
    public Optional<E> delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id-ul nu poate sa fie null");
        }
        E entity = entities.remove(id);
        if (entity != null) {
            return Optional.of(entity);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<E> update(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        }
        validator.validate(entity);
        return Optional.ofNullable(entities.computeIfPresent(entity.getId(), (k, v) -> entity));
    }

    @Override
    public Iterable<E> changeEntities(Map<ID, E> entities) {
        this.entities = entities;
        return this.entities.values();
    }

    @Override
    public Page<E> findAllOnPage(Pageable pageable) {
        return null;
    }
}
