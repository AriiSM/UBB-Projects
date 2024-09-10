package com.example.monitorizareangajati.domain;

public class AtribuireSarcina extends Entity<Integer>{
    private Angajat angajat;
    private Sarcina sarcina;
    private StatusSarcina status;

    public AtribuireSarcina(Angajat angajat, Sarcina sarcina, StatusSarcina status) {
        this.angajat = angajat;
        this.sarcina = sarcina;
        this.status = status;
    }


    @Override
    public Integer getId() {
        return super.getId();
    }

    public Angajat getAngajat() {
        return angajat;
    }

    public Sarcina getSarcina() {
        return sarcina;
    }

    public StatusSarcina getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "AtribuireSarcina{" +
                "angajat=" + angajat +
                ", sarcina=" + sarcina +
                ", status=" + status +
                '}';
    }
}
