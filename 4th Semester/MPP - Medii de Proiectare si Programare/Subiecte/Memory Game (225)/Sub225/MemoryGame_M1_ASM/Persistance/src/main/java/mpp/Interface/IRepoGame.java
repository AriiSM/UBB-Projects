package mpp.Interface;

import mpp.Game;

public interface IRepoGame extends IRepository<Integer, Game>{
    public String findCerinta(Integer id);

}
