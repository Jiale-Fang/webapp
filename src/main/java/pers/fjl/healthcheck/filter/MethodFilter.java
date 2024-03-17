
package pers.fjl.healthcheck.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MethodFilter extends OncePerRequestFilter {

    /**
     * Only allow these 4 http methods post, put, delete, get
     * expect for options, head ...
     * See: https://stackoverflow.com/questions/42367975/disable-http-options-method-in-spring-boot-application
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getMethod().equals("OPTIONS") || request.getMethod().equals("HEAD")) {
            logger.error("Illegal http method");
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } else {
            filterChain.doFilter(request, response);
        }
    }
} 