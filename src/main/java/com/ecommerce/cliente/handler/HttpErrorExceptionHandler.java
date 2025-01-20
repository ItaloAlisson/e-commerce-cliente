package com.ecommerce.cliente.handler;

import com.ecommerce.cliente.exceptions.BadRequestException;
import com.ecommerce.cliente.exceptions.ConflictException;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HttpErrorExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> conflict(ConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> notFound(ObjectNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Não foi encontrado o " +
                        exception.getEntityName() +
                        " com o ID" +
                exception.getIdentifier());

    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> BadRequest(BadRequestException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }


}
