package mpp.Interface;

import mpp.Exceptions.GenericException;

import java.util.Optional;

public interface IRepository<ID, E> {
    /**
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return an {@code Optional} encapsulating the entity with the given id
     */
    Optional<E> findOne(ID id);

    /**
     * @return all entities
     */
    Iterable<E> findAll();

    /**
     * @param entity entity must be not null
     * @return an {@code Optional} - null if the entity was saved,
     * - the entity (id already exists)
     * @throws GenericException if the entity is not valid
     */
    Optional<E> save(E entity) throws GenericException;

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return an {@code Optional}
     * - null if there is no entity with the given id,
     * - the removed entity, otherwise
     */
    Optional<E> delete(ID id);

    /**
     * @param entity entity must not be null
     * @return an {@code Optional}
     * - null if the entity was updated
     * - otherwise (e.g. id does not exist) returns the entity.
     * @throws GenericException if the entity is not valid.
     */
    Optional<E> update(E entity) throws GenericException;
}