package org.eru.errorhandling;

import org.eru.errorhandling.exceptions.common.ServerErrorException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<EpicErrorModel> handleException(BaseException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Epic-Error-Name", e.ErrorCode);
        responseHeaders.set("X-Epic-Error-Code", Integer.toString(e.StatusCode));

        return ResponseEntity.status(e.StatusCode).headers(responseHeaders).body(new EpicErrorModel(e.ErrorCode, e.getMessage(), e.MessageVars, e.NumericErrorCode));
    }

    //@ExceptionHandler(Exception.class)
    public ResponseEntity<EpicErrorModel> handleException(Exception e) {
        return handleException(new ServerErrorException());
    }
}
