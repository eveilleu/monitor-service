package ca.randomname.monitor.rest.model;

import java.io.Serializable;

public class StopMonitoringRequest implements Serializable {

    private String hostname;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public String toString() {
        return "StopMonitoringRequest{" +
                "hostname='" + hostname + '\'' +
                '}';
    }
}
