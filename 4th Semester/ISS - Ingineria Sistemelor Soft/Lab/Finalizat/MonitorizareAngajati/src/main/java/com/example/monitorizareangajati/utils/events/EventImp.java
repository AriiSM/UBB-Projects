package com.example.monitorizareangajati.utils.events;

import com.example.monitorizareangajati.domain.Angajat;
import com.example.monitorizareangajati.domain.AtribuireSarcina;
import com.example.monitorizareangajati.domain.Sarcina;

public class EventImp implements Event {
    private EventType type;
    private Angajat angajat;
    private AtribuireSarcina atribuireSarcina;

    public EventImp(EventType type, Angajat angajat) {
        this.type = type;
        this.angajat = angajat;
    }

    public EventImp(EventType type, AtribuireSarcina atribuireSarcina) {
        this.type = type;
        this.atribuireSarcina = atribuireSarcina;
    }
}
