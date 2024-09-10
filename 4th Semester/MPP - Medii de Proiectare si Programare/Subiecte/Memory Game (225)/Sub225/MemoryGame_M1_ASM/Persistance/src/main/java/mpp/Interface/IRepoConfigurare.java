package mpp.Interface;

import mpp.Configurare;

import java.util.Optional;

public interface IRepoConfigurare extends IRepository<Integer, Configurare>{
    public void updateWithId(Integer id, String attribute, String newValue);

}
