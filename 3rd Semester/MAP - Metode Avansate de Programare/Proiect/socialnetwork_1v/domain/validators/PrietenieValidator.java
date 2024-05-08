package com.example.socialnetwork_1v.domain.validators;

import com.example.socialnetwork_1v.domain.Prietenie;
import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.exceptii.ValidationException;
import com.example.socialnetwork_1v.repository.RepositoryOptional;

public class PrietenieValidator implements Validator<Prietenie> {
    private final RepositoryOptional<Long, Utilizator> repo;

    public PrietenieValidator(RepositoryOptional<Long, Utilizator> repo) {
        this.repo = repo;
    }

    @Override
    public void validate(Prietenie entity) throws ValidationException {
        //verificam daca useri din prietenie exista deja
        if (repo.findOne(entity.getId().getRight()).isEmpty()) {
            throw new ValidationException("Eroare citire user cu id:" + entity.getId().getRight());
        }

        if (repo.findOne(entity.getId().getLeft()).isEmpty()) {
            throw new ValidationException("Eroare citire user cu id:" + entity.getId().getLeft());
        }

        if (entity.getId().getLeft().toString().equals((entity.getId().getRight().toString()))) {
            throw new ValidationException("Nu poti sa fii prieten cu tine!");
        }

        if (entity.getId().getLeft() == null) {
            throw new ValidationException("Left is null");
        }

        if (entity.getId().getRight() == null) {
            throw new ValidationException("Right is null");
        }

        //verificam daca exista useri unei prietenii
        if (repo.findOne(entity.getId().getRight()).isEmpty())
            throw new ValidationException("Userul cu id-ul " + entity.getId().getRight() + " nu exista!");

        if (repo.findOne(entity.getId().getLeft()).isEmpty())
            throw new ValidationException("Userul cu id-ul " + entity.getId().getLeft() + " nu exista!");
    }
}
