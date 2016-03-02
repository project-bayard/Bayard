package edu.usm.web;

import edu.usm.domain.exception.*;
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
        return new Response(null, e.getMessage(), Response.TYPE_NULL_DOMAIN_REFERENCE);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody Response handleUnauthorizedUser(AccessDeniedException e) {
        return new Response(null, e.getMessage(), Response.TYPE_ACCESS_DENIED);
    }

    @ExceptionHandler(ConstraintViolation.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody Response handleConstraintViolation(ConstraintViolation e) {
        return new Response(e.getClashingDomainId(), e.getMessage(), Response.TYPE_CONSTRAINT_VIOLATION);
    }

    @ExceptionHandler(InvalidApiRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody Response handleInvalidApiRequest(InvalidApiRequestException e) {
        return new Response(null, e.getMessage(), Response.INVALID_API_REQUEST);
    }

    @ExceptionHandler(SecurityConstraintException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody Response handleSecurityConstraint(SecurityConstraintException e) {
        return new Response(null, e.getMessage(), Response.SECURITY_CONSTRAINT);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody Response handleNotFound(NotFoundException e) {
        return new Response(null, e.getMessage(), Response.NOT_FOUND);
    }


}