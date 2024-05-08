package com.example.taximetrie.domain;

import java.security.Timestamp;
import java.time.LocalDateTime;

public class Comanda extends Entity<Long>{
    private Long id_persoana;
    private Long id_sofer;
    private LocalDateTime data;

    public Comanda(Long aLong, Long id_persoana, Long id_sofer, LocalDateTime data) {
        super(aLong);
        this.id_persoana = id_persoana;
        this.id_sofer = id_sofer;
        this.data = data;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    public Long getId_persoana() {
        return id_persoana;
    }

    public Long getId_sofer() {
        return id_sofer;
    }

    public LocalDateTime getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "id_persoana=" + id_persoana +
                ", id_sofer=" + id_sofer +
                ", data=" + data +
                '}';
    }
}
