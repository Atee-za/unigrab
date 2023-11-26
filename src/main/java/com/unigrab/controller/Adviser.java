package com.unigrab.controller;

import com.unigrab.model.dto.ErrorResponse;
import com.unigrab.model.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class Adviser {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    private ErrorResponse userNotFound(NotFoundException e) {
        return handleException(e.getCode(), e);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthorizedException.class)
    private ErrorResponse userNotAuthorized(UnAuthorizedException e) {
        return handleException(e.getCode(), e);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    private ErrorResponse badRequest(BadRequestException e) {
        return handleException(e.getCode(), e);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    private ErrorResponse forbiddenRequest(ForbiddenException e) {
        return handleException(e.getCode(), e);
    }

    private ErrorResponse handleException(String code, RuntimeException e){
        log.error("{} - {}", code, e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

}
