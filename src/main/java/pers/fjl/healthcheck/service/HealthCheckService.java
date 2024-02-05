package pers.fjl.healthcheck.service;

public interface HealthCheckService {

    /**
     * Check database connection status
     * @return flag
     */
    boolean isDatabaseConnected();
}
