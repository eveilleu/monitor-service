package ca.randomname.monitor.impl.accumulator;

import ca.randomname.monitor.model.MonitoringData;
import ca.randomname.monitor.model.MonitoringStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MonitoringDataAccumulator.class})
public class MonitoringDataAccumulatorTest {

    @Autowired
    private MonitoringDataAccumulator accumulator;


    @Test
    public void addingDataForANewHostname_createsDataAndCurrentTimePeriod() {
        String hostname = "host1.ca";

        accumulator.addMonitoringData(MonitoringStatus.UP, hostname);
        MonitoringData actual = accumulator.getMonitoringData(hostname);

        assertNotNull("MonitoringData is null", actual);
        assertEquals("hostname is not equal", hostname, actual.getHostname());
        assertNotNull("CurrentTimePeriod is null", actual.getCurrentTimePeriod());
        assertEquals("status is not equal", MonitoringStatus.UP, actual.getCurrentTimePeriod().getStatus());
    }

    @Test
    public void addingDataTwiceForTheSameHostnameAndSameStatus_generatesOnly1TimePeriod() {
        String hostname = "host2.ca";

        accumulator.addMonitoringData(MonitoringStatus.UP, hostname);
        accumulator.addMonitoringData(MonitoringStatus.UP, hostname);
        MonitoringData actual = accumulator.getMonitoringData(hostname);

        assertNotNull("MonitoringData is null", actual);
        assertEquals("Data does not have 1 time period", 1, actual.getTimePeriods().size());
    }


    @Test
    public void addingDataTwiceForTheSameHostnameAndDifferentStatus_generates2TimePeriods() {
        String hostname = "host3.ca";

        accumulator.addMonitoringData(MonitoringStatus.UP, hostname);
        accumulator.addMonitoringData(MonitoringStatus.DOWN, hostname);
        MonitoringData actual = accumulator.getMonitoringData(hostname);

        assertNotNull("MonitoringData is null", actual);
        assertEquals("Data does not have 2 time periods", 2, actual.getTimePeriods().size());
        assertEquals("status is not equal", MonitoringStatus.DOWN, actual.getCurrentTimePeriod().getStatus());

    }

    @Test
    public void addingDataForTheSameHostAfterStopping_generates2TimePeriods() {
        String hostname = "host4.ca";

        accumulator.addMonitoringData(MonitoringStatus.UP, hostname);
        accumulator.stopMonitoring(hostname);
        accumulator.addMonitoringData(MonitoringStatus.UP, hostname);
        MonitoringData actual = accumulator.getMonitoringData(hostname);

        assertNotNull("MonitoringData is null", actual);
        assertEquals("Data does not have 2 time periods", 2, actual.getTimePeriods().size());
    }

    @Test
    public void stopMonitoring_nullifiesTheCurrentPeriod() {
        String hostname = "host5.ca";

        accumulator.addMonitoringData(MonitoringStatus.UP, hostname);
        accumulator.stopMonitoring(hostname);
        MonitoringData actual = accumulator.getMonitoringData(hostname);

        assertNotNull("MonitoringData is null", actual);
        assertNull("Current period is not null", actual.getCurrentTimePeriod());
    }
}
