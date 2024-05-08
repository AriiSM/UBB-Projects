package com.example.socialnetwork_1v.repository.database;

//import com.example.socialnetwork_1v.criptare.Criptare;

import com.example.socialnetwork_1v.criptare.AESEncryptionDecryptionExample;
import com.example.socialnetwork_1v.domain.Login;

import java.sql.*;
import java.util.Objects;
import java.util.Optional;

public class DbLoginRepository {

    private final String urlT;
    private final String usernameT;
    private final String passwordT;

    public DbLoginRepository(String url, String username, String password) {
        this.urlT = url;
        this.usernameT = username;
        this.passwordT = password;
    }


    public Optional<String[]> findOne(String username, String password) throws Exception {
        String[] combo = new String[0];

        AESEncryptionDecryptionExample encryptionDecryptionExample = new AESEncryptionDecryptionExample();
        String crypted = encryptionDecryptionExample.encrypt(password);


        if (username == null || password == null) {
            throw new IllegalArgumentException("Trebuie sa introduceti un username si o parola");
        }
        try (Connection connection = DriverManager.getConnection(urlT, usernameT, passwordT);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Login\" WHERE username = '" + username + "' AND parola = '" + crypted + "'");
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                Long cod = resultSet.getLong("cod");
                Long utilizator = resultSet.getLong("utilizator");
                String usernameT = resultSet.getString("username");

                String parolaT = resultSet.getString("parola");
                combo = new String[]{usernameT + " " + utilizator};
                return Optional.of(combo);
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public Optional save(Login entity) throws Exception {
        if (entity == null) {
            throw new IllegalArgumentException("Entitatea nu exista");
        }

        try (
                Connection connection = DriverManager.getConnection(urlT, usernameT, passwordT);
                PreparedStatement statement = connection.prepareStatement("INSERT INTO public.\"Login\"(utilizator,username,parola) VALUES (?,?,?)")) {

            statement.setLong(1, entity.getUtilizator());
            statement.setString(2, entity.getUsername());

            AESEncryptionDecryptionExample parolaCriptata = new AESEncryptionDecryptionExample();
            String encryptedText = parolaCriptata.encrypt(entity.getParola());

            statement.setString(3, encryptedText);

            statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(entity);
    }


}
