package pers.fjl.healthcheck.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.fjl.healthcheck.service.HealthCheckService;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class HealthCheckServiceImpl implements HealthCheckService {

    @Autowired
    private DataSource dataSource;

    public boolean isDatabaseConnected() {
        // Try to connect to database
        try (Connection connection = dataSource.getConnection()) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
