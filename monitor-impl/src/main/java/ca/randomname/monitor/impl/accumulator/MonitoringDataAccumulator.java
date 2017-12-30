package ca.randomname.monitor.impl.accumulator;

import ca.randomname.monitor.api.MonitorServiceException;
import ca.randomname.monitor.model.MonitoringData;
import ca.randomname.monitor.model.MonitoringStatus;
import ca.randomname.monitor.model.TimePeriod;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class MonitoringDataAccumulator {

    private ConcurrentMap<String, MonitoringData> dataMap = new ConcurrentHashMap<>();

    public MonitoringData getMonitoringData(String hostname) {
        return dataMap.get(hostname);
    }

    synchronized
    public void addMonitoringData(MonitoringStatus currentStatus, String hostname) {

        MonitoringData data = dataMap.get(hostname);
        LocalDateTime now = LocalDateTime.now();

        if (data == null) {
            data = addHostToMap(hostname);
        }

        TimePeriod currentPeriod = data.getCurrentTimePeriod();

        // Not currently monitored?
        if (currentPeriod == null) {
            TimePeriod newPeriod = createPeriod(currentStatus, now);
            data.getTimePeriods().add(newPeriod);
            data.setCurrentTimePeriod(newPeriod);
        } else {

            // Change of status?
            if (!currentPeriod.getStatus().equals(currentStatus)) {
                currentPeriod.setEnd(now.minusSeconds(1));

                TimePeriod newPeriod = createPeriod(currentStatus, now);
                data.getTimePeriods().add(newPeriod);
                data.setCurrentTimePeriod(newPeriod);
            } else {
                data.getCurrentTimePeriod().setEnd(now);

            }
        }
    }

    private MonitoringData addHostToMap(String hostname) {
        MonitoringData data = new MonitoringData();
        data.setHostname(hostname);
        data.setTimePeriods(new LinkedList<>());

        dataMap.put(hostname, data);
        return data;
    }

    private TimePeriod createPeriod(MonitoringStatus currentStatus, LocalDateTime now) {
        TimePeriod timePeriod = new TimePeriod();
        timePeriod.setStart(now);
        timePeriod.setEnd(null);
        timePeriod.setStatus(currentStatus);

        return timePeriod;
    }

    synchronized
    public void stopMonitoring(String hostname) {
        MonitoringData data = dataMap.get(hostname);

        if (data == null) {
            throw new MonitorServiceException(hostname + " is not monitored.", 500);
        }

        TimePeriod currentTimePeriod = data.getCurrentTimePeriod();
        if (currentTimePeriod != null) {
            currentTimePeriod.setEnd(LocalDateTime.now());
            data.setCurrentTimePeriod(null);
        }
    }

}