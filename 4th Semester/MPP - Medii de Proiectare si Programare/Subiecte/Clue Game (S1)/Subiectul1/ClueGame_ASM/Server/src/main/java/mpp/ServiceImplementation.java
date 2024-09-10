package mpp;

import mpp.Exceptions.GenericException;
import mpp.Interface.IRepoConfiguratie;
import mpp.Interface.IRepoGame;
import mpp.Interface.IRepoJucatorGame;
import mpp.Interface.IRepoPlayer;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImplementation implements IServices{
    private final IRepoPlayer repoPlayer;
    private final IRepoConfiguratie repoConfiguratie;
    private final IRepoGame repoGame;
    private final IRepoJucatorGame repoJucatorGame;


    HashMap<Integer, IObserver> onlineClients;

    public ServiceImplementation(IRepoPlayer repoPlayer, IRepoConfiguratie repoConfiguratie, IRepoGame repoGame, IRepoJucatorGame repoJucatorGame) {
        this.repoPlayer = repoPlayer;
        this.repoConfiguratie = repoConfiguratie;
        this.repoGame = repoGame;
        this.repoJucatorGame = repoJucatorGame;
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
    public Iterable<JucatorGame> findAllJucatoriGames() throws GenericException {
        return repoJucatorGame.findAll();
    }

    @Override
    public Configuratie saveConfiguratie(Configuratie configuratie) throws GenericException {
        return repoConfiguratie.save(configuratie).get();
    }

    @Override
    public Game saveGame(Game game) throws GenericException {
        return repoGame.save(game).get();
    }

    private final int defaultThreadsNo=5;
    @Override
    public JucatorGame saveJucatoriGames(JucatorGame jucatorGame) throws GenericException {
        JucatorGame jg = repoJucatorGame.save(jucatorGame).get();

        ExecutorService executorService = Executors.newFixedThreadPool(defaultThreadsNo);
        for(var user: onlineClients.keySet()){
            System.out.println("Notifying user with id: " + user.toString());
            onlineClients.get(user).gameFinished(jucatorGame);
        }
        executorService.shutdown();
        return jg;
    }
}
