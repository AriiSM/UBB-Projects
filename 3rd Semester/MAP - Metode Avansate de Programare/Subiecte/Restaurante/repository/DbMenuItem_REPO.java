package com.example.restaurante.repository;

import com.example.restaurante.domain.MenuItem;
import com.example.restaurante.domain.Table;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DbMenuItem_REPO implements RepositoryOptional<Long, MenuItem> {
    private final String url;
    private final String username;
    private final String password;

    public DbMenuItem_REPO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<MenuItem> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        MenuItem menuItem = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, category, item, price, curreny\n" +
                     "\tFROM public.\"MenuItem\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                Long idm = resultSet.getLong("id");
                String category = resultSet.getString("category");
                String item = resultSet.getString("item");
                Float price = resultSet.getFloat("price");
                String curreny = resultSet.getString("curreny");

                menuItem = new MenuItem(idm, category, item, price, curreny);

                return Optional.of(menuItem);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(menuItem);
    }

    @Override
    public Iterable<MenuItem> findAll() {
        Set<MenuItem> menuItems = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"MenuItem\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idm = resultSet.getLong("id");
                String category = resultSet.getString("category");
                String item = resultSet.getString("item");
                Float price = resultSet.getFloat("price");
                String curreny = resultSet.getString("curreny");

                MenuItem menuItem = new MenuItem(idm, category, item, price, curreny);

                menuItems.add(menuItem);
            }
            return menuItems;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    @Override
    public Optional<MenuItem> save(MenuItem entity) {
        return Optional.empty();
    }

    @Override
    public Optional<MenuItem> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<MenuItem> update(MenuItem entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<MenuItem> changeEntities(Map<Long, MenuItem> entities) {
        return null;
    }
}
