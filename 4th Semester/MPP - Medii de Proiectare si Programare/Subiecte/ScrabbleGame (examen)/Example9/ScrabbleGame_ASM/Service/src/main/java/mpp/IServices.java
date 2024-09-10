package mpp;


import mpp.Exceptions.GenericException;

public interface IServices {
    Persoana login(String username, IObserver client) throws GenericException;
    Persoana findJucator(String username) throws GenericException;

    Configurare findOneCuvant() throws GenericException;
    Configurare saveConfigurare(Configurare configurare) throws  GenericException;
    Game saveGame(Game game) throws GenericException;
    JucatoriGames saveJucatorGames(JucatoriGames jucatorGame) throws GenericException;

    Iterable<JucatoriGames> findAllJucatoriGames() throws GenericException;
}
