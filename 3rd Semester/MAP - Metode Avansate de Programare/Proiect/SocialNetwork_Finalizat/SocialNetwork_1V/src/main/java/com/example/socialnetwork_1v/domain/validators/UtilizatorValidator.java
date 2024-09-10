package com.example.socialnetwork_1v.domain.validators;

import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.exceptii.ValidationException;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        String errors = "";
        if (entity.getLast_name().isEmpty())
            errors += "[FirstName is empty]";

        if (entity.getFirst_name().isEmpty())
            errors += "[LastName is empty]";

        if (entity.getId() < 0)
            errors += "[Id is negative]";

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
