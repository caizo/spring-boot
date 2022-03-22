package org.pmv.springboot.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiException {
    private String message;
    private HttpStatus httpStatus;
    private ZonedDateTime timestamp;
    private String path;
    private Map<String,String> validationErrors;
    //private final Throwable throwable;

    public ApiException(String message, HttpStatus httpStatus, ZonedDateTime timestamp, String path) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
        this.path = path;
    }

    public ApiException() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
