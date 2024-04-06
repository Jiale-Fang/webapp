package pers.fjl.healthcheck.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> data = new HashMap<>();
        data.put("error", "Unauthorized");
        data.put("err_msg", e.getMessage());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS z");
        String formattedDateTime = ZonedDateTime.now().format(formatter);
        data.put("timestamp", formattedDateTime);

        if (e instanceof InternalAuthenticationServiceException) {
            InternalAuthenticationServiceException exception = (InternalAuthenticationServiceException) e;
            if (exception.getCause() != null) {
                if (exception.getCause() instanceof PersistenceException) {
                    response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
                    data.put("error", "Service Unavailable");
                }
            }
        } else if (e instanceof LockedException) {
            data.put("error", "Forbidden");
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("X-Content-Type-Options", "nosniff");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response.getOutputStream().write(objectMapper.writeValueAsBytes(data));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}