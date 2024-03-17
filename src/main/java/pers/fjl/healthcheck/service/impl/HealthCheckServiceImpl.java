package pers.fjl.healthcheck.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(HealthCheckServiceImpl.class);

    public boolean isDatabaseConnected() {
        // Try to connect to database
        try (Connection connection = dataSource.getConnection()) {
            return true;
        } catch (SQLException e) {
            logger.error("Unable to connect to the database", e);
            return false;
        }
    }
}
