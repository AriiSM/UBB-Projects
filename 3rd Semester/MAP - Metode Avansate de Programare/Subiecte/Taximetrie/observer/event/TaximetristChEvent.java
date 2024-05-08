package com.example.taximetrie.observer.event;


public class TaximetristChEvent implements Event {
    private String numeClient;
    private String locatie;


    private Long clientId;
    private Long soferid;
    private String raspuns;
    private String indicator_masina;

    //Ce trimite Taximetristul spre Client
    public TaximetristChEvent(Long clientId, String numeClient, String locatie) {
        this.clientId = clientId;
        this.numeClient = numeClient;
        this.locatie = locatie;
    }

    public TaximetristChEvent(Long soferid, String raspuns, String locatie, String numeClient) {
        this.soferid = soferid;
        this.raspuns = raspuns;
        this.locatie = locatie;
        this.numeClient = numeClient;
    }

    public Long getSoferid() {
        return soferid;
    }

    public String getIndicator_masina() {
        return indicator_masina;
    }

    public String getNumeClient() {
        return numeClient;
    }

    public String getLocatie() {
        return locatie;
    }

    public Long getClientId() {
        return clientId;
    }

    public String getRaspuns() {
        return raspuns;
    }
}
