package com.example.restaurante.domain;

public class Table extends Entity<Long>{
    private Integer nr ;

    public Table(Long aLong, Integer nr) {
        super(aLong);
        this.nr = nr;
    }

    public Integer getNr() {
        return nr;
    }

    @Override
    public Long getId() {
        return super.getId();
    }
}
