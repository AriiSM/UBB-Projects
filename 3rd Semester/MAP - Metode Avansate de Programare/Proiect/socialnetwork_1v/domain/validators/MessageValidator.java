package com.example.socialnetwork_1v.domain.validators;

import com.example.socialnetwork_1v.domain.Message;
import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.exceptii.ValidationException;
import com.example.socialnetwork_1v.repository.RepositoryOptional;

public class MessageValidator implements Validator<Message>{
    private RepositoryOptional<Long, Utilizator> repoUser;

    public MessageValidator(RepositoryOptional<Long, Utilizator> repoUser) {
        this.repoUser = repoUser;
    }

    @Override
    public void validate(Message entity) throws ValidationException {
        if(repoUser.findOne(entity.getFrom().getId()).isEmpty()){
            throw new ValidationException("Nu exista userFrom la mesaj!");
        }
        if(entity.getTo().isEmpty()){
            throw  new ValidationException("Nu exista userTo la mesaj!");
        }
        if(entity.getDate()==null){
            throw new ValidationException("Nu exista data la mesaj!");
        }
        if(entity.getMesaj().isEmpty()){
            throw new ValidationException("Nu exista mesaj la mesaj!");
        }
    }
}
