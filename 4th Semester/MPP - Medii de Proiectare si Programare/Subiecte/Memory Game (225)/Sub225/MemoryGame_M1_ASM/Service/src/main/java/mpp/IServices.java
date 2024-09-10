package mpp;

import mpp.Exceptions.GenericException;

import java.util.Optional;

public interface IServices {
    Jucator login(String username, IObserver client) throws GenericException;
    Jucator findJucator(String username) throws GenericException;
    String[] find5Pairs() throws GenericException;

    Iterable<JucatoriGames> findAllJucatoriGames() throws GenericException;
    Optional<Game> saveGame(Game entity) throws GenericException;
    Optional<JucatoriGames> saveJucatorGame(JucatoriGames entity) throws GenericException;

    Optional<Configurare> saveConfigurare(Configurare entity) throws GenericException;
}
