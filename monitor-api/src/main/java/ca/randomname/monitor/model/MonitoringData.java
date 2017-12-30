package ca.randomname.monitor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MonitoringData implements Serializable {

    private String hostname;

    private List<TimePeriod> timePeriods;

    // Internal use only, a pointer to the latest time period.
    @JsonIgnore
    private TimePeriod currentTimePeriod;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public List<TimePeriod> getTimePeriods() {
        return timePeriods;
    }

    public void setTimePeriods(List<TimePeriod> timePeriods) {
        this.timePeriods = timePeriods;
    }

    public TimePeriod getCurrentTimePeriod() {
        return currentTimePeriod;
    }

    public void setCurrentTimePeriod(TimePeriod currentTimePeriod) {
        this.currentTimePeriod = currentTimePeriod;
    }
}
