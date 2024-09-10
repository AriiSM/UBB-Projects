package com.example.monitorizareangajati.service;

import com.example.monitorizareangajati.domain.Angajat;
import com.example.monitorizareangajati.domain.AtribuireSarcina;
import com.example.monitorizareangajati.domain.Sarcina;
import com.example.monitorizareangajati.domain.Sef;
import com.example.monitorizareangajati.repository.jdbc.AngajatDBRepository;
import com.example.monitorizareangajati.repository.jdbc.AtribuieSarcinaDBRepository;
import com.example.monitorizareangajati.repository.jdbc.SarcinaDBRepository;
import com.example.monitorizareangajati.repository.jdbc.SefDBRepository;
import com.example.monitorizareangajati.utils.Observable;
import com.example.monitorizareangajati.utils.Observer;
import com.example.monitorizareangajati.utils.events.EventImp;
import com.example.monitorizareangajati.utils.events.EventType;

import java.util.ArrayList;
import java.util.List;

public class Service implements Observable<EventImp> {
    private AngajatDBRepository angajatDBRepository;
    private AtribuieSarcinaDBRepository atribuieSarcinaDBRepository;
    private SarcinaDBRepository sarcinaDBRepository;
    private SefDBRepository sefDBRepository;

    public Service(AngajatDBRepository angajatDBRepository, AtribuieSarcinaDBRepository atribuieSarcinaDBRepository, SarcinaDBRepository sarcinaDBRepository, SefDBRepository sefDBRepository) {
        this.angajatDBRepository = angajatDBRepository;
        this.atribuieSarcinaDBRepository = atribuieSarcinaDBRepository;
        this.sarcinaDBRepository = sarcinaDBRepository;
        this.sefDBRepository = sefDBRepository;
    }

    public Sef findAccountSef(String parola, String username){
        return sefDBRepository.findAccount(username,parola);
    }
    public Angajat findAccountAngajat(String parola, String username){
        return angajatDBRepository.findAccount(parola,username);
    }

    public Angajat findAngajatByNumePrenume(String nume, String prenume){
        return angajatDBRepository.findByNumePrenume(nume,prenume);
    }

    public Iterable<AtribuireSarcina> findAllAtribuireSarcinaAngajat(Integer id){
        return atribuieSarcinaDBRepository.findAtribuireSarcinaAngajat(id);
    }

    public Iterable<AtribuireSarcina> findAllAtribuireSarcina(){
        return atribuieSarcinaDBRepository.findAll();
    }

    public Iterable<Angajat> findAllAngajati(){
        return angajatDBRepository.findAll();
    }

    public Sarcina saveSarcina(Sarcina sarcina){
        return sarcinaDBRepository.save(sarcina).get();
    }

    public AtribuireSarcina saveAtribuireSarcina(AtribuireSarcina atribuireSarcina){
        return atribuieSarcinaDBRepository.save(atribuireSarcina).get();
    }

    public Sarcina findSarcinaByDescriere(String descriere){
        return sarcinaDBRepository.findByDescriere(descriere).get();
    }

    public void updateAngajat(Angajat angajat){
        Angajat antajat = angajatDBRepository.update(angajat).get();
        notifyObserver(new EventImp(EventType.UPDATE,antajat));
    }

    public void updateAtribuieSarcina(AtribuireSarcina atribuireSarcina){
        AtribuireSarcina atribuireSarcina1 = atribuieSarcinaDBRepository.update(atribuireSarcina).get();
        notifyObserver(new EventImp(EventType.UPDATE,atribuireSarcina1));
    }

    public void deleteAtribuireSarcina(AtribuireSarcina atribuireSarcina){
        atribuieSarcinaDBRepository.delete(atribuireSarcina.getId());
        notifyObserver(new EventImp(EventType.DELETE,atribuireSarcina));
    }

    private final List<Observer<EventImp>> observers = new ArrayList<>();
    @Override
    public void addObserver(Observer<EventImp> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<EventImp> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObserver(EventImp t) {
        observers.stream().forEach((x -> x.update(t)));
    }
}
