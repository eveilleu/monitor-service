package ca.randomname.monitor.impl;

import ca.randomname.monitor.impl.monitor.MonitorThread;
import ca.randomname.monitor.model.MonitoringContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfiguration {

    @Bean
    @Scope("prototype")
    public MonitorThread getMonitorThread(MonitoringContext context) {
        return new MonitorThread(context);
    }


    @Bean
    public RestTemplate getRestTemplate() {
        return mock(RestTemplate.class);
    }
}
