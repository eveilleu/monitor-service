package ca.randomname.monitor.impl.monitor;


import ca.randomname.monitor.impl.accountmanagement.AccountManagementClient;
import ca.randomname.monitor.impl.accumulator.MonitoringDataAccumulator;
import ca.randomname.monitor.model.MonitoringContext;
import ca.randomname.monitor.model.MonitoringStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MonitorThread extends Thread {

    private static Logger logger = LoggerFactory.getLogger(MonitorThread.class);
    private final MonitoringContext context;

    private boolean keepGoing = true;

    @Autowired
    private AccountManagementClient client;

    @Autowired
    private MonitoringDataAccumulator accumulator;

    public MonitorThread(MonitoringContext context) {
        this.context = context;
    }

    @Override
    public void run() {

        while (keepGoing) {

            MonitoringStatus status = client.monitor(context.getHostname());
            accumulator.addMonitoringData(status, context.getHostname());

            try {
                logger.debug("Monitor of " + context.getHostname() + " sleeping for " + context.getInterval() + " millis.");
                Thread.sleep(context.getInterval());
            } catch (InterruptedException e) {
                logger.info("Monitor of " + context.getHostname() + " got interrupted ", e);
            }
        }

    }

    public void stopMe() {
        keepGoing = false;
    }
}
