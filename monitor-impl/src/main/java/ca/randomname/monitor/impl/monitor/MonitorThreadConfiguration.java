package ca.randomname.monitor.impl.monitor;

import ca.randomname.monitor.model.MonitoringContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MonitorThreadConfiguration {

    @Bean
    @Scope("prototype")
    public MonitorThread getMonitorThread(MonitoringContext context) {
        return new MonitorThread(context);
    }


    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
