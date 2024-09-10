package com.example.monitorizareangajati.repository;

import com.example.monitorizareangajati.domain.Sarcina;

import java.util.Optional;

public interface ISarcinaRepository extends ICrudRepository<Integer, Sarcina>{
    Optional<Sarcina> findByDescriere(String descriere);
}
