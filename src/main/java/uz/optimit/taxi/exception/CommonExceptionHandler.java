package uz.optimit.taxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.optimit.taxi.entity.api.ApiResponse;

import java.util.List;
import java.util.stream.Stream;

import static uz.optimit.taxi.entity.Enum.Constants.TOKEN_TIME_OUT;

@RestControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<ApiResponse> handleBindingException(BindException e) {
        return Stream.concat(
                e.getBindingResult().getFieldErrors().stream()
                        .map(fieldError -> new ApiResponse(
                                fieldError.getDefaultMessage(),
                                false,
                                fieldError.getField())),
                e.getBindingResult().getGlobalErrors().stream()
                        .map(globalError -> new ApiResponse(
                                globalError.getDefaultMessage(),
                                false,
                                globalError.getObjectName()))
        ).toList();

    }

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse handleObjectNotException(RecordNotFoundException e) {
        return new ApiResponse(
                e.getMessage()
                , false
                , null);
    }

    @ExceptionHandler(RecordAlreadyExistException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ApiResponse handleObjectAlreadyExist(RecordAlreadyExistException e) {
        return new ApiResponse(
                e.getMessage()
                , false
                , null);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse handleUserNotFoundException(UserNotFoundException e) {
        return new ApiResponse(
                e.getMessage()
                , false
                , null);
    }
    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse handleUserNotFoundException(UserAlreadyExistException e) {
        return new ApiResponse(
            e.getMessage()
            , false
            , " User already exist ");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ApiResponse handleAccessTokenTimeExceededException(Exception e) {
        return new ApiResponse(
                TOKEN_TIME_OUT
                , false
                , null);
    }

    @ExceptionHandler(SmsSendingFailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse handleSmsSendingFailException(SmsSendingFailException e) {
        return new ApiResponse(
                e.getMessage()
                , false
                , null);
    }

    @ExceptionHandler(NotEnoughSeat.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse notEnoughNotException(NotEnoughSeat e) {
        return new ApiResponse(
                e.getMessage()
                , false
                , null);
    }
}
