package pers.fjl.healthcheck.utils;

import org.springframework.http.HttpHeaders;

public class HttpUtils {
    public static HttpHeaders getResponseHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        headers.setPragma("no-cache");
        headers.clearContentHeaders();
        headers.add("X-Content-Type-Options", "nosniff");
        return headers;
    }
}
