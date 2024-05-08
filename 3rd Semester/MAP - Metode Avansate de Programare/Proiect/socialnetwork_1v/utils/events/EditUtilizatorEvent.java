package com.example.socialnetwork_1v.utils.events;

import com.example.socialnetwork_1v.domain.Utilizator;

public class EditUtilizatorEvent implements Event {
    private ChangeEventType type;
    private Utilizator user, oldUser;

    public EditUtilizatorEvent(ChangeEventType type, Utilizator utilizator) {
        this.type = type;
        this.user = utilizator;
    }

    public EditUtilizatorEvent(ChangeEventType type, Utilizator utilizator, Utilizator oldUtilizator) {
        this.type = type;
        this.user = utilizator;
        this.oldUser = oldUtilizator;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Utilizator getUtilizator() {
        return user;
    }

    public Utilizator getOldUtilizator() {
        return oldUser;
    }

}
