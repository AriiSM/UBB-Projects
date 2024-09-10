package mpp.Exceptions;

import java.sql.SQLException;
import java.util.ArrayList;

public class GenericException extends Exception{
    private ArrayList<String> errors = new ArrayList<>();
    private ExceptionType errorExceptionType;

    public GenericException(ExceptionType errorExceptionType, ArrayList<String> error) {
        this.errors = error;
        this.errorExceptionType = errorExceptionType;
    }

    public GenericException(ExceptionType errorExceptionType, String error) {
        this.errors = new ArrayList<>();
        this.errors.add(error);
        this.errorExceptionType = errorExceptionType;
    }

    public GenericException(SQLException e) {
    }


    public ArrayList<String> getError(){
        return errors;
    }

    public ExceptionType getErrorType() {
        return errorExceptionType;
    }

    @Override
    public String toString() {
        return "GenericException{" +
                "errorType=" + errorExceptionType +
                ", errors=" + errors +
                '}';
    }
}
