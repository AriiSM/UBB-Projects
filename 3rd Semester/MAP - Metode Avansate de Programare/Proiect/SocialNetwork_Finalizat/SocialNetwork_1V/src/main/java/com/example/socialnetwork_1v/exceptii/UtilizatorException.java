package com.example.socialnetwork_1v.exceptii;

public class UtilizatorException extends Exception {
    private final String error;

    public UtilizatorException(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public String toString() {
        return "UserError{" +
                "error=" + error + '\'' +
                "}";
    }
}
