package com.example.socialnetwork_1v.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comunitate {
    private static short NUMBERS = 1;
    private final List<Utilizator> userlist = new ArrayList<>();
    private static int numarComunitati = 0;
    Map<Long, Integer> vizitat = new HashMap<>();

    public Comunitate() {

    }

    /**
     * Adauga un utilizator in comunitate
     *
     * @param utilizator: Utilizatorul care o sa fie adaugat in lista
     */
    public void addUserList(Utilizator utilizator) {
        this.userlist.add(utilizator);
    }

    /**
     * Sterge un utilizator din comunitate
     *
     * @param utilizator: Utilizatorul care o sa fie sters din lista
     */
    public void deleteUserList(Utilizator utilizator) {
        if (userlist.contains(utilizator)) {
            this.userlist.remove(utilizator);
        }
    }

    @Override
    public String toString() {
        final String[] Nume = new String[1];
        List<String> ListaNume = new ArrayList<>();

        userlist.forEach(user -> {
            Nume[0] = user.getLast_name() + "," + user.getFirst_name();
            ListaNume.add(Nume[0]);
        });

//        for (Utilizator user : userlist) {
//            Nume[0] = user.getFirstName() + "," + user.getLastName();
//            ListaNume.add(Nume[0]);
//        }
        return "Comunitate{" +
                "userlist=" + ListaNume +
                ", numarComunitati=" + numarComunitati +
                '}';
    }

    /**
     * Intr-un MAP se stocheaza id-ul utilizatorului si se marcheaza cu 0 ca si cum ar fi vizitat
     */
    public void initializare_vizitat() {
        userlist.forEach(user -> vizitat.put(user.getId(), 0));

//        for (Utilizator user : userlist) {
//            vizitat.put(user.getId(), 0);
//        }
    }

    /**
     * dfs -Depth-First Search - Parcurgere in adancime
     *
     * @param user:
     * @param comunitate:
     */
    public void dfs(Utilizator user, int comunitate) {
        vizitat.put(user.getId(), comunitate);

        userlist.stream()
                .filter(u -> vizitat.get(u.getId()) == 0 && u.isFriend(user))
                .forEach(u -> {
                    dfs(u, comunitate);
                    vizitat.put(u.getId(), comunitate);
                });
//        for (Utilizator u : userlist) {
//            if (vizitat.get(u.getId()) == 0 && u.isFriend(user)) {
//                dfs(u, comunitate);
//                vizitat.put(u.getId(), comunitate);
//            }
//        }
    }

    /**
     * Numara componentele conexe
     *
     * @return: int numarul de componente
     */
    public int nr_comunitati() {
        initializare_vizitat();
        numarComunitati = 0;

        userlist
                .stream()
                .filter(user -> vizitat.get(user.getId()) == 0)
                .forEach(user -> {
                    dfs(user, numarComunitati + 1);
                    numarComunitati++;
                });
        return numarComunitati;
//        for (Utilizator user : userlist) {
//            if (vizitat.get(user.getId()) == 0) {
//                dfs(user, numarComunitati + 1);
//                //if(user.getFriend()!=null
//                numarComunitati++;
//            }
//        }
//        return numarComunitati;
    }

    /**
     * Cauta cea mai sociabila componenta conexa
     *
     * @return List<Utilizator>
     **/
    public List<Utilizator> sociabil_com() {
        numarComunitati = nr_comunitati();
        List<Utilizator> useri_comunitate = new ArrayList<>();
        final short[] count = {0};
        int id_comunitate = 0;

        for (int i = 1; i <= numarComunitati; i++) {


            for (Utilizator user : userlist) {
                if (vizitat.get(user.getId()) == i)
                    count[0]++;
            }
            if (count[0] > NUMBERS) {
                NUMBERS = count[0];
                id_comunitate = i;
            }
            count[0] = 0;
        }
        for (Utilizator user : userlist) {
            if (vizitat.get(user.getId()) == id_comunitate) {
                useri_comunitate.add(user);
            }
        }
        return useri_comunitate;
    }
}