package com.example.restaurante.service;

import com.example.restaurante.domain.MenuItem;
import com.example.restaurante.domain.Order;
import com.example.restaurante.domain.Table;
import com.example.restaurante.observer.Observable;
import com.example.restaurante.observer.Observer;
import com.example.restaurante.observer.event.Event;
import com.example.restaurante.repository.DbMenuItem_REPO;
import com.example.restaurante.repository.DbOrder_REPO;
import com.example.restaurante.repository.DbTable_REPO;

import java.util.ArrayList;
import java.util.List;

public class Service implements Observable<Event> {
    private DbTable_REPO table_repo;
    private DbMenuItem_REPO menuItem_repo;
    private DbOrder_REPO order_repo;
    private List<Observer<Event>> observers = new ArrayList<>();

    public Service(DbTable_REPO table_repo, DbMenuItem_REPO menuItem_repo, DbOrder_REPO order_repo) {
        this.table_repo = table_repo;
        this.menuItem_repo = menuItem_repo;
        this.order_repo = order_repo;
    }

    public Iterable<Table> getTable() {
        return table_repo.findAll();
    }

    public MenuItem getMenuItemByName(String name) {
        for (MenuItem menuItem : this.getMenuItemList()) {
            if (menuItem.getItem().equals(name)) {
                return menuItem;
            }
        }
        return null;
    }

    public Iterable<MenuItem> getMenuItemList() {
        return menuItem_repo.findAll();
    }

    public void saveOrder(Order newOrder){
        order_repo.save(newOrder);
    }
    public Iterable<Order>getOrderList(){
        return order_repo.findAll();
    }

    @Override
    public void addObserver(Observer<Event> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<Event> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObserver(Event t) {
        observers.forEach(o -> o.update(t));
    }
}
