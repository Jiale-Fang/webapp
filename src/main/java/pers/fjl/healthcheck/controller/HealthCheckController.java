package pers.fjl.healthcheck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pers.fjl.healthcheck.entity.Result;
import pers.fjl.healthcheck.service.HealthCheckService;
import pers.fjl.healthcheck.utils.HttpUtils;

@RestController
public class HealthCheckController {
    @Autowired
    private HealthCheckService healthCheckService;

    @GetMapping("/healthz")
    public ResponseEntity<Object> healthCheck(@RequestBody(required = false) String requestBody) {
        // Check if the request body has parameters
        if (StringUtils.hasText(requestBody)) {
            return Result.fail(HttpStatus.BAD_REQUEST);
        }

        if (healthCheckService.isDatabaseConnected()) {
            return Result.ok();
        } else {
            return Result.fail(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
