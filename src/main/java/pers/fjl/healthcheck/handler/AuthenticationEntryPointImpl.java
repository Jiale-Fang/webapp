package pers.fjl.healthcheck.handler;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import pers.fjl.healthcheck.utils.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        if (e instanceof InternalAuthenticationServiceException) {
            InternalAuthenticationServiceException exception = (InternalAuthenticationServiceException) e;
            if (exception.getCause() != null) {
                if (exception.getCause() instanceof PersistenceException) {
                    response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
                }
            }
        }

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("X-Content-Type-Options", "nosniff");
    }
}