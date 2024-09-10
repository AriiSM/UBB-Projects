package mpp;

import mpp.Exceptions.GenericException;
import mpp.Interface.IRepoConfigurare;
import mpp.Interface.IRepoGame;
import mpp.Interface.IRepoJucatorGame;
import mpp.Interface.IRepoPlayer;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImplementation implements IServices{
    private final IRepoPlayer repoPlayer;
    private final IRepoJucatorGame repoJucatorGame;
    private final IRepoGame repoGame;
    private final IRepoConfigurare repoConfigurare;



    HashMap<Integer, IObserver> onlineClients;

    public ServiceImplementation(IRepoPlayer repoPlayer, IRepoJucatorGame repoJucatorGame, IRepoGame repoGame, IRepoConfigurare repoConfigurare) {
        this.repoPlayer = repoPlayer;
        this.repoJucatorGame = repoJucatorGame;
        this.repoGame = repoGame;
        this.repoConfigurare = repoConfigurare;
        onlineClients = new HashMap<>();
    }

    @Override
    public Persoana login(String username, IObserver client) throws GenericException {
        Optional<Persoana> player = repoPlayer.login(username);
        if (player.isPresent()) {
            onlineClients.put(player.get().getId(), client);
            return player.get();
        }
        return null;
    }

    @Override
    public Persoana findJucator(String username) {
        Optional<Persoana> player = repoPlayer.findJucator(username);
        if (player.isPresent()) {
            return player.get();
        }
        return null;
    }

    @Override
    public Optional<Configurare> saveConfiguratie(Configurare conf) throws GenericException {
        Optional<Configurare> configurare = repoConfigurare.save(conf);
        return configurare;
    }

    @Override
    public Optional<Game> saveGame(Game game) throws GenericException {
        return repoGame.save(game);
    }

    private final int defaultThreadsNo=5;
    @Override
    public Optional<JucatorGame> saveJucatorGame(JucatorGame jucatorGame) throws GenericException {
        JucatorGame jg = repoJucatorGame.save(jucatorGame).get();

        ExecutorService executorService = Executors.newFixedThreadPool(defaultThreadsNo);
        for(var user: onlineClients.keySet()){
            System.out.println("Notifying user with id: " + user.toString());
            onlineClients.get(user).gameFinished(jucatorGame);
        }
        executorService.shutdown();
        return Optional.of(jg);
    }

    @Override
    public Iterable<JucatorGame> findAllJucatoriGames() throws GenericException {
        return repoJucatorGame.findAll();
    }
}
