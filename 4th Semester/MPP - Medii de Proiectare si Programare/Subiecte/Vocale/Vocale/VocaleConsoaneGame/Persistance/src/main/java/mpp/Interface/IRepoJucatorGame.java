package mpp.Interface;

import mpp.JucatorGame;

public interface IRepoJucatorGame extends IRepository<Integer, JucatorGame>{
    public String findCerinta(Integer id);
}
