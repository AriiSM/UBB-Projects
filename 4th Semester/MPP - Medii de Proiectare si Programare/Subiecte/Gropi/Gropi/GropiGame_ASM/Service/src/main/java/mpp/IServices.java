package mpp;


import mpp.Exceptions.GenericException;

import java.util.Optional;

public interface IServices {
    Persoana login(String username, IObserver client) throws GenericException;
    Persoana findJucator(String username) throws GenericException;

    Optional<Configurare> saveConfiguratie(Configurare conf) throws GenericException;
    Optional<Game> saveGame(Game game) throws GenericException;
    Optional<JucatorGame> saveJucatorGame(JucatorGame jucatorGame) throws GenericException;

    Iterable<JucatorGame> findAllJucatoriGames() throws GenericException;
}
