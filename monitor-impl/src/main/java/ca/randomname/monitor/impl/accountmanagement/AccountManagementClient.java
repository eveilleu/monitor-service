package ca.randomname.monitor.impl.accountmanagement;

import ca.randomname.monitor.api.MonitorServiceException;
import ca.randomname.monitor.model.MonitoringStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Component
public class AccountManagementClient {

    private static Logger logger = LoggerFactory.getLogger(AccountManagementClient.class);

    @Autowired
    private RestTemplate restTemplate = new RestTemplate();

    public MonitoringStatus monitor(String hostname) {

        String url = "https://" + hostname + "/accountmanagement/monitor";

        try {
            MonitorResponse response = restTemplate.getForObject(url, MonitorResponse.class);
            logger.info("Called monitor service for " + hostname + " response:" + response);

            if ("READY".equalsIgnoreCase(response.getStatus())) {
                return MonitoringStatus.UP;
            }
        } catch (RestClientException e) {
            throw new MonitorServiceException("Error calling the monitor service", e);
        }

        return MonitoringStatus.DOWN;

    }
}
