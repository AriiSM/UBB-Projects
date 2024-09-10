package com.example.socialnetwork_1v.starters;

import com.example.socialnetwork_1v.domain.Prietenie;
import com.example.socialnetwork_1v.domain.Tuple;
import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.domain.validators.PrietenieValidator;
import com.example.socialnetwork_1v.domain.validators.UtilizatorValidator;
import com.example.socialnetwork_1v.repository.inmemory.InMemoryRepositoryOptional;
import com.example.socialnetwork_1v.service.UtilizatorServiceOptional;
import com.example.socialnetwork_1v.ui.UiOptional;


public class MainOptional {
    public static void main(String[] args) {

        InMemoryRepositoryOptional<Long, Utilizator> utilizatorInMemoryRepository = new InMemoryRepositoryOptional<>(new UtilizatorValidator());
        InMemoryRepositoryOptional<Tuple<Long, Long>, Prietenie> prietenieInMemoryRepository = new InMemoryRepositoryOptional<>(new PrietenieValidator(utilizatorInMemoryRepository));
        UtilizatorServiceOptional service = new UtilizatorServiceOptional(utilizatorInMemoryRepository, prietenieInMemoryRepository);


        UiOptional console = new UiOptional(service);
        //rulare consola
        console.run();
    }
}