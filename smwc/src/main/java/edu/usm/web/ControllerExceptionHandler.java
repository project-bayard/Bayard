package edu.usm.web;

import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.NullDomainReference;
import edu.usm.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by andrew on 8/20/15.
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NullDomainReference.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody Response handleNullDomainReference(NullDomainReference e) {
        return new Response(null, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody Response handleUnauthorizedUser(AccessDeniedException e) {
        return new Response(null, e.getMessage());
    }

    @ExceptionHandler(ConstraintViolation.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody Response handleConstraintViolation(ConstraintViolation e) {
        return new Response(null, e.getMessage());
    }

}