package ca.randomname.monitor.rest;

import ca.randomname.monitor.api.IMonitorService;
import ca.randomname.monitor.model.MonitoringContext;
import ca.randomname.monitor.model.MonitoringData;
import ca.randomname.monitor.rest.model.StartMonitoringRequest;
import ca.randomname.monitor.rest.model.StopMonitoringRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/monitor")
public class MonitorServiceRestV1 {

    @Autowired
    private IMonitorService monitorService;

    @RequestMapping(method = RequestMethod.POST, value = "/start")
    public void start(@RequestBody StartMonitoringRequest request) {

        MonitoringContext context = new MonitoringContext();
        context.setHostname(request.getHostname());
        context.setInterval(request.getInterval());
        monitorService.start(context);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/stop")
    public void stop(@RequestBody StopMonitoringRequest request) {

        monitorService.stop(request.getHostname());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/monitoring_data")
    public MonitoringData getMonitoringData(@RequestParam(value = "hostname", required = true) String hostname) {
        return monitorService.getMonitoringData(hostname);
    }
}
