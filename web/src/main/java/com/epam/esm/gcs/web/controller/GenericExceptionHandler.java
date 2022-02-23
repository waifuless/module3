package com.epam.esm.gcs.web.controller;

import com.epam.esm.gcs.business.dto.UserOrderDto;
import com.epam.esm.gcs.business.exception.EntitiesArchivedException;
import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.exception.GiftCertificateCountsNotEnoughException;
import com.epam.esm.gcs.business.exception.NotUniquePropertyException;
import com.epam.esm.gcs.web.error.ErrorResponse;
import com.epam.esm.gcs.web.error.ErrorResponseEntitiesArchived;
import com.epam.esm.gcs.web.error.ErrorResponseGiftCertificateCountNotEnough;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.MimeType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
//todo: make default messages
public class GenericExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR = "server.error.internal";
    private static final String NOT_FOUND_ERROR = "not.found.error";
    private static final String NOT_UNIQUE_PROPERTY = "not.unique.property";
    private static final String ARGUMENT_TYPE_MISMATCH = "argument.type.mismatch";
    private static final String DEFAULT_BAD_REQUEST = "default.bad.request";
    private static final String NO_HANDLER_FOUND = "no.handler.found";
    private static final String NO_METHOD_SUPPORTED = "no.method.supported";
    private static final String METHOD_NOT_ALLOWED = "method.not.allowed";
    private static final String MEDIA_TYPE_NOT_SUPPORTED = "media.type.not.supported";
    private static final String ENTITIES_ARCHIVED = "entities.archived";
    private static final String GIFT_CERTIFICATE_NOT_ENOUGH_COUNT = "gift.certificate.not.enough.count";

    private final MessageSource clientErrorMessageSource;
    private final MessageSource serverErrorMessageSource;

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentNotValidException(BindException ex, Locale locale) {
        BindingResult result = ex.getBindingResult();
        Class<?> targetClass = Objects.requireNonNull(result.getTarget()).getClass();
        result.getObjectName();
        String errorMessage = result.getAllErrors()
                .stream()
                .map(objectError -> clientErrorMessageSource.getMessage(objectError, locale))
                .collect(Collectors.joining("; ", "", "."));
        return new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST, targetClass);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorResponse handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                     Locale locale) {
        String contentType = ex.getContentType() == null ? "" : ex.getContentType().toString();
        String supportedMediaTypes = ex.getSupportedMediaTypes().stream()
                .map(MimeType::toString)
                .collect(Collectors.joining(", "));
        String errorMessage = clientErrorMessageSource
                .getMessage(MEDIA_TYPE_NOT_SUPPORTED, new Object[]{contentType, supportedMediaTypes}, locale);
        return new ErrorResponse(errorMessage, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e, Locale locale) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(constraintViolation ->
                        clientErrorMessageSource.getMessage(constraintViolation.getMessage(),
                                new Object[]{constraintViolation.getInvalidValue(),
                                        constraintViolation.getPropertyPath()},
                                locale))
                .collect(Collectors.joining("; ", "", "."));
        Class<?> targetClass = e.getConstraintViolations().iterator().next().getLeafBean().getClass();
        return new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST, targetClass);
    }

    @ExceptionHandler(NotUniquePropertyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleNotUniquePropertyValue(NotUniquePropertyException ex, Locale locale) {
        String errorMessage = clientErrorMessageSource
                .getMessage(NOT_UNIQUE_PROPERTY, new Object[]{ex.getField(), ex.getValue()}, locale);
        return new ErrorResponse(errorMessage, HttpStatus.CONFLICT, ex.getDtoClass());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, Locale locale) {
        String errorMessage = clientErrorMessageSource
                .getMessage(ARGUMENT_TYPE_MISMATCH,
                        new Object[]{ex.getName(), ex.getValue(), ex.getRequiredType()}, locale);
        return new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMessageNotReadable(HttpMessageNotReadableException messageReadableEx, Locale locale) {
        try {
            InvalidFormatException invalidFormatEx = (InvalidFormatException) messageReadableEx.getCause();
            JsonMappingException.Reference ref = invalidFormatEx.getPath().get(0);
            String errorMessage = clientErrorMessageSource
                    .getMessage(ARGUMENT_TYPE_MISMATCH,
                            new Object[]{ref.getFieldName(), invalidFormatEx.getValue(),
                                    invalidFormatEx.getTargetType()},
                            locale);
            Class<?> targetClass = invalidFormatEx.getPath().get(0).getFrom().getClass();
            return new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST, targetClass);
        } catch (Exception ex) {
            String errorMessage = clientErrorMessageSource.getMessage(DEFAULT_BAD_REQUEST, null, locale);
            return new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(EntityNotFoundException ex, Locale locale) {
        String errorMessage = clientErrorMessageSource
                .getMessage(NOT_FOUND_ERROR, new Object[]{ex.getField(), ex.getValue()}, locale);
        return new ErrorResponse(errorMessage, HttpStatus.NOT_FOUND, ex.getDtoClass());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoHandlerFound(NoHandlerFoundException ex, Locale locale) {
        String errorMessage = clientErrorMessageSource
                .getMessage(NO_HANDLER_FOUND, new Object[]{ex.getHttpMethod(), ex.getRequestURL()}, locale);
        return new ErrorResponse(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, Locale locale) {
        String message;
        if (ex.getSupportedHttpMethods() == null) {
            message = clientErrorMessageSource.getMessage(NO_METHOD_SUPPORTED, null, locale);
        } else {
            String supportedMethods = ex.getSupportedHttpMethods().stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            message = clientErrorMessageSource
                    .getMessage(METHOD_NOT_ALLOWED, new Object[]{ex.getMethod(), supportedMethods}, locale);
        }
        return new ErrorResponse(message, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(EntitiesArchivedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseEntitiesArchived handleEntitiesArchived(EntitiesArchivedException ex, Locale locale) {
        //todo: return links to actual entities instead of Ids
        String message = clientErrorMessageSource.getMessage(ENTITIES_ARCHIVED, null, locale);
        return new ErrorResponseEntitiesArchived(message, HttpStatus.FORBIDDEN, ex);
    }

    @ExceptionHandler(GiftCertificateCountsNotEnoughException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseGiftCertificateCountNotEnough handleNotEnoughCount(GiftCertificateCountsNotEnoughException ex,
                                                                           Locale locale) {
        String message = clientErrorMessageSource.getMessage(GIFT_CERTIFICATE_NOT_ENOUGH_COUNT, null, locale);
        return new ErrorResponseGiftCertificateCountNotEnough(message, HttpStatus.FORBIDDEN, UserOrderDto.class, ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherExceptions(Exception e, Locale locale) {
        log.error(e.getMessage(), e);
        String errorMessage = serverErrorMessageSource.getMessage(INTERNAL_SERVER_ERROR, null, locale);
        return new ErrorResponse(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
