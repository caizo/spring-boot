package org.pmv.springboot.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


/**
 * Use ErrorHandlerController instead
 */
@ControllerAdvice
public class ApiExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /**
     *
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(value = {NoSuchElementException.class})
    //@ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundtException(NoSuchElementException exception, HttpServletRequest request){
        // 1. create payload containing exception details
        ApiException apiException = new ApiException(exception.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now(ZoneId.of("Z")),
                request.getServletPath(),
                null);
        // 2. return response entity
        return new ResponseEntity<>(apiException,HttpStatus.NOT_FOUND);
    }


    /**
     *
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    //@ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotValidArgumentException(MethodArgumentNotValidException exception, HttpServletRequest request){
        // 1. create payload containing exception details
        Map<String,String> valErrors = new HashMap<>();
        List<FieldError> fieldErrors = exception.getFieldErrors();

        for(FieldError fe: fieldErrors){
            valErrors.put(fe.getField(), fe.getDefaultMessage());
        }

        ApiException apiException = new ApiException(exception.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z")),
                request.getServletPath(),
                valErrors);
        // 2. return response entity
        return new ResponseEntity<>(apiException,HttpStatus.BAD_REQUEST);
    }
}
