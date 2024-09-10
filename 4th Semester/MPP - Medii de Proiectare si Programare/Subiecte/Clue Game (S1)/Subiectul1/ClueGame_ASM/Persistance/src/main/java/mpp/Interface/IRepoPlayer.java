package mpp.Interface;

import mpp.Persoana;

import java.util.Optional;

public interface IRepoPlayer extends IRepository<Integer, Persoana>{
    public Optional<Persoana> login(String username);
    public Optional<Persoana> findJucator(String username);
}
