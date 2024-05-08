package com.example.socialnetwork_1v.exceptii;

public class PrietenieException extends Exception {
    private final String error;

    public PrietenieException(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "FriendError{" +
                "error=" + error + '\'' +
                "}";
    }
}
