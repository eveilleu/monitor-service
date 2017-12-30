package ca.randomname.monitor.api;

import org.springframework.core.NestedRuntimeException;

public class MonitorServiceException extends NestedRuntimeException {

    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public MonitorServiceException(String message) {
        super(message);
    }

    public MonitorServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MonitorServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public MonitorServiceException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

}
