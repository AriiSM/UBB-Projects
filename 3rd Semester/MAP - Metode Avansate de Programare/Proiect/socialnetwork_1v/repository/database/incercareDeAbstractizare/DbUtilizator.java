package com.example.socialnetwork_1v.repository.database.incercareDeAbstractizare;

import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.domain.validators.Validator;
import com.example.socialnetwork_1v.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DbUtilizator extends AbstractDbRepository<Long, Utilizator>{
    public DbUtilizator(String url, String username, String password,Validator<Utilizator> validator) {
        super(url, username, password,validator);
    }

    @Override
    public Utilizator extractEntity(ResultSet rs) {
        Utilizator utilizator = null;
        try{
            Long id = rs.getLong("id");
            String prenume = rs.getString("first_name");
            String nume = rs.getString("last_name");


            utilizator=new Utilizator(nume,prenume);
            utilizator.setId(id);

            String friends = rs.getString("friends");
            if (!Objects.equals(friends, "0")) {
                List<String> list = List.of(friends.split(", "));
                for (var f : list) {
                    long id1 = Long.parseLong(f);
                    Optional<Utilizator> findthat = findOne(id1);
                    if(findthat.isPresent()){
                        utilizator.addFriend(findthat.get());
                    }

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilizator;
    }

    @Override
    public String abstractSelect() {
        return "SELECT * FROM public.\"Utilizatori\"";
    }

    @Override
    public String abstractInsert() {
        return "INSERT INTO public.\"Utilizatori\" VALUES(?,?,?,?)";
    }

    @Override
    public void abstractInsertParameters(PreparedStatement stmt, Utilizator entity) {
        try{
            stmt.setLong(1,entity.getId());
            stmt.setString(2,entity.getFirst_name());
            stmt.setString(3,entity.getLast_name());

            if(!entity.getFriends().isEmpty()){
                List<Long> ids = new ArrayList<>();
                for (var i : entity.getFriends()) {
                    ids.add(i.getId());
                }
                String lili = ids.toString();
                stmt.setString(4, lili.substring(1, lili.length() - 1));
            }
            else{
                stmt.setString(4, "0");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String abstractDelete(Long id) {
        return "DELETE FROM public.\"Utilizatori\" WHERE id='" + id.toString() + "'";
    }

    @Override
    public String abstractUpdate() {
        return "";
    }

    @Override
    public void abstractUpdateParameters(PreparedStatement stmt, Utilizator entity) {
        //TODO
    }
}
