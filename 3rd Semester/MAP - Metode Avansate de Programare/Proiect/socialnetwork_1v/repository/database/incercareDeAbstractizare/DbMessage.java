package com.example.socialnetwork_1v.repository.database.incercareDeAbstractizare;

import com.example.socialnetwork_1v.domain.Message;
import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.domain.validators.UtilizatorValidator;
import com.example.socialnetwork_1v.domain.validators.Validator;
import com.example.socialnetwork_1v.repository.RepositoryOptional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class DbMessage extends AbstractDbRepository<Long, Message> {
    public RepositoryOptional<Long, Utilizator> utilizatorRepositoryOptional;

    public DbMessage(String url, String username, String password, Validator<Message> validator) {
        super(url, username, password, validator);
    }

    @Override
    public Message extractEntity(ResultSet rs) {
        utilizatorRepositoryOptional = new DbUtilizator(url, username, password, new UtilizatorValidator());
        Message message = null;
        try {
            Long id = rs.getLong("id");
            Long fromUserID = rs.getLong("from");
            String toUsers = rs.getString("to");
            String mesaj = rs.getString("mesaj");
            Timestamp data = rs.getTimestamp("data");
            Long replyMsg = rs.getLong("id_reply");

            String[] toUserOneByOne = toUsers.split(",");
            List<Utilizator> all = new ArrayList<>();
            for (var user : toUserOneByOne) {
                all.add(utilizatorRepositoryOptional.findOne(Long.parseLong(user)).get());
            }
            message = new Message(id, mesaj, utilizatorRepositoryOptional.findOne(fromUserID).get(), all, data.toLocalDateTime(), Optional.ofNullable(replyMsg));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public String abstractSelect() {
        return "SELECT * FROM public.\"Message\"";
    }

    @Override
    public String abstractInsert() {
        return null;
    }

    @Override
    public String abstractDelete(Long aLong) {
        return null;
    }

    @Override
    public void abstractInsertParameters(PreparedStatement stmt, Message entity) {

    }

    @Override
    public String abstractUpdate() {
        return null;
    }

    @Override
    public void abstractUpdateParameters(PreparedStatement stmt, Message entity) {

    }
}
