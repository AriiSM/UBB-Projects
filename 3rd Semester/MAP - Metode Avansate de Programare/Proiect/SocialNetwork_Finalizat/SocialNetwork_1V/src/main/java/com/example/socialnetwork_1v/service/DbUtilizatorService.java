package com.example.socialnetwork_1v.service;


import com.example.socialnetwork_1v.domain.Comunitate;
import com.example.socialnetwork_1v.domain.Prietenie;
import com.example.socialnetwork_1v.domain.Tuple;
import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.repository.RepositoryOptional;
import com.example.socialnetwork_1v.repository.database.paging.Page;
import com.example.socialnetwork_1v.repository.database.paging.Pageable;

import java.time.LocalDateTime;
import java.util.*;

public class DbUtilizatorService {
    private final RepositoryOptional<Long, Utilizator> utilizatorRepository;
    private final RepositoryOptional<Tuple<Long, Long>, Prietenie> prietenieRepository;

    Comunitate comunitate = new Comunitate();


    public DbUtilizatorService(RepositoryOptional<Long, Utilizator> userDataBase, RepositoryOptional<Tuple<Long, Long>, Prietenie> repoPrietenie) {
        this.utilizatorRepository = userDataBase;
        this.prietenieRepository = repoPrietenie;
    }

    public List<Utilizator> sociabilComunitate() {
        return comunitate.sociabil_com();
    }

    public int numarComunitati() {
        return comunitate.nr_comunitati();
    }

    public String[] desparte(String numeSiPrenume) {
        String[] list;
        list = numeSiPrenume.split(" ");
        return list;
    }

    public void addUtilizator(String firstName, String lastName) throws Exception {
        Utilizator utilizator_nou = new Utilizator(firstName, lastName);
        utilizator_nou.setId(this.createID());
        Utilizator gasit = this.findByNumePrenume(firstName, lastName);
        if (gasit != null) {
            throw new Exception("Userul pe care doriti sa-l introduceti exista deja!");
        } else {
            comunitate.addUserList(utilizator_nou);
            utilizatorRepository.save(utilizator_nou);
        }
    }

    public void stergeUtilizator(String firstName, String lastName) throws Exception {
        if (firstName.isEmpty())
            throw new Exception("FirstName is empty!");
        if (lastName.isEmpty())
            throw new Exception("LastName is empty!");
        //Ariana Stan
        Utilizator gasit = this.findByNumePrenume(lastName, firstName);
        if (gasit == null) {
            throw new Exception("Nu exista userul cu numele " + firstName + " !!");
        } else {
            this.removeFromListAFriend(gasit);
            comunitate.deleteUserList(gasit);
            utilizatorRepository.delete(gasit.getId());
        }
    }

    public Utilizator findByNumePrenume(String nume, String prenume) {
//
//        Optional<Utilizator> utilizatorGasit = StreamSupport.stream(this.getAllUsers().spliterator(), false)
//                .filter(u -> u.getLast_name().compareTo(nume) == 0 && u.getFirst_name().compareTo(prenume) == 0)
//                .findFirst();
//        return utilizatorGasit.orElse(null);

        for (Utilizator u : this.getAllUsers()) {
            if (u.getFirst_name().compareTo(prenume) == 0 && u.getLast_name().compareTo(nume) == 0)
                return u;
        }
        return null;
    }


    public Utilizator findOneUser(long id) {
        if (utilizatorRepository.findOne(id).isPresent())
            return utilizatorRepository.findOne(id).get();
        return null;
    }

    public Iterable<Utilizator> getAllUsers() {
        return utilizatorRepository.findAll();
    }

    public Long createID() {
        do {
            boolean bool = true;
            Long id = new Random().nextLong();
            if (id < 0) {
                id *= -1;
            }
            for (Utilizator u : utilizatorRepository.findAll()) {
                if (id.equals(u.getId())) {
                    bool = false;
                    break;
                }
            }
            if (bool)
                return id;
        } while (true);
    }

