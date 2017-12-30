package ca.randomname.monitor.impl;

import ca.randomname.monitor.api.IMonitorService;
import ca.randomname.monitor.api.MonitorServiceException;
import ca.randomname.monitor.impl.accumulator.MonitoringDataAccumulator;
import ca.randomname.monitor.impl.monitor.MonitorThread;
import ca.randomname.monitor.model.MonitoringContext;
import ca.randomname.monitor.model.MonitoringData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class MonitorService implements IMonitorService {

    private static Logger logger = LoggerFactory.getLogger(MonitorService.class);

    private ConcurrentMap<String, MonitorThread> threads = new ConcurrentHashMap<>();

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MonitoringDataAccumulator accumulator;

    @Override
    synchronized
    public void start(MonitoringContext monitoringContext) {

        validateHostname(monitoringContext.getHostname());
        validateInterval(monitoringContext.getInterval());

        if (threads.containsKey(monitoringContext.getHostname())) {
            throw new MonitorServiceException(monitoringContext.getHostname() + " is already monitored.", 400);
        }

        logger.info("Starting monitor for " + monitoringContext.getHostname());
        MonitorThread monitor = context.getBean(MonitorThread.class, monitoringContext);
        threads.put(monitoringContext.getHostname(), monitor);
        monitor.start();
    }

    @Override
    synchronized
    public void stop(String hostname) {
        validateHostname(hostname);

        MonitorThread monitor = threads.get(hostname);
        if (monitor == null) {
            throw new MonitorServiceException(hostname + " is not monitored.", 400);
        }

        logger.info("Stopping monitor for " + hostname);
        monitor.stopMe();
        accumulator.stopMonitoring(hostname);
        threads.remove(hostname, monitor);
    }

    @Override
    public MonitoringData getMonitoringData(String hostname) {
        validateHostname(hostname);

        logger.info("Returning monitor data for " + hostname);
        return accumulator.getMonitoringData(hostname);
    }

    private void validateInterval(long interval) {
        if (interval <= 0) {
            throw new MonitorServiceException("interval must be positive", 400);
        }
    }

    private void validateHostname(String hostname) {

        if (hostname == null) {
            throw new MonitorServiceException("hostname is required", 400);
        }
        try {
            InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            throw new MonitorServiceException("Unknown host " + hostname, 400);
        }
    }
}
