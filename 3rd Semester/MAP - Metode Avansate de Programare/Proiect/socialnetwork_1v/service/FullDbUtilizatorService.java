package com.example.socialnetwork_1v.service;

import com.example.socialnetwork_1v.domain.*;
import com.example.socialnetwork_1v.repository.database.DbLoginRepository;
import com.example.socialnetwork_1v.repository.database.paging.Page;
import com.example.socialnetwork_1v.repository.database.paging.Pageable;
import com.example.socialnetwork_1v.utils.Observable;
import com.example.socialnetwork_1v.utils.Observer;
import com.example.socialnetwork_1v.utils.events.ChangeEventType;
import com.example.socialnetwork_1v.utils.events.EditUtilizatorEvent;
import com.example.socialnetwork_1v.repository.RepositoryOptional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FullDbUtilizatorService implements Observable<EditUtilizatorEvent> {
    private final RepositoryOptional<Long, Utilizator> utilizatorRepository;
    private final RepositoryOptional<Tuple<Long, Long>, Prietenie> prietenieRepository;
    private RepositoryOptional<Long, Message> messageRepositoryOptional;
    private RepositoryOptional<Long, Invitatii> invitatiiRepositoryOptional;
    private DbLoginRepository loginRepository ;


    public FullDbUtilizatorService(RepositoryOptional<Long, Utilizator> utilizatorRepository, RepositoryOptional<Tuple<Long, Long>, Prietenie> prietenieRepository, RepositoryOptional<Long, Message> messageRepositoryOptional, RepositoryOptional<Long, Invitatii> invitatiiRepositoryOptional, DbLoginRepository loginRepository) {
        this.utilizatorRepository = utilizatorRepository;
        this.prietenieRepository = prietenieRepository;
        this.messageRepositoryOptional = messageRepositoryOptional;
        this.invitatiiRepositoryOptional = invitatiiRepositoryOptional;
        this.loginRepository = loginRepository;
    }

    private final List<Observer<EditUtilizatorEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<EditUtilizatorEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<EditUtilizatorEvent> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObserver(EditUtilizatorEvent t) {
        observers.stream().forEach((x -> x.update(t)));
    }

    public Utilizator addUtilizator(String firstName, String lastName) throws Exception {
        Utilizator utilizator_nou = new Utilizator(firstName, lastName);
        utilizator_nou.setId(this.createID());
        Utilizator gasit = this.findByNumePrenume(firstName, lastName);
        if (gasit != null) {
            throw new Exception("Userul pe care doriti sa-l introduceti exista deja!");
        } else {
            utilizatorRepository.save(utilizator_nou);
            notifyObserver(new EditUtilizatorEvent(ChangeEventType.ADD, utilizator_nou));
        }
        return utilizator_nou;
    }

    public Message addMessage(Message m) {
        messageRepositoryOptional.save(m);
        return m;
    }

    public Invitatii addInvitatie(Invitatii i) {
        invitatiiRepositoryOptional.save(i);
        return i;
    }

    public Utilizator updateUtilizator(Utilizator newUser) {
        Optional<Utilizator> oldUtilizator = utilizatorRepository.findOne(newUser.getId());
        if (oldUtilizator != null) {
            Optional<Utilizator> res = utilizatorRepository.update(newUser);
            notifyObserver(new EditUtilizatorEvent(ChangeEventType.UPDATE, newUser, oldUtilizator.get()));
            return res.get();
        }
        return oldUtilizator.get();
    }

    public Invitatii updateInvitatie(Invitatii i) {
        invitatiiRepositoryOptional.update(i);
        return i;
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

    public Utilizator findByNumePrenume(String nume, String prenume) {
        for (Utilizator u : this.getAllUsers()) {
            if (u.getFirst_name().compareTo(prenume) == 0 && u.getLast_name().compareTo(nume) == 0)
                return u;
        }
        return null;
    }

    public Page<Utilizator> findAllOnePage(Pageable pageable) {
        return utilizatorRepository.findAllOnPage(pageable);
    }

    public Iterable<Utilizator> getAllUsers() {
        return utilizatorRepository.findAll();
    }

    public Iterable<Message> getAllMesaje() {
        return messageRepositoryOptional.findAll();
    }

    public Page<Invitatii> findAllOnInvitatiiPage(Pageable pageable) {
        return invitatiiRepositoryOptional.findAllOnPage(pageable);
    }

    public Iterable<Invitatii> getAllInvitatii() {
        return invitatiiRepositoryOptional.findAll();
    }

    public Page<Prietenie> findAllFriendsPage(Pageable pageable) {
        return prietenieRepository.findAllOnPage(pageable);
    }

    public Iterable<Prietenie> getAllFriends() {
        return prietenieRepository.findAll();
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
            utilizatorRepository.delete(gasit.getId());
            for (var user : gasit.getFriends()) {
                utilizatorRepository.update(user);
            }
            notifyObserver(new EditUtilizatorEvent(ChangeEventType.DELETE, gasit));
        }
    }

    public void deleteInvitatie(Invitatii i) {
        invitatiiRepositoryOptional.delete(i.getId());
    }

    public Utilizator findOneUser(long id) {
        if (utilizatorRepository.findOne(id).isPresent())
            return utilizatorRepository.findOne(id).get();
        return null;
    }

    public void adaugaPrietenie(String firstNameUser1, String lastNameUser1, String firstNameUser2, String lastNameUser2, LocalDateTime data) throws Exception {
        Utilizator gasitUser1 = this.findByNumePrenume(lastNameUser1, firstNameUser1);
        if (gasitUser1 == null) {
            throw new Exception("Userul1 nu exista!");
        }
        Utilizator gasitUser2 = this.findByNumePrenume(lastNameUser2, firstNameUser2);
        if (gasitUser2 == null) {
            throw new Exception("Userul2 nu exista!");
        }

        Prietenie gasit = this.findOneFriend(gasitUser1.getId(), gasitUser2.getId());
        if (gasit != null) {
            throw new Exception("Exista deja prietenia dintre cei 2 utilizatori");
        }

        Prietenie ptr = Prietenie.create(gasitUser1, gasitUser2, data);
        prietenieRepository.save(ptr);
        this.connectPrietenii();
    }

    public void stergePrietenie(String firstNameUser1, String lastNameUser1, String firstNameUser2, String lastNameUser2) throws Exception {
        Utilizator gasitUser1 = this.findByNumePrenume(firstNameUser1, lastNameUser1);
        if (gasitUser1 == null) {
            throw new Exception("Userul1 nu exista!");
        }
        Utilizator gasitUser2 = this.findByNumePrenume(firstNameUser2, lastNameUser2);
        if (gasitUser2 == null) {
            throw new Exception("Userul2 nu exista!");
        }

        //Prietenie ptr1 = Prietenie.create(gasitUser1, gasitUser2);
        //Prietenie ptr2 = Prietenie.create(gasitUser2, gasitUser1);
        //this.removeFromListFriends(gasitUser1, gasitUser2);
        //prietenieRepository.delete(ptr2.getId());
        //prietenieRepository.delete(ptr1.getId());
    }

    public Prietenie findOneFriend(long idUser1, long idUser2) {
        for (var p : this.getAllFriends()) {
            if (p.getId().getLeft() == idUser1 && p.getId().getRight() == idUser2)
                return p;
        }
        return null;
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
                    notifyObserver(new EditUtilizatorEvent(ChangeEventType.UPDATE, u1));
                    u2.addFriend(u1);
                    utilizatorRepository.update(u2);
                    notifyObserver(new EditUtilizatorEvent(ChangeEventType.UPDATE, u2));
                }
        );
    }

    public void removeFromListFriends(Utilizator u1, Utilizator u2) {
        Utilizator gasitUser1 = this.findByNumePrenume(u1.getLast_name(), u1.getFirst_name());
        Utilizator gasitUser2 = this.findByNumePrenume(u2.getLast_name(), u2.getFirst_name());

        gasitUser1.getFriends().remove(u2);
        gasitUser2.getFriends().remove(u1);
    }


    public List<Message> afisareConversatii(String firstNameUser1, String lastNameUser1, String firstNameUser2, String lastNameUser2) throws Exception {
        Utilizator gasitUser1 = this.findByNumePrenume(lastNameUser1, firstNameUser1);
        if (gasitUser1 == null) {
            throw new Exception("Userul1 nu exista!");
        }
        Utilizator gasitUser2 = this.findByNumePrenume(lastNameUser2, firstNameUser2);
        if (gasitUser2 == null) {
            throw new Exception("Userul2 nu exista!");
        }

        // mesajele de la user1 la user2
        Predicate<Message> fromUser1 = x -> x.getFrom().getId().toString().equals(gasitUser1.getId().toString());
        Predicate<Message> toUser2 = x -> x.getTo().contains(gasitUser2);

        List<Message> messagesUser1 = StreamSupport.stream(messageRepositoryOptional.findAll().spliterator(), false)
                .filter(fromUser1)
                .filter(toUser2)
                .collect(Collectors.toList());


        // mesajele de la user2 la user1
        Predicate<Message> fromUser2 = x -> x.getFrom().getId().toString().equals(gasitUser2.getId().toString());
        Predicate<Message> toUser1 = x -> x.getTo().contains(gasitUser1);
        List<Message> messagesUser2 = StreamSupport.stream(messageRepositoryOptional.findAll().spliterator(), false)
                .filter(fromUser2)
                .filter(toUser1)
                .collect(Collectors.toList());

        //sortam mesajele de la ambii utilizatori in fucntie de data
        List<Message> sortedList = Stream.concat(messagesUser1.stream(), messagesUser2.stream())
                .sorted(Comparator.comparing(Message::getDate))
                .collect(Collectors.toList());

        System.out.println(sortedList.toString());

        return messagesUser1;

    }

    private List<Utilizator> useri = new ArrayList<>();

    public void sendUseri(Utilizator user1, Utilizator user2) {
        useri.clear();
        useri.add(user1);
        useri.add(user2);

    }


    public Optional<String[]> findUsernamePass(String username, String parola) throws Exception {
        return loginRepository.findOne(username,parola);
    }

    public Optional saveCont(Long utilizator,String username, String parola) throws Exception {
        Login l = new Login(utilizator,username,parola);
        return loginRepository.save(l);
    }

}
