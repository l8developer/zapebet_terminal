package com.bet.mpos.objects;

public class ErrorContainer {
    private boolean error;
    private String message;

    public ErrorContainer(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Error{" +
                "error=" + error +
                ", message='" + message + '\'' +
                '}';
    }
}
