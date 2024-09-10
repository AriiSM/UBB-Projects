package mpp.Interface;

import mpp.Jucator;

import java.util.Optional;

public interface IRepoPlayer extends IRepository<Integer, Jucator>{
    public Optional<Jucator> login(String username);
    public Optional<Jucator> findJucator(String username);
}
