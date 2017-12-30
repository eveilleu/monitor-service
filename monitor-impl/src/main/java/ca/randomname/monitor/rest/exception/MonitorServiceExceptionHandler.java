package ca.randomname.monitor.rest.exception;

import ca.randomname.monitor.api.MonitorServiceException;
import ca.randomname.monitor.rest.model.ApiError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MonitorServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MonitorServiceException.class)
    protected ResponseEntity<Object> handleMonitorServiceException(MonitorServiceException ex) {

        if (ex.getStatusCode() == 400) {
            logger.info(ex.getMessage());
        } else {
            logger.error(ex.getMessage(), ex);

        }

        // This is where we could do i18n and such for the errors.
        ApiError error = new ApiError();
        error.setMessage(ex.getMessage());

        if (ex.getStatusCode() != 0) {
            return new ResponseEntity<>(error, HttpStatus.valueOf(ex.getStatusCode()));

        } else {
            return new ResponseEntity<>(error, HttpStatus.valueOf(500));
        }
    }
}
