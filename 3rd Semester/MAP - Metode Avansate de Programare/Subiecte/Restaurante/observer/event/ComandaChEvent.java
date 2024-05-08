package com.example.restaurante.observer.event;

public class ComandaChEvent implements Event {
    private String id_masa;
    private String data;
    private String numeProduse;

    public ComandaChEvent(String id_masa, String data, String numeProduse) {
        this.id_masa = id_masa;
        this.data = data;
        this.numeProduse = numeProduse;
    }

    public String getId_masa() {
        return id_masa;
    }

    public String getData() {
        return data;
    }

    public String getNumeProduse() {
        return numeProduse;
    }
}
