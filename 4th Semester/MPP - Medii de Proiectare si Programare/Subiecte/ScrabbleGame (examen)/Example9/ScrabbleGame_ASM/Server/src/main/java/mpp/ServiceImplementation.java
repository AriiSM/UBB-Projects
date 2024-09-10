package mpp;

import mpp.Exceptions.GenericException;
import mpp.Interface.*;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImplementation implements IServices{
    private final IRepoPlayer repoPlayer;
    private final IRepoCuvant repoCuvant;
    private final IRepoGame repoGame;
    private final IRepoJucatorGame repoJucatorGamer;
    private final IRepoConfigurare repoConfigurare;


    HashMap<Integer, IObserver> onlineClients;

    public ServiceImplementation(IRepoPlayer repoPlayer,IRepoCuvant repoCuvant, IRepoGame repoGame, IRepoJucatorGame repoJucatorGame, IRepoConfigurare repoConfigurare) {
        this.repoConfigurare=repoConfigurare;
        this.repoGame = repoGame;
        this.repoJucatorGamer = repoJucatorGame;
        this.repoCuvant = repoCuvant;
        this.repoPlayer = repoPlayer;
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
    public Configurare findOneCuvant() throws GenericException {
        return repoConfigurare.findConfigurare().get();
    }

    @Override
    public Configurare saveConfigurare(Configurare configurare) throws GenericException {
        return repoConfigurare.save(configurare).get();
    }

    @Override
    public Game saveGame(Game game) throws GenericException {
        return repoGame.save(game).get();
    }

    private final int defaultThreadsNo = 5;
    @Override
    public JucatoriGames saveJucatorGames(JucatoriGames jucatorGame) throws GenericException {
        JucatoriGames jg = repoJucatorGamer.save(jucatorGame).get();
        ExecutorService executorService = Executors.newFixedThreadPool(defaultThreadsNo);
        for (var user : onlineClients.keySet()) {
            System.out.println("Notifying user with id: " + user.toString());
            onlineClients.get(user).gameFinished(jucatorGame);
        }
        executorService.shutdown();
        return jg;
    }

    @Override
    public Iterable<JucatoriGames> findAllJucatoriGames() throws GenericException {
        return repoJucatorGamer.findAll();
    }
}
