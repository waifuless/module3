package com.epam.esm.gcs.web.controller;

import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.exception.NotUniquePropertyException;
import com.epam.esm.gcs.web.error.ErrorResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class GenericExceptionHandler {

    private final static String INTERNAL_SERVER_ERROR = "server.error.internal";
    private final static String NOT_FOUND_ERROR = "not.found.error";
    private final static String NOT_UNIQUE_PROPERTY_ERROR = "not.unique.property";
    private final static String ARGUMENT_TYPE_MISMATCH_ERROR = "argument.type.mismatch";
    private final static String DEFAULT_BAD_REQUEST = "default.bad.request";

    private final MessageSource clientErrorMessageSource;
    private final MessageSource serverErrorMessageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentNotValidException(MethodArgumentNotValidException ex,
                                                         Locale locale) {
        BindingResult result = ex.getBindingResult();
        result.getObjectName();
        String errorMessage = result.getAllErrors()
                .stream()
                .map(objectError -> clientErrorMessageSource.getMessage(objectError, locale))
                .collect(Collectors.joining("; ", "", "."));
        return new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST,
                Objects.requireNonNull(result.getTarget()).getClass());
    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
//        return new ErrorResponse("ConstraintViolationException: " + e.getLocalizedMessage(),
//                "40001");
//    }

//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
//    public ErrorResponse handleMethodNotSupported(HttpRequestMethodNotSupportedException e, Locale locale) {
//        log.error(e.getMessage(), e);
//        return new ErrorResponse(serverErrorMessageSource.getMessage(INTERNAL_SERVER_ERROR, null, locale),
//                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
//    }

    @ExceptionHandler(NotUniquePropertyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleNotUniquePropertyValue(NotUniquePropertyException ex, Locale locale) {
        return new ErrorResponse(clientErrorMessageSource
                .getMessage(NOT_UNIQUE_PROPERTY_ERROR, new Object[]{ex.getField(), ex.getValue()}, locale),
                HttpStatus.CONFLICT, ex.getDtoClass());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, Locale locale) {
        return new ErrorResponse(clientErrorMessageSource
                .getMessage(ARGUMENT_TYPE_MISMATCH_ERROR,
                        new Object[]{ex.getName(), ex.getValue(), ex.getRequiredType()}, locale),
                String.valueOf(HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMessageNotReadable(HttpMessageNotReadableException messageReadableEx, Locale locale) {
        try {
            InvalidFormatException invalidFormatEx = (InvalidFormatException) messageReadableEx.getCause();
            JsonMappingException.Reference ref = invalidFormatEx.getPath().get(0);
            return new ErrorResponse(clientErrorMessageSource
                    .getMessage(ARGUMENT_TYPE_MISMATCH_ERROR,
                            new Object[]{ref.getFieldName(), invalidFormatEx.getValue(),
                                    invalidFormatEx.getTargetType()},
                            locale),
                    HttpStatus.BAD_REQUEST, invalidFormatEx.getPath().get(0).getFrom().getClass());
        } catch (Exception ex) {
            return new ErrorResponse(DEFAULT_BAD_REQUEST, String.valueOf(HttpStatus.BAD_REQUEST.value()));
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(EntityNotFoundException ex, Locale locale) {
        return new ErrorResponse(clientErrorMessageSource
                .getMessage(NOT_FOUND_ERROR, new Object[]{ex.getField(), ex.getValue()}, locale),
                HttpStatus.NOT_FOUND, ex.getDtoClass());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherExceptions(Exception e, Locale locale) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(serverErrorMessageSource.getMessage(INTERNAL_SERVER_ERROR, null, locale),
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
