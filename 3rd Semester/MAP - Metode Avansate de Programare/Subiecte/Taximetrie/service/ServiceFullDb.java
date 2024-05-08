package com.example.taximetrie.service;

import com.example.taximetrie.domain.Comanda;
import com.example.taximetrie.domain.Persoana;
import com.example.taximetrie.domain.Sofer;
import com.example.taximetrie.observer.Observable;
import com.example.taximetrie.observer.Observer;
import com.example.taximetrie.observer.event.Event;
import com.example.taximetrie.repository.DbComanda_REPO;
import com.example.taximetrie.repository.DbPersoana_REPO;
import com.example.taximetrie.repository.DbSofer_REPO;
import com.example.taximetrie.repository.pagini.Page;
import com.example.taximetrie.repository.pagini.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServiceFullDb implements Observable<Event> {
    private DbPersoana_REPO persoana_repo;
    private DbSofer_REPO sofer_repo;
    private DbComanda_REPO comanda_repo;
    private List<Observer<Event>> observers = new ArrayList<>();


    public ServiceFullDb(DbPersoana_REPO persoana_repo, DbSofer_REPO sofer_repo, DbComanda_REPO comanda_repo) {
        this.persoana_repo = persoana_repo;
        this.sofer_repo = sofer_repo;
        this.comanda_repo = comanda_repo;
    }

    public Sofer findOneSoferBYusername(String username1) {
        List<Sofer> soferList = StreamSupport.stream(sofer_repo.findAll().spliterator(), false)
                .filter(n -> n.getUsername().equals(username1))
                .map(n -> new Sofer(n.getId(), n.getUsername(), n.getName(), n.getIndicativMasina()))
                .collect(Collectors.toList());

        if (soferList.size() == 0)
            return null;
        return soferList.get(0);
    }

    public Persoana findOnePersoanaByNume(String nume){
        List<Persoana> persoanaList = StreamSupport.stream(persoana_repo.findAll().spliterator(), false)
                .filter(n -> n.getName().equals(nume))
                .map(n -> new Persoana(n.getId(), n.getUsername(), n.getName()))
                .collect(Collectors.toList());
        if (persoanaList.size() == 0)
            return null;
        return persoanaList.get(0);
    }

    public Persoana findOnePersoanaBYusername(String username1) {
        List<Persoana> persoanaList = StreamSupport.stream(persoana_repo.findAll().spliterator(), false)
                .filter(n -> n.getUsername().equals(username1))
                .map(n -> new Persoana(n.getId(), n.getUsername(), n.getName()))
                .collect(Collectors.toList());
        if (persoanaList.size() == 0)
            return null;
        return persoanaList.get(0);
    }

    public Sofer findOneSoferByIndicator(String indicator_Masina){
        List<Sofer> soferList = StreamSupport.stream(sofer_repo.findAll().spliterator(), false)
                .filter(n -> n.getIndicativMasina().equals(indicator_Masina))
                .map(n -> new Sofer(n.getId(),n.getUsername(),n.getName(),n.getIndicativMasina()))
                .collect(Collectors.toList());

        if (soferList.size() == 0)
            return null;
        return soferList.get(0);
    }


    public Iterable<Comanda> getComandaList() {
        return comanda_repo.findAll();
    }

    public Optional<Persoana> findOnePersoana(Long id) {
        return persoana_repo.findOne(id);
    }

    public Page<Persoana> findAllPersoanaPage(Pageable pageable) {
        return persoana_repo.findAllOnPage(pageable);
    }

    public Optional<Sofer> findOneSofer(Long id) {
        return sofer_repo.findOne(id);
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

    public Long createID() {
        do {
            boolean bool = true;
            Long id = new Random().nextLong();
            if (id < 0) {
                id *= -1;
            }
            for (Comanda u : comanda_repo.findAll()) {
                if (id.equals(u.getId())) {
                    bool = false;
                    break;
                }
            }
            if (bool)
                return id;
        } while (true);
    }

    public void saveComanda(Long idp, Long ids, LocalDateTime data){
        Long idc = this.createID();
        Comanda comanda = new Comanda(idc,idp,ids,data);
        comanda_repo.save(comanda);
    }
}
