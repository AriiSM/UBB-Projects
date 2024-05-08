package com.example.restaurante.repository;

import com.example.restaurante.domain.MenuItem;
import com.example.restaurante.domain.Order;
import com.example.restaurante.domain.Status;
import com.example.restaurante.domain.Table;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DbOrder_REPO implements RepositoryOptional<Long, Order> {
    private final String url;
    private final String username;
    private final String password;
    private DbMenuItem_REPO menuItem_repo;

    public DbOrder_REPO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        menuItem_repo = new DbMenuItem_REPO(url, username, password);
    }

    @Override
    public Optional<Order> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Order order = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, id_table, \"meniuList\", data, status\n" +
                     "\tFROM public.\"Order\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                Long ido = resultSet.getLong("id");
                Long idt = resultSet.getLong("id_table");
                String meniuList = resultSet.getString("meniuList");
                LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();
                String status = resultSet.getString("status");

                String[] meniu = meniuList.split(", ");
                List<MenuItem> Lista = new ArrayList<>();
                for (String m : meniu) {
                    MenuItem menuItem = menuItem_repo.findOne(Long.valueOf(m)).get();
                    Lista.add(menuItem);
                }

                if (status.equals("PLACED"))
                    order = new Order(ido, idt, Lista, data, Status.PLACED);
                if (status.equals("PREPARING"))
                    order = new Order(ido, idt, Lista, data, Status.PREPARING);
                if (status.equals("SERVED"))
                    order = new Order(ido, idt, Lista, data, Status.SERVED);

                return Optional.of(order);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(order);
    }

    @Override
    public Iterable<Order> findAll() {
        Set<Order> orders = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Order\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long ido = resultSet.getLong("id");
                Long idt = resultSet.getLong("id_table");
                String meniuList = resultSet.getString("meniuList");
                LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();
                String status = resultSet.getString("status");

                String[] meniu = meniuList.split(", ");
                List<MenuItem> Lista = new ArrayList<>();
                for (String m : meniu) {
                    MenuItem menuItem = menuItem_repo.findOne(Long.valueOf(m)).get();
                    Lista.add(menuItem);
                }

                Order order = null;
                if (status.equals("PLACED"))
                    order = new Order(ido, idt, Lista, data, Status.PLACED);
                if (status.equals("PREPARING"))
                    order = new Order(ido, idt, Lista, data, Status.PREPARING);
                if (status.equals("SERVED"))
                    order = new Order(ido, idt, Lista, data, Status.SERVED);


                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public Optional<Order> save(Order entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entitatea nu poate fi nula!");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection
                     .prepareStatement("INSERT INTO public.\"Order\"(\n" +
                             "\tid_table, \"meniuList\", data, status)\n" +
                             "\tVALUES (?, ?, ?, ?)")) {


            statement.setLong(1, entity.getId_table());

            List<MenuItem> lista = entity.getMenuItemList();
            StringBuilder sb = new StringBuilder();

            for (MenuItem item : lista) {
                sb.append(item.getId()).append(", ");
            }

            statement.setString(2, sb.toString());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getData()));
            statement.setString(4, entity.getStatus().toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Order> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Order> update(Order entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Order> changeEntities(Map<Long, Order> entities) {
        return null;
    }
}
