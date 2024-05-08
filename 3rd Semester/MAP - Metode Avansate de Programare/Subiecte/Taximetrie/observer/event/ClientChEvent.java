package com.example.taximetrie.observer.event;

public class ClientChEvent implements Event{

    //ce trimite Clientul spre taximetrist
    private String timpMaxim;
    private String indicativMasina;
    private Long clientId;

    public ClientChEvent(String timpMaxim, String indicativMasina, Long clientId) {
        this.timpMaxim = timpMaxim;
        this.indicativMasina = indicativMasina;
        this.clientId = clientId;
    }

    public String getTimpMaxim() {
        return timpMaxim;
    }

    public String getIndicativMasina() {
        return indicativMasina;
    }

    public Long getClientId() {
        return clientId;
    }
}
