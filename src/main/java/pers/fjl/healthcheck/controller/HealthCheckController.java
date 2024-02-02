package pers.fjl.healthcheck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pers.fjl.healthcheck.service.HealthCheckService;
import pers.fjl.healthcheck.utils.HttpUtils;

@RestController
public class HealthCheckController {
    @Autowired
    private HealthCheckService healthCheckService;

    @GetMapping("/healthz")
    public ResponseEntity<String> healthCheck(@RequestBody(required = false) String requestBody) {
        HttpHeaders headers = HttpUtils.getResponseHeaders();

        // Check if the request body has parameters
        if (StringUtils.hasText(requestBody)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
        }

        if (healthCheckService.isDatabaseConnected()) {
            return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).headers(headers).build();
        }
    }
}
