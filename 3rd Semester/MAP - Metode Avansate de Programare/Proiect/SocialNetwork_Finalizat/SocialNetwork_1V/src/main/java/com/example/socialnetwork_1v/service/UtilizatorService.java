package com.example.socialnetwork_1v.service;//package org.example.service;
//
//import org.example.domain.Comunitate;
//import org.example.domain.Prietenie;
//import org.example.domain.Tuple;
//import org.example.domain.Utilizator;
//import org.example.repository.Repository;
//
//import java.util.List;
//import java.util.Random;
//
//public class UtilizatorService {
//    private final Repository<Long, Utilizator> utilizatorRepository;
//    private final Repository<Tuple<Long, Long>, Prietenie> prietenieRepository;
//
//    Comunitate comunitate = new Comunitate();
//
//    //Constructor
//    public UtilizatorService(Repository<Long, Utilizator> utilizatorRepository, Repository<Tuple<Long, Long>, Prietenie> prietenieRepository) {
//        this.utilizatorRepository = utilizatorRepository;
//        this.prietenieRepository = prietenieRepository;
//        connectPrietenii();
//    }
//
//    /**
//     * Apeleaza metoda sociabil_com din Comunitate
//     *
//     * @return List<Utilizator>
//     */
//    public List<Utilizator> sociabilComunitate() {
//        List<Utilizator> utilizatorList = comunitate.sociabil_com();
//        return utilizatorList;
//    }
//
//    /**
//     * Apeleaza metoda nr_comunitati din Comunitate
//     *
//     * @return int
//     */
//    public int numarComunitati() {
//        return comunitate.nr_comunitati();
//    }
//
//    /**
//     * Se populeaza repo-ul cu date
//     */
//    public void populare() {
//
//        Utilizator u1 = new Utilizator("Ariana", "Stan");
//        u1.setId(1L);
//        Utilizator u2 = new Utilizator("Dana", "Rusu");
//        u2.setId(2L);
//        Utilizator u3 = new Utilizator("Teodor", "Ruse");
//        u3.setId(3L);
//        Utilizator u4 = new Utilizator("Denisa", "Borla");
//        u4.setId(4L);
//        Utilizator u5 = new Utilizator("Alexandra", "Borla");
//        u5.setId(5L);
//        Utilizator u6 = new Utilizator("Madalina", "Negrea");
//        u6.setId(6L);
//        Utilizator u7 = new Utilizator("Oana", "Ungureanu");
//        u7.setId(7L);
//        utilizatorRepository.save(u1);
//        utilizatorRepository.save(u2);
//        utilizatorRepository.save(u3);
//        utilizatorRepository.save(u4);
//        utilizatorRepository.save(u5);
//        utilizatorRepository.save(u6);
//        utilizatorRepository.save(u7);
//
//        comunitate.addUserList(u1);
//        comunitate.addUserList(u2);
//        comunitate.addUserList(u3);
//        comunitate.addUserList(u4);
//        comunitate.addUserList(u5);
//        comunitate.addUserList(u6);
//        comunitate.addUserList(u7);
//
//        try {
//            this.adaugaPrietenie("Dana", "Rusu", "Ariana", "Stan");
//            this.adaugaPrietenie("Oana", "Ungureanu", "Ariana", "Stan");
//            //this.adaugaPrietenie("Alexandra","Borla","Denisa","Borla");
//            //this.adaugaPrietenie("Teodor","Ruse","Oana","Ungureanu");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * Se face un split pe un String
//     *
//     * @param numeSiPrenume: String ul introdus de la tastatura
//     * @return String[]
//     */
//    public String[] desparte(String numeSiPrenume) {
//        String[] list;
//        list = numeSiPrenume.split(" ");
//        return list;
//    }
//
//    /**
//     * Adauga utilizatori in retea si in comunitate
//     *
//     * @param firstName: prenumele persoanei
//     * @param lastName:  numele persoanei
//     * @throws Exception -Daca userul exista deja nu va fi adaugat in lista
//     * @return: Utilizator
//     */
//    public Utilizator addUtilizator(String firstName, String lastName) throws Exception {
//        Utilizator utilizator_nou = new Utilizator(firstName, lastName);
//        utilizator_nou.setId(this.createID());
//        Utilizator gasit = this.findByNumePrenume(firstName, lastName);
//        if (gasit != null) {
//            throw new Exception("Userul pe care doriti sa-l introduceti exista deja!");
//        } else {
//            comunitate.addUserList(utilizator_nou);
//            return utilizatorRepository.save(utilizator_nou);
//        }
//    }
//
//    /**
//     * Se genereaza un id unic pentru un Utilizator nou creat
//     *
//     * @return Long
//     */
//    public Long createID() {
//        do {
//            boolean bool = true;
//            Long id = new Random().nextLong();
//            if (id < 0) {
//                id *= -1;
//            }
//            for (Utilizator u : utilizatorRepository.findAll()) {
//                if (id.equals(u.getId())) {
//                    bool = false;
//                    break;
//                }
//            }
//            if (bool)
//                return id;
//        } while (true);
//    }
//
//    /**
//     * Se cauta un Utilizator in functie de nume si prenume
//     *
//     * @param nume:    numele utilizatorului
//     * @param prenume: prenumele utilizatorului
//     * @return Utilizator sau null daca nu l-a gasit
//     */
//    public Utilizator findByNumePrenume(String nume, String prenume) {
//        for (Utilizator u : this.getAllUsers()) {
//            if (u.getLast_name().compareTo(nume) == 0 && u.getFirst_name().compareTo(prenume) == 0)
//                return u;
//        }
//        return null;
//    }
//
//    /**
//     * Returneaza toti utilizatorii din retea
//     *
//     * @return Iterable<Utilizator>
//     */
//    public Iterable<Utilizator> getAllUsers() {
//        return utilizatorRepository.findAll();
//    }
//
//    /**
//     * Se sterge un utilizator din retea
//     *
//     * @param firstName:    prenume
//     * @param lastName:nume
//     * @return Utilizator
//     * @throws Exception -Daca nu exista un utilizator cu numele si prenumele specificat
//     */
//    public Utilizator stergeUtilizator(String firstName, String lastName) throws Exception {
//        Utilizator gasit = this.findByNumePrenume(firstName, lastName);
//        if (gasit == null) {
//            throw new Exception("Nu exista userul cu numele " + firstName + " !!");
//        } else {
//
//            this.removeFromListAFriend(gasit);
//            comunitate.deleteUserList(gasit);
//            return utilizatorRepository.delete(gasit.getId());
//        }
//    }
//
//    /**
//     * Sterge utilizatorul sters din listele de prieteni ale altor utilizatori daca este cazul
//     *
//     * @param user: userul pe care-l stergem din lista de prieteni a tuturor utilizatorilor
//     */
//    public void removeFromListAFriend(Utilizator user) {
//        for (var userr : utilizatorRepository.findAll()) {
//            Prietenie ptr = Prietenie.create(userr, user);
//            Prietenie ptr1 = Prietenie.create(user, userr);
//
//            prietenieRepository.delete(ptr1.getId());
//            prietenieRepository.delete(ptr.getId());
//            userr.getFriends().remove(user);
//        }
//    }
//
//    /**
//     * Returneaza toate prieteniile din retea
//     *
//     * @return Iterable<Prietenie>
//     */
//    public Iterable<Prietenie> getAllFriends() {
//        return prietenieRepository.findAll();
//    }
//
//    /**
//     * Cauta un utilizator in functie de id-ul lui
//     *
//     * @param id:id ul unic
//     * @return: Utilizator
//     */
//    public Utilizator findOneUser(long id) {
//        return utilizatorRepository.findOne(id);
//    }
//
//    /**
//     * Adaugarea in lista de prieteni
//     */
//    public void connectPrietenii() {
//        Long id1, id2;
//        for (Prietenie p : this.getAllFriends()) {
//            id1 = p.getId().getLeft();
//            id2 = p.getId().getRight();
//            Utilizator u1 = this.findOneUser(id1);
//            Utilizator u2 = this.findOneUser(id2);
//            u1.addFriend(u2);
//            u2.addFriend(u1);
//        }
//    }
//
//    /**
//     * Adauga o prietenie intre 2 utilizatori
//     *
//     * @param firstNameUser1: prenumele primului utilizator
//     * @param lastNameUser1:  numele primului utilizator
//     * @param firstNameUser2: prenume user2
//     * @param lastNameUser2:  nume user2
//     * @throws Exception -in cazul in care userul1 nu exista
//     *                   - in cazul in care userul2 nu exista
//     * @return: Prietenie
//     */
//    public Prietenie adaugaPrietenie(String firstNameUser1, String lastNameUser1, String firstNameUser2, String lastNameUser2) throws Exception {
//        Utilizator gasitUser1 = this.findByNumePrenume(firstNameUser1, lastNameUser1);
//        if (gasitUser1 == null) {
//            throw new Exception("Userul1 nu exista!");
//        }
//        Utilizator gasitUser2 = this.findByNumePrenume(firstNameUser2, lastNameUser2);
//        if (gasitUser2 == null) {
//            throw new Exception("Userul2 nu exista!");
//        }
//
//        Prietenie ptr = Prietenie.create(gasitUser1, gasitUser2);
//        Prietenie task = prietenieRepository.save(ptr);
//        this.connectPrietenii();
//        return task;
//    }
//
//    /**
//     * Sterge prietenie intre 2 useri
//     *
//     * @param firstNameUser1: prenume user1
//     * @param lastNameUser1:  nume user1
//     * @param firstNameUser2: prenume user2
//     * @param lastNameUser2:  nume user2
//     * @throws Exception: -Daca user1 nu exista
//     *                    -Daca user2 nu exista
//     * @return: Prietenie
//     */
//    public Prietenie stergePrietenie(String firstNameUser1, String lastNameUser1, String firstNameUser2, String lastNameUser2) throws Exception {
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
//        return prietenieRepository.delete(ptr1.getId());
//    }
//
//    /**
//     * Il sterge pe u2 din lista de prieteni a lui u1 SI il sterge pe u1 din lista de prieteni a lui u2
//     *
//     * @param u1:User1
//     * @param u2:User2
//     */
//    public void removeFromListFriends(Utilizator u1, Utilizator u2) {
//        Utilizator gasitUser1 = this.findByNumePrenume(u1.getLast_name(), u1.getFirst_name());
//        Utilizator gasitUser2 = this.findByNumePrenume(u2.getLast_name(), u2.getFirst_name());
//
//        gasitUser1.getFriends().remove(u2);
//        gasitUser2.getFriends().remove(u1);
//    }
//
//
//}
