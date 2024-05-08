package com.example.socialnetwork_1v.starters;


import com.example.socialnetwork_1v.domain.validators.PrietenieValidator;
import com.example.socialnetwork_1v.domain.validators.UtilizatorValidator;
import com.example.socialnetwork_1v.repository.database.DbPrieteniRepository;
import com.example.socialnetwork_1v.repository.database.DbUtilizatoriRepository;
import com.example.socialnetwork_1v.service.DbUtilizatorService;
import com.example.socialnetwork_1v.ui.UiDB;

public class MainDB {
    public static void main(String[] args) {
        //TODO:Repo+Service fara OPTIONAL
        //InMemoryRepository<Long, Utilizator> utilizatorInMemoryRepository = new InMemoryRepository<>(new UtilizatorValidator());
        //InMemoryRepository<Tuple<Long, Long>, Prietenie> prietenieInMemoryRepository = new InMemoryRepository<>(new PrietenieValidator(utilizatorInMemoryRepository));
        //UtilizatorService service = new UtilizatorService(utilizatorInMemoryRepository, prietenieInMemoryRepository);


        //TODO: DB
        String url = "jdbc:postgresql://localhost:5432/SocialNetwork";
        String username = "postgres";
        String password = "qwertyuiop";

        DbUtilizatoriRepository userRepository = new DbUtilizatoriRepository(url,username,password,new UtilizatorValidator());
        DbPrieteniRepository prieteniRepository = new DbPrieteniRepository(url,username,password,new PrietenieValidator(userRepository));

        //DbUtilizator userRepository = new DbUtilizator(url,username,password,new UtilizatorValidator());
        //DbPrietenie prieteniRepository = new DbPrietenie(url,username,password,new PrietenieValidator(userRepository));

        DbUtilizatorService service = new DbUtilizatorService(userRepository, prieteniRepository);
        //apelare UI cu service ul de mai sus
        UiDB console = new UiDB(service);
        //rulare consola
        console.run();
    }
}