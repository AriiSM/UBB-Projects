package com.example.socialnetwork_1v.repository;

import com.example.socialnetwork_1v.exceptii.ValidationException;
import com.example.socialnetwork_1v.domain.Entity;

import java.util.Map;


/**
 * CRUD operations repository interface
 *
 * @param <ID> type E must have an attribute of type ID
 * @param <E>  type of entities saved in repository
 */

public interface Repository<ID, E extends Entity<ID>> {

    //schimba entitatea din map-ul din repo Map
    Iterable<E> changeEntities(Map<ID, E> entities);

    /**
     * @param id - the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     */
    E findOne(ID id);

    /**
     * @return all entities
     */
    Iterable<E> findAll();

    /**
     * @param entity entity must be not null
     * @return null - if the given entity is saved
     * otherwise returns the entity (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null
     */
    E save(E entity);

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the gived id
     * @throws IllegalArgumentException if the given id is null
     */
    E delete(ID id);

    /**
     * @param entity entity must not be null
     * @return null - if the entity is updated,
     * otherwise return the entity
     * @throws IllegalArgumentException if the given entity is null
     * @throws ValidationException      if the entity is not valid
     */
    E update(E entity);
}
