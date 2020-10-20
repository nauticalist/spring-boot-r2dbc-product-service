package io.seanapse.app.products.infrastructure.exception.handler;

import io.seanapse.app.products.infrastructure.exception.exception.AccessDeniedException;
import io.seanapse.app.products.infrastructure.exception.exception.FileStorageException;
import io.seanapse.app.products.infrastructure.exception.exception.ResourceNotFoundException;
import io.seanapse.app.products.infrastructure.exception.exception.UnauthorizedException;
import io.seanapse.app.products.infrastructure.exception.model.ErrorCodes;
import io.seanapse.app.products.infrastructure.exception.model.ErrorModel;
import io.seanapse.app.products.infrastructure.exception.model.ErrorResponse;
import io.seanapse.app.products.infrastructure.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


@ControllerAdvice
public class ExceptionControllerAdvice {
    private static final Logger LOG = LoggerFactory.getLogger(org.springframework.web.bind.annotation.ExceptionHandler.class);


    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse onResourceFound(ResourceNotFoundException exception, ServerHttpRequest request) {
        LOG.error(String.format("No resource found exception occurred: %s ", exception.getMessage()));

        ErrorResponse response = new ErrorResponse();
        response.getErrors().add(
                new ErrorModel(
                        ErrorCodes.ERR_RESOURCE_NOT_FOUND,
                        "Resource not found",
                        exception.getMessage()));

        return response;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse onAccessDenied(AccessDeniedException exception) {
        LOG.error(String.format("Access denied: %s ", exception.getMessage()));

        ErrorResponse response = new ErrorResponse();
        response.getErrors().add(
                new ErrorModel(
                        ErrorCodes.ERR_ACCESS_DENIED,
                        "Access Denied",
                        exception.getMessage()));

        return response;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse onUnauthorizedAccess(UnauthorizedException exception) {
        LOG.error(String.format("Unauthorized access detected to the resource: %s ", exception.getMessage()));

        ErrorResponse response = new ErrorResponse();
        response.getErrors().add(
                new ErrorModel(
                        ErrorCodes.ERR_UNAUTHORIZED_ACCESS,
                        "Unauthorized access detected",
                        exception.getMessage()));

        return response;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        ErrorResponse error = new ErrorResponse();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.getErrors().add(
                    new ErrorModel(
                            ErrorCodes.ERR_REQUEST_PARAMS_BODY_VALIDATION_FAILED,
                            fieldError.getField(),
                            fieldError.getDefaultMessage()));
        }
        LOG.error(String.format("Validation exception occurred: %s", JsonConverter.convertObjectToJsonString(error)));
        return error;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageConversionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse onInvalidRequest(HttpMessageConversionException e) {
        LOG.error("Invalid request received", e);

        ErrorResponse error = new ErrorResponse();
        error.getErrors().add(
                new ErrorModel(
                        ErrorCodes.ERR_REQUEST_PARAMS_BODY_VALIDATION_FAILED,
                        "Invalid Request",
                        "Invalid request body. Please verify the request and try again !!"
                )
        );

        return error;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        LOG.error("Constraint validation exception occurred", e);

        ErrorResponse error = new ErrorResponse();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            error.getErrors().add(
                    new ErrorModel(
                            ErrorCodes.ERR_CONSTRAINT_CHECK_FAILED,
                            violation.getPropertyPath().toString(),
                            violation.getMessage()));
        }
        return error;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(FileStorageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse onFileStorageException(FileStorageException e) {
        LOG.error("Error occurred while uploading file", e);

        ErrorResponse error = new ErrorResponse();
        error.getErrors().add(
                new ErrorModel(
                        ErrorCodes.ERR_RUNTIME,
                        "Error occurred while uploading the file",
                        e.getMessage()
                )
        );
        return error;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse onRuntimeException(RuntimeException e) {

        LOG.error("Error occurred while handling request", e);

        ErrorResponse error = new ErrorResponse();
        error.getErrors().add(
                new ErrorModel(
                        ErrorCodes.ERR_RUNTIME,
                        "Internal Server Error",
                        "Error occurred while processing your request. Please try again !!"
                )
        );
        return error;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(WebClientResponseException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> onWebClientResponseExcepion(WebClientResponseException exception) {
        ErrorResponse error = new ErrorResponse();
        switch (exception.getStatusCode()) {
            case NOT_FOUND :
                error.getErrors().add(
                        new ErrorModel(
                                ErrorCodes.ERR_RESOURCE_NOT_FOUND,
                                "Resource not found",
                                exception.getMessage()));
                return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);

            case UNPROCESSABLE_ENTITY :
                error.getErrors().add(
                        new ErrorModel(
                                ErrorCodes.ERR_CONSTRAINT_CHECK_FAILED,
                                "Invalid input detected",
                                exception.getMessage()));
                return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNPROCESSABLE_ENTITY);
            default:
                LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", exception.getStatusCode());
                LOG.warn("Error body: {}", exception.getResponseBodyAsString());
                error.getErrors().add(
                        new ErrorModel(
                                ErrorCodes.ERR_RUNTIME,
                                "Internal Server Error",
                                "Error occurred while processing your request. Please try again !!"
                        )
                );
                return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
