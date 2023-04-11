package uz.optimit.taxi.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Stream;

@RestControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<ApiError>> handleBindingException(BindException e, HttpServletRequest request) {
        List<ApiError> apiErrors = Stream.concat(
                e.getBindingResult().getFieldErrors().stream()
                        .map(fieldError -> new ApiError(
                                        request.getRequestURI(),
                                        fieldError.getField(),
                                        HttpStatus.NOT_FOUND.value(),
                                        fieldError.getDefaultMessage()
                                )
                        ),
                e.getBindingResult().getGlobalErrors().stream()
                        .map(globalError -> new ApiError(
                                request.getRequestURI(),
                                globalError.getObjectName(),
                                HttpStatus.NOT_FOUND.value(),
                                globalError.getDefaultMessage()))
        ).toList();
        return new ResponseEntity<>(apiErrors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ApiError> handleObjectNotException(RecordNotFoundException e, HttpServletRequest request) {
        ApiError ariError = new ApiError(
                request.getRequestURI()
                , e.getMessage()
                , HttpStatus.NOT_FOUND.value()
                , " Object not found");
        return new ResponseEntity<>(ariError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecordAlreadyExistException.class)
    public ResponseEntity<ApiError> handleObjectAlreadyExist(RecordAlreadyExistException e, HttpServletRequest request) {
        ApiError ariError = new ApiError(
                request.getRequestURI()
                , e.getMessage()
                , HttpStatus.ALREADY_REPORTED.value()
                , " Object already have");
        return new ResponseEntity<>(ariError, HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        ApiError ariError = new ApiError(
                request.getRequestURI()
                , e.getMessage()
                , HttpStatus.NOT_FOUND.value()
                , " User not found");
        return new ResponseEntity<>(ariError, HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> handleAccessTokenTimeExceededException(Exception e, HttpServletRequest request) {
//        ApiError ariError = new ApiError(
//                request.getRequestURI()
//                , e.getMessage()
//                , HttpStatus.BAD_GATEWAY.value()
//                , " Token time out");
//        return new ResponseEntity<>(ariError, HttpStatus.BAD_GATEWAY);
//    }

    @ExceptionHandler(SmsSendingFailException.class)
    public ResponseEntity<ApiError> handleSmsSendingFailException(SmsSendingFailException e, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.CONFLICT.value(),
                "Can not send sms to your phone number "
        );
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
}
