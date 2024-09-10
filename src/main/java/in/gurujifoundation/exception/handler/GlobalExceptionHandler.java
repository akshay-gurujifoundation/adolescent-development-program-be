package in.gurujifoundation.exception.handler;


import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.exception.BadRequestException;
import in.gurujifoundation.exception.EntityNotFoundException;
import in.gurujifoundation.exception.ForbiddenException;
import in.gurujifoundation.response.APIResponse;
import in.gurujifoundation.response.ResponseMessage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<APIResponse<Object>> handleBadRequestException(BadRequestException ex) {
        log.error(ex.getMessage(), ex.getCause());
        ResponseMessage responseMessage = ResponseMessage.builder().message(ex.getMessage()).build();
        APIResponse<Object> apiResponse = APIResponse.builder().status(Boolean.FALSE).messages(List.of(responseMessage)).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Object>> handleInternalServerException(Exception ex) {
        log.error(ex.getMessage(), ex.getCause());
        ResponseMessage responseMessage = ResponseMessage.builder().message("Unexpected error occurred").build();
        APIResponse<Object> apiResponse = APIResponse.builder().status(Boolean.FALSE).messages(List.of(responseMessage)).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .distinct()
                .toList();
        List<ResponseMessage> responseMessageList = errorMessages.stream()
                .map(ResponseMessage::new)
                .toList();
        APIResponse<Object> apiResponse = APIResponse.builder().status(Boolean.FALSE).messages(responseMessageList).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<APIResponse<Object>> handleForbiddenException(ForbiddenException ex) {
        log.error(ex.getMessage(), ex.getCause());
        ResponseMessage responseMessage = ResponseMessage.builder().message(ex.getMessage()).build();
        APIResponse<Object> apiResponse = APIResponse.builder().status(Boolean.FALSE).messages(List.of(responseMessage)).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<APIResponse<Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage(), ex.getCause());
        ResponseMessage responseMessage = ResponseMessage.builder().message(ex.getMessage()).build();
        APIResponse<Object> apiResponse = APIResponse.builder().status(Boolean.FALSE).messages(List.of(responseMessage)).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponse<Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errorMessages = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .distinct()
                .toList();
        List<ResponseMessage> responseMessageList = errorMessages.stream()
                .map(ResponseMessage::new)
                .toList();
        APIResponse<Object> apiResponse = APIResponse.builder().status(Boolean.FALSE).messages(responseMessageList).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIResponse<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage(), ex.getCause());
        ResponseMessage responseMessage = ResponseMessage.builder().message(ErrorCodeConstant.INVALID_REQUEST_BODY).build();
        APIResponse<Object> apiResponse = APIResponse.builder().status(Boolean.FALSE).messages(List.of(responseMessage)).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Logged is user is not an admin", ex);
        ResponseMessage responseMessage = ResponseMessage.builder().message(ex.getMessage()).build();
        APIResponse<Object> apiResponse = APIResponse.builder().status(Boolean.FALSE).messages(List.of(responseMessage)).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }
}
