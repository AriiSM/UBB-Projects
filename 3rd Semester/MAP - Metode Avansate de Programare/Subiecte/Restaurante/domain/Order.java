package com.example.restaurante.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order extends Entity<Long>{
    private Long id_table;
    private List<MenuItem> menuItemList;
    private LocalDateTime data;
    private Status status;

    public Order(Long aLong, Long id_table, List<MenuItem> menuItemList, LocalDateTime data, Status status) {
        super(aLong);
        this.id_table = id_table;
        this.menuItemList = menuItemList;
        this.data = data;
        this.status = status;
    }


    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    public Long getId_table() {
        return id_table;
    }

    public List<MenuItem> getMenuItemList() {
        return menuItemList;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Status getStatus() {
        return status;
    }
}
