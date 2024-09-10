package mpp;

import mpp.Exceptions.GenericException;
import mpp.Interface.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImplementation implements IServices{
    private final IRepoPlayer repoPlayer;
    private final IRepoCuvinte repoCuvinte;
    private final IRepoGame repoGame;
    private final IRepoJucatorGame repoJucatorGame;
    private final IRepoConfigurare repoConfigurare;

    HashMap<Integer, IObserver> onlineClients;

    public ServiceImplementation(IRepoPlayer repoPlayer, IRepoCuvinte repoCuvinte, IRepoGame repoGame, IRepoJucatorGame repoJucatorGame, IRepoConfigurare repoConfigurare) {
        this.repoPlayer = repoPlayer;
        this.repoCuvinte = repoCuvinte;
        this.repoGame = repoGame;
        this.repoJucatorGame = repoJucatorGame;
        this.repoConfigurare = repoConfigurare;
        onlineClients = new HashMap<>();
    }

    @Override
    public Jucator login(String username, IObserver client) throws GenericException {
        Optional<Jucator> player = repoPlayer.login(username);
        if (player.isPresent()) {
            onlineClients.put(player.get().getId(), client);
            return player.get();
        }
        return null;
    }

    @Override
    public Jucator findJucator(String username) {
        Optional<Jucator> player = repoPlayer.findJucator(username);
        if (player.isPresent()) {
            return player.get();
        }
        return null;
    }

    @Override
    public String[] find5Pairs() throws GenericException {
        return repoCuvinte.find5Pairs();
    }

    @Override
    public Iterable<JucatoriGames> findAllJucatoriGames() throws GenericException {
        return repoJucatorGame.findAll();
    }

    @Override
    public Optional<Game> saveGame(Game entity) throws GenericException {
        return repoGame.save(entity);
    }

    private final int defaultThreadsNo=5;
    @Override
    public Optional<JucatoriGames> saveJucatorGame(JucatoriGames entity) throws GenericException {

        Optional<JucatoriGames> jg = repoJucatorGame.save(entity);

        ExecutorService executorService = Executors.newFixedThreadPool(defaultThreadsNo);
        for(var user: onlineClients.keySet()){
            System.out.println("Notifying user with id: " + user.toString());
            onlineClients.get(user).gameFinished(entity);
        }
        executorService.shutdown();
        return jg;
    }

    @Override
    public Optional<Configurare> saveConfigurare(Configurare entity) throws GenericException {
        return repoConfigurare.save(entity);
    }


}
