package mpp.Interface;

import mpp.Configurare;

import java.util.Optional;

public interface IRepoConfigurare extends IRepository<Integer, Configurare> {
    Optional<Configurare> findConfigurare();
}
