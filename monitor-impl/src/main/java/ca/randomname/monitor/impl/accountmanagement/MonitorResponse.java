package ca.randomname.monitor.impl.accountmanagement;

public class MonitorResponse {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MonitorResponse{" +
                "status='" + status + '\'' +
                '}';
    }
}
