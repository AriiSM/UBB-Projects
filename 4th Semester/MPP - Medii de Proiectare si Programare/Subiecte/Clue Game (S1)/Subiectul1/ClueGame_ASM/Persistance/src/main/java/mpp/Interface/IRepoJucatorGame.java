package mpp.Interface;

import mpp.JucatorGame;
import mpp.Persoana;

public interface IRepoJucatorGame extends IRepository<Integer, JucatorGame>{
    public String findCerinta(Integer id);
}
