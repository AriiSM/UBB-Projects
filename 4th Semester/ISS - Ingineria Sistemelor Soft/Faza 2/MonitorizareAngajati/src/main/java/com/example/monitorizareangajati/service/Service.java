package com.example.monitorizareangajati.service;

import com.example.monitorizareangajati.domain.Angajat;
import com.example.monitorizareangajati.domain.Sef;
import com.example.monitorizareangajati.repository.jdbc.AngajatDBRepository;
import com.example.monitorizareangajati.repository.jdbc.AtribuieSarcinaDBRepository;
import com.example.monitorizareangajati.repository.jdbc.SarcinaDBRepository;
import com.example.monitorizareangajati.repository.jdbc.SefDBRepository;

public class Service {
    private final AngajatDBRepository angajatDBRepository;
    private final AtribuieSarcinaDBRepository atribuieSarcinaDBRepository;
    private final SarcinaDBRepository sarcinaDBRepository;
    private final SefDBRepository sefDBRepository;

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
}
