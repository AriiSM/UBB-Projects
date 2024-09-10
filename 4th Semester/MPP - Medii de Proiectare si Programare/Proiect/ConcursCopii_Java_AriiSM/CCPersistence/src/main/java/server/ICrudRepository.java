package server;


import java.util.Optional;

public interface ICrudRepository<ID, E>{
    Optional<E> findOne(ID id);
    Iterable<E> findAll();
    Optional<E> save(E entity);
}
