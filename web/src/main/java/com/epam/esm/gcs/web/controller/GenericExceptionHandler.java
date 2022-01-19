package com.epam.esm.gcs.web.controller;

import com.epam.esm.gcs.web.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class GenericExceptionHandler {

    private final static String INTERNAL_SERVER_ERROR = "server.error.internal";

    private final MessageSource validationMessageSource;
    private final MessageSource serverErrorMessageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentNotValidException(MethodArgumentNotValidException ex,
                                                         Locale locale) {
        BindingResult result = ex.getBindingResult();
        result.getObjectName();
        String errorMessage = result.getAllErrors()
                .stream()
                .map(objectError -> validationMessageSource.getMessage(objectError, locale))
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

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherExceptions(Exception e, Locale locale) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(serverErrorMessageSource.getMessage(INTERNAL_SERVER_ERROR, null, locale),
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
