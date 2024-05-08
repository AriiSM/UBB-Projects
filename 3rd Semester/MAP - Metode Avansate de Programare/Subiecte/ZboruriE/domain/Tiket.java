package com.example.zboruri.domain;

import java.time.LocalDateTime;

public class Tiket extends Entity<Long>{
    private String username;
    private LocalDateTime purchaseTime;
    private Long id_flight;

    public Tiket(Long aLong, String username, LocalDateTime purchaseTime, Long id_flight) {
        super(aLong);
        this.username = username;
        this.purchaseTime = purchaseTime;
        this.id_flight = id_flight;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    public Long getId_flight() {
        return id_flight;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    @Override
    public String toString() {
        return "Tiket{" +
                "username='" + username + '\'' +
                ", purchaseTime=" + purchaseTime +
                '}';
    }
}
