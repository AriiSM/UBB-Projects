package com.example.socialnetwork_1v.repository.database.incercareDeAbstractizare;

import com.example.socialnetwork_1v.domain.Prietenie;
import com.example.socialnetwork_1v.domain.Tuple;
import com.example.socialnetwork_1v.domain.validators.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DbPrietenie extends AbstractDbRepository<Tuple<Long,Long>, Prietenie> {
    public DbPrietenie(String url, String username, String password,Validator<Prietenie> validator) {
        super(url, username, password,validator);
    }

    @Override
    public Prietenie extractEntity(ResultSet rs) {
        Prietenie prietenie = null;
        try{
            Long id_left = rs.getLong("id_utilizator");
            Long id_right = rs.getLong("id_right");
            Timestamp data = rs.getTimestamp("data_prietenie");
            prietenie = new Prietenie(id_left,id_right,data.toLocalDateTime());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prietenie;
    }

    @Override
    public String abstractSelect() {
        return "SELECT * FROM public.\"Prieteni\"";
    }

    @Override
    public String abstractInsert() {
        return "INSERT INTO public.\"Prieteni\"(id_utilizator,id_right,data_prietenie) values(?,?,?)";
    }

    @Override
    public void abstractInsertParameters(PreparedStatement stmt, Prietenie entity) {
        try{
            stmt.setLong(1,entity.getId().getLeft());
            stmt.setLong(2,entity.getId().getRight());
            Timestamp timestamp = Timestamp.valueOf(entity.getDate());
            stmt.setTimestamp(3, timestamp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String abstractDelete(Tuple<Long, Long> id) {
        return "DELETE FROM public.\"Prieteni\" WHERE id_utilizator='" + id.getLeft().toString() + "'and id_right'" + id.getRight().toString() + "'";
    }

    @Override
    public String abstractUpdate() {
        return "";
    }

    @Override
    public void abstractUpdateParameters(PreparedStatement stmt, Prietenie entity) {
        //TODO
    }
}
