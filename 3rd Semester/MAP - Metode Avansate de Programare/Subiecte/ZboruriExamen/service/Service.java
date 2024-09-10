package com.example.zboruri.service;

import com.example.zboruri.domain.Client;
import com.example.zboruri.domain.Flight;
import com.example.zboruri.domain.Tiket;
import com.example.zboruri.observer.Observable;
import com.example.zboruri.observer.Observer;
import com.example.zboruri.observer.event.Event;
import com.example.zboruri.repository.DbClient_REPO;
import com.example.zboruri.repository.DbFlight_REPO;
import com.example.zboruri.repository.DbTiket_REPO;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service implements Observable<Event> {
    private DbClient_REPO client_repo;
    private DbFlight_REPO flight_repo;
    private DbTiket_REPO tiket_repo;
    private List<Observer<Event>> observers = new ArrayList<>();

    public Service(DbClient_REPO client_repo, DbFlight_REPO flight_repo, DbTiket_REPO tiket_repo) {
        this.client_repo = client_repo;
        this.flight_repo = flight_repo;
        this.tiket_repo = tiket_repo;
    }

    public Client findOneClientByUsername(String username1) {
        List<Client> clientList = StreamSupport.stream(client_repo.findAll().spliterator(), false)
                .filter(n -> n.getUsername().equals(username1))
                .map(n -> new Client(n.getId(), n.getUsername(), n.getName()))
                .collect(Collectors.toList());
        if (clientList.size() == 0)
            return null;
        return clientList.get(0);
    }

    public Iterable<Flight> getFlightList() {
        return flight_repo.findAll();
    }

    public Iterable<Tiket> getTiketList() {
        return tiket_repo.findAll();
    }

    public Long createID() {
        do {
            boolean bool = true;
            Long id = new Random().nextLong();
            if (id < 0) {
                id *= -1;
            }
            for (Tiket u : tiket_repo.findAll()) {
                if (id.equals(u.getId())) {
                    bool = false;
                    break;
                }
            }
            if (bool)
                return id;
        } while (true);
    }

    public void saveTiket(String username, LocalDateTime data, Long id_zbor) {
        Long idf = this.createID();
        Tiket tiket = new Tiket(idf, username, data, id_zbor);
        tiket_repo.save(tiket);
    }

    @Override
    public void addObserver(Observer<Event> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<Event> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObserver(Event t) {
        observers.forEach(o -> o.update(t));
    }

    // In Service.java
    public int getTicketsSoldForFlight(Long flightId) {
        return StreamSupport.stream(getTiketList().spliterator(), false)
                .filter(tiket -> tiket.getId_flight().equals(flightId))
                .collect(Collectors.toList()).size();
    }
}
