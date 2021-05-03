package dhbw.cloudia.phonenumber.boundary.handler;

import dhbw.cloudia.phonenumber.boundary.dto.ErrorTO;
import dhbw.cloudia.phonenumber.control.exception.PhoneNumberParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Special Spring exception handler that takes thrown exception and creates response.
 */
@RestControllerAdvice
public class PhoneNumberApiExceptionHandler {

    /**
     * Method that handles PhoneNumberParseExceptions.
     * @param e parse exception with message
     * @return HTTP-Status 400 with error message
     */
    @ExceptionHandler(PhoneNumberParseException.class)
    public ResponseEntity<ErrorTO> handleParseError(PhoneNumberParseException e) {
        return ResponseEntity.status(400).body(ErrorTO.builder().errorMessage(e.getMessage()).build());
    }
}
