package pers.fjl.healthcheck.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pers.fjl.healthcheck.utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@ToString
public class Result {

    private static HttpHeaders headers = HttpUtils.getResponseHeaders();

    public static ResponseEntity<Object> ok() {
        return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
    }

    public static ResponseEntity<Object> ok(HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).headers(headers).build();
    }

    public static ResponseEntity<Object> ok(Object data) {
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(data);
    }

    public static ResponseEntity<Object> ok(HttpStatus httpStatus, Object data) {
        return ResponseEntity.status(httpStatus).headers(headers).body(data);
    }

    public static ResponseEntity<Object> fail(HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).headers(headers).build();
    }

    public static ResponseEntity<Object> fail(HttpStatus httpStatus, String errMsg) {
        Map<String, String> errMap = new HashMap<>();
        errMap.put("err_msg", errMsg);
        return ResponseEntity.status(httpStatus).headers(headers).body(errMap);
    }

}
