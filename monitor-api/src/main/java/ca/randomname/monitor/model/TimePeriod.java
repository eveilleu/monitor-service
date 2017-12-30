package ca.randomname.monitor.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimePeriod implements Serializable {

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime start;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime end;

    private MonitoringStatus status;

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public MonitoringStatus getStatus() {
        return status;
    }

    public void setStatus(MonitoringStatus status) {
        this.status = status;
    }
}
