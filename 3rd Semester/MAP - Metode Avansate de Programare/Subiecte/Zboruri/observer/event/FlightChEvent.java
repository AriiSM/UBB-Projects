package com.example.zboruri.observer.event;

public class FlightChEvent implements Event {
    private int nr_locuri;
    private Long id_zbor;

    public FlightChEvent(int nr_locuri, Long id_zbor) {
        this.nr_locuri = nr_locuri;
        this.id_zbor = id_zbor;
    }

    public int getNr_locuri() {
        return nr_locuri;
    }

    public Long getId_zbor() {
        return id_zbor;
    }
}
