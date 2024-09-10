package mpp.Interface;

import mpp.JucatoriGames;

public interface IRepoJucatorGame extends IRepository<Integer, JucatoriGames> {
    public String findCerinta(Integer id);
}
