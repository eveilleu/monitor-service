package ca.randomname.monitor.api;

import ca.randomname.monitor.model.MonitoringContext;
import ca.randomname.monitor.model.MonitoringData;

public interface IMonitorService {
    void start(MonitoringContext monitoringContext);

    void stop(String hostname);

    MonitoringData getMonitoringData(String hostname);
}