    public void removeFromListAFriend(Utilizator user) {

        utilizatorRepository.findAll()
                .forEach(userr -> {
                            Prietenie ptr = Prietenie.create(userr, user,LocalDateTime.now());
                            Prietenie ptr1 = Prietenie.create(user, userr,LocalDateTime.now());

                            prietenieRepository.delete(ptr1.getId());
                            prietenieRepository.delete(ptr.getId());
                            userr.getFriends().remove(user);
                        }
                );
    }


//    public void adaugaPrietenie(String firstNameUser1, String lastNameUser1, String firstNameUser2, String lastNameUser2) throws Exception {
//        Utilizator gasitUser1 = this.findByNumePrenume(lastNameUser1, firstNameUser1);
//        if (gasitUser1 == null) {
//            throw new Exception("Userul1 nu exista!");
//        }
//        Utilizator gasitUser2 = this.findByNumePrenume(lastNameUser2, firstNameUser2);
//        if (gasitUser2 == null) {
//            throw new Exception("Userul2 nu exista!");
//        }
//
//        Prietenie gasit = this.findOneFriend(gasitUser1.getId(), gasitUser2.getId());
//        if (gasit != null) {
//            throw new Exception("Exista deja prietenia dintre cei 2 utilizatori");
//        }
//
//        Prietenie ptr = Prietenie.create(gasitUser1, gasitUser2);
//        prietenieRepository.save(ptr);
//        this.connectPrietenii();
//
//    }

//    public void stergePrietenie(String firstNameUser1, String lastNameUser1, String firstNameUser2, String lastNameUser2) throws Exception {
//        Utilizator gasitUser1 = this.findByNumePrenume(firstNameUser1, lastNameUser1);
//        if (gasitUser1 == null) {
//            throw new Exception("Userul1 nu exista!");
//        }
//        Utilizator gasitUser2 = this.findByNumePrenume(firstNameUser2, lastNameUser2);
//        if (gasitUser2 == null) {
//            throw new Exception("Userul2 nu exista!");
//        }
//
//        Prietenie ptr1 = Prietenie.create(gasitUser1, gasitUser2);
//        Prietenie ptr2 = Prietenie.create(gasitUser2, gasitUser1);
//        this.removeFromListFriends(gasitUser1, gasitUser2);
//        prietenieRepository.delete(ptr2.getId());
//        if (prietenieRepository.delete(ptr1.getId()).isPresent()) {
//            prietenieRepository.delete(ptr1.getId()).get();
//        }
//    }

    public Prietenie findOneFriend(long idUser1, long idUser2) {
        for (var p : this.getAllFriends()) {
            if (p.getId().getLeft() == idUser1 && p.getId().getRight() == idUser2)
                return p;
        }
        return null;
    }

    public Iterable<Prietenie> getAllFriends() {
        return prietenieRepository.findAll();
    }

    public void connectPrietenii() {
        final Long[] id1 = new Long[1];
        final Long[] id2 = new Long[1];

        this.getAllFriends().forEach(p -> {
                    id1[0] = p.getId().getLeft();
                    id2[0] = p.getId().getRight();
                    Utilizator u1 = this.findOneUser(id1[0]);
                    Utilizator u2 = this.findOneUser(id2[0]);
                    u1.addFriend(u2);
                    utilizatorRepository.update(u1);
                    u2.addFriend(u1);
                    utilizatorRepository.update(u2);
                }
        );
    }

    public void removeFromListFriends(Utilizator u1, Utilizator u2) {
        Utilizator gasitUser1 = this.findByNumePrenume(u1.getLast_name(), u1.getFirst_name());
        Utilizator gasitUser2 = this.findByNumePrenume(u2.getLast_name(), u2.getFirst_name());

        gasitUser1.getFriends().remove(u2);
        gasitUser2.getFriends().remove(u1);
    }

    public Map<Utilizator, LocalDateTime> prieteniiMei(String prenume, String nume, String luna) {
        Utilizator user = findByNumePrenume(nume, prenume);

        Set<Prietenie> friends = new HashSet<>();
        for (var p : prietenieRepository.findAll()) {
            if (p.getId().getLeft().equals(user.getId())) {
                friends.add(p);
            }
        }
        Map<Utilizator, LocalDateTime> map = new HashMap<>();
        for (var p : friends) {
            Utilizator user1 = findOneUser(p.getId().getRight());
            if (p.getDate().getMonth().toString().equals(luna))
                map.put(user1, p.getDate());
        }
        return map;
    }










}
