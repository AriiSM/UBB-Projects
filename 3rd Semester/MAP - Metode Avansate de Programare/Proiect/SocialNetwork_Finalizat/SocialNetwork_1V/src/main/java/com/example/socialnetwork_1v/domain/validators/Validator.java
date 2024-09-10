package com.example.socialnetwork_1v.domain.validators;

import com.example.socialnetwork_1v.exceptii.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
