package com.example.game.exceptions;

import com.example.game.payloads.errors.ErrorsDTO;
import com.example.game.payloads.errors.ErrorsWithListDTO;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorsWithListDTO handleBadRequest(BadRequestException e) {
        if (e.getMessageList() != null) {
            List<String> errorsList = e.getMessageList().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ErrorsWithListDTO(e.getMessage(), new Date(), errorsList);
        } else {
            return new ErrorsWithListDTO(e.getMessage(), new Date(), new ArrayList<>());
        }
    }
    @ExceptionHandler(CityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 400
    public ErrorsDTO handleCityNotFound(CityNotFoundException e) {
            return new ErrorsDTO(e.getMessage(), new Date());
    }
    @ExceptionHandler(UserWithEmailNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 400
    public ErrorsDTO handleUserWithEmailNotFound(UserWithEmailNotFoundException e) {
        return new ErrorsDTO(e.getMessage(), new Date());
    }
    @ExceptionHandler(UserWithIdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 400
    public ErrorsDTO handleUserWithIdNotFound(UserWithIdNotFoundException e) {
        return new ErrorsDTO(e.getMessage(), new Date());
    }
    @ExceptionHandler(CodeMismatchException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE) // 400
    public ErrorsDTO handleCodeMismatch(CodeMismatchException e) {
        return new ErrorsDTO(e.getMessage(), new Date());
    }
    @ExceptionHandler(InvalidParamsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorsDTO handleInvalidParams(InvalidParamsException e) {
        return new ErrorsDTO(e.getMessage(), new Date());
    }
    @ExceptionHandler(PasswordMismatchException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE) // 400
    public ErrorsDTO handlePasswordMismatch(PasswordMismatchException e) {
        return new ErrorsDTO(e.getMessage(), new Date());
    }
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorsDTO handleUnauthorized(UnauthorizedException e) {
        return new ErrorsDTO(e.getMessage(), new Date());
    }
    @ExceptionHandler(EmailAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorsDTO handleEmailAlreadyInUse(EmailAlreadyInUseException e) {
        return new ErrorsDTO(e.getMessage(), new Date());
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    public ErrorsDTO handleGeneric(Exception e) {
        e.printStackTrace();
        return new ErrorsDTO("Problema lato server.", new Date());
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)  // 500
    public ErrorsDTO handleGeneric(AccessDeniedException e) {
        e.printStackTrace();
        return new ErrorsDTO(e.getMessage(),new Date());
    }
}
