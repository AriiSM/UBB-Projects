package com.example.monitorizareangajati.repository;

import com.example.monitorizareangajati.domain.Angajat;

public interface IAngajatRepository extends ICrudRepository<Integer, Angajat>{
    Angajat findAccount(String parola, String username);
    Angajat findByNumePrenume(String nume, String prenume);
}
