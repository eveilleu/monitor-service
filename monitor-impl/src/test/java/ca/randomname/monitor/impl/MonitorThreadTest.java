package ca.randomname.monitor.impl;


import ca.randomname.monitor.impl.accountmanagement.AccountManagementClient;
import ca.randomname.monitor.impl.accountmanagement.MonitorResponse;
import ca.randomname.monitor.impl.accumulator.MonitoringDataAccumulator;
import ca.randomname.monitor.impl.monitor.MonitorThread;
import ca.randomname.monitor.model.MonitoringContext;
import ca.randomname.monitor.model.MonitoringData;
import ca.randomname.monitor.model.MonitoringStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * Bigger end to end tests, My unit is from the monitoring thread, the accountmanagement client and the accumulator.
 * Validates the entire behaviour of the threads.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, AccountManagementClient.class, MonitoringDataAccumulator.class})
public class MonitorThreadTest {

    @Autowired
    private RestTemplate restTemplate;
    @InjectMocks
    AccountManagementClient accountManagementClient;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private MonitoringDataAccumulator accumulator;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void whenMonitoringServiceAnswersReady_MonitoringDataShouldSayUP() {

        MonitoringContext context = new MonitoringContext();
        context.setHostname("good.url.com");
        context.setInterval(100L);

        String url = "https://" + context.getHostname() + "/accountmanagement/monitor";

        MonitorResponse response = new MonitorResponse();
        response.setStatus("READY");
        when(restTemplate.getForObject(url, MonitorResponse.class)).thenReturn(response);

        MonitorThread thread = getMonitorThread(context);
        thread.start();

        // small sleep, just to make sure the thread has time to start before we kill it.
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }

        thread.stopMe();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }

        MonitoringData actual = accumulator.getMonitoringData(context.getHostname());

        assertNotNull("MonitoringData is null", actual);
        assertEquals("hostname is not equal", context.getHostname(), actual.getHostname());
        assertEquals("status is not equal", MonitoringStatus.UP, actual.getCurrentTimePeriod().getStatus());
    }

    @Test
    public void whenMonitoringServiceAnswersNotReady_MonitoringDataShouldSayDown() {

        MonitoringContext context = new MonitoringContext();
        context.setHostname("bad.url.com");
        context.setInterval(100L);

        String url = "https://" + context.getHostname() + "/accountmanagement/monitor";

        MonitorResponse response = new MonitorResponse();
        response.setStatus("NOT READY");
        when(restTemplate.getForObject(url, MonitorResponse.class)).thenReturn(response);


        MonitorThread thread = getMonitorThread(context);
        thread.start();

        // small sleep, just to make sure the thread has time to start before we kill it.
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }

        thread.stopMe();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }

        MonitoringData actual = accumulator.getMonitoringData(context.getHostname());

        assertNotNull("MonitoringData is null", actual);
        assertEquals("hostname is not equal", context.getHostname(), actual.getHostname());
        assertEquals("status is not equal", MonitoringStatus.DOWN, actual.getCurrentTimePeriod().getStatus());
    }

    private MonitorThread getMonitorThread(MonitoringContext monitoringContext) {
        return context.getBean(MonitorThread.class, monitoringContext);
    }

}
