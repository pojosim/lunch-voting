package com.example.lunchvoting.web;

import com.example.lunchvoting.util.ValidationUtil;
import com.example.lunchvoting.util.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionInfoHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        return logAndGetErrorInfo(req, e, true, ErrorType.VALIDATION_ERROR);
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorInfo handleAccessDeniedException(HttpServletRequest req, AccessDeniedException e) {
        return logAndGetErrorInfo(req, e, true, ErrorType.APP_ERROR);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, ErrorType.DATA_NOT_FOUND);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, MethodArgumentNotValidException e) {
        return logAndGetErrorInfo(req, e, ErrorType.VALIDATION_ERROR);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(IllegalRequestDataException.class)
    public ErrorInfo handleError(HttpServletRequest req, IllegalRequestDataException e) {
        return logAndGetErrorInfo(req, e, false, ErrorType.VALIDATION_ERROR);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(VoteRepeatException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, VoteRepeatException e) {
        return logAndGetErrorInfo(req, e, true, ErrorType.VOTE_REPEAT_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, ErrorType.APP_ERROR);
    }

    private ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);

        if (logException) {
            logger.error(errorType + " from request " + req.getRequestURL(), rootCause);
        } else {
            logger.warn("{} from request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }

        return new ErrorInfo(req.getRequestURL(), errorType, rootCause.getLocalizedMessage());
    }

    private ErrorInfo logAndGetErrorInfo(HttpServletRequest req, MethodArgumentNotValidException e, ErrorType errorType) {
        List<String> details = new ArrayList<>();
        BindingResult result = e.getBindingResult();
        result.getFieldErrors().forEach(fieldError -> {
            String msg = fieldError.getField() + ": " + fieldError.getDefaultMessage();
            details.add(msg);
        });

        return new ErrorInfo(req.getRequestURL(), errorType, details.toArray(new String[0]));
    }
}