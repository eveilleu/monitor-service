package ca.randomname.monitor.rest.model;

import java.io.Serializable;

public class ApiError implements Serializable {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
