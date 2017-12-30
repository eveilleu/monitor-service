package ca.randomname.monitor.rest.model;

import java.io.Serializable;

public class StartMonitoringRequest implements Serializable {

    private String hostname;
    private long interval;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        return "StartMonitoringRequest{" +
                "hostname='" + hostname + '\'' +
                ", interval=" + interval +
                '}';
    }
}
