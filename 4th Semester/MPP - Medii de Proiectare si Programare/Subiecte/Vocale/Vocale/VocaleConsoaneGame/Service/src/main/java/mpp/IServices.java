package mpp;


import mpp.Exceptions.GenericException;

public interface IServices {
    Persoana login(String username, IObserver client) throws GenericException;
    Persoana findJucator(String username) throws GenericException;

    Iterable<JucatorGame> findAllJucatoriGames() throws GenericException;
    String findOneCuvant() throws GenericException;
    Configurare saveConfigurare(Configurare configurare) throws GenericException;
    Game saveGame(Game game) throws GenericException;
    JucatorGame saveJucatorGame(JucatorGame jucatorGame) throws GenericException;
}
