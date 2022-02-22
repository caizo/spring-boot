package org.pmv.springboot.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ErrorControllerHandler implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;


    /**
     *
     * @param request
     * @return
     */
    @RequestMapping("/error")
    public ApiException handleError(WebRequest request){

        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(request,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE,
                        ErrorAttributeOptions.Include.BINDING_ERRORS));

        String message = (String) errorAttributes.get("message");
        String path = (String) errorAttributes.get("path");
        Integer status = (Integer) errorAttributes.get("status");
        ApiException apiException = new ApiException(message,HttpStatus.valueOf(status), ZonedDateTime.now(ZoneId.of("Z")),path);

        if(errorAttributes.containsKey("errors")){
            Map<String,String> valErrors = new HashMap<>();
            List<FieldError> fieldErrors = (List<FieldError>) errorAttributes.get("errors");
            for(FieldError fe: fieldErrors){
                valErrors.put(fe.getField(), fe.getDefaultMessage());
            }
            apiException.setValidationErrors(valErrors);
        }
        return apiException;
    }
}
