package com.example.zboruri.observer.event;

public class FlightChEvent implements Event {
    private int nr_locuri;
    private String id_client;
    private Long id_zbor;

    public FlightChEvent(String id_client, int nr_locuri, Long id_zbor) {

        this.id_client = id_client;
        this.nr_locuri = nr_locuri;
        this.id_zbor = id_zbor;
    }

    public String getId_client() {
        return id_client;
    }

    public int getNr_locuri() {
        return nr_locuri;
    }

    public Long getId_zbor() {
        return id_zbor;
    }
}
