package mpp;


import mpp.Exceptions.GenericException;

public interface IServices {
    Persoana login(String username, IObserver client) throws GenericException;
    Persoana findJucator(String username) throws GenericException;

    Iterable<JucatorGame> findAllJucatoriGames() throws GenericException;
    Configuratie saveConfiguratie(Configuratie configuratie) throws GenericException;
    Game saveGame(Game game) throws GenericException;
    JucatorGame saveJucatoriGames(JucatorGame jucatorGame) throws GenericException;

}
