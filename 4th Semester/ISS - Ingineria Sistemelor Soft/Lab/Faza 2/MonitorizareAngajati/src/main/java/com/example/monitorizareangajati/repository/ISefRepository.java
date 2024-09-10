package com.example.monitorizareangajati.repository;

import com.example.monitorizareangajati.domain.Sef;

public interface ISefRepository extends ICrudRepository<Integer, Sef> {
    Sef findAccount(String username, String parola);
}
