package ca.randomname.monitor.impl.accountmanagement;

import ca.randomname.monitor.api.MonitorServiceException;
import ca.randomname.monitor.impl.TestConfiguration;
import ca.randomname.monitor.model.MonitoringStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, AccountManagementClient.class})
public class AccountManagementClientTest {

    @Autowired
    private RestTemplate restTemplate;

    @InjectMocks
    private AccountManagementClient client;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenMonitoringServiceAnswersReady_MonitoringDataShouldSayUP() {
        String hostname = "good.url.com";
        String url = "https://" + hostname + "/accountmanagement/monitor";

        MonitorResponse response = new MonitorResponse();
        response.setStatus("READY");
        when(restTemplate.getForObject(url, MonitorResponse.class)).thenReturn(response);

        MonitoringStatus actual = client.monitor(hostname);

        assertNotNull("MonitoringStatus is null", actual);
        assertEquals("status is not equal", MonitoringStatus.UP, actual);
    }

    @Test
    public void whenMonitoringServiceAnswersNotReady_MonitoringDataShouldSayDown() {
        String hostname = "bad.url.com";
        String url = "https://" + hostname + "/accountmanagement/monitor";

        MonitorResponse response = new MonitorResponse();
        response.setStatus("NOT READY");
        when(restTemplate.getForObject(url, MonitorResponse.class)).thenReturn(response);


        MonitoringStatus actual = client.monitor(hostname);

        assertNotNull("MonitoringStatus is null", actual);
        assertEquals("status is not equal", MonitoringStatus.DOWN, actual);
    }

    @Test(expected = MonitorServiceException.class)
    public void whenMonitoringServiceThrowsAnException_ClientShouldWrapItInAMonitorServiceException() {
        String hostname = "boom.com";
        String url = "https://" + hostname + "/accountmanagement/monitor";

        when(restTemplate.getForObject(url, MonitorResponse.class)).thenThrow(new RestClientException("Sorry, don't know " + hostname));

        client.monitor(hostname);
        fail();
    }
}
