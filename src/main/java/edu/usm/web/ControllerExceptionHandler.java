package edu.usm.web;

import edu.usm.domain.exception.*;
import edu.usm.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactController.class);


    @ExceptionHandler(NullDomainReference.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody Response handleNullDomainReference(NullDomainReference e) {
        LOGGER.info(e.toString());
        return new Response(null, e.getMessage(), Response.TYPE_NULL_DOMAIN_REFERENCE);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody Response handleUnauthorizedUser(AccessDeniedException e) {
        LOGGER.info(e.toString());
        return new Response(null, e.getMessage(), Response.TYPE_ACCESS_DENIED);
    }

    @ExceptionHandler(ConstraintViolation.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody Response handleConstraintViolation(ConstraintViolation e) {
        LOGGER.info(e.toString());
        return new Response(e.getClashingDomainId(), e.getMessage(), Response.TYPE_CONSTRAINT_VIOLATION);
    }

    @ExceptionHandler(InvalidApiRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody Response handleInvalidApiRequest(InvalidApiRequestException e) {
        LOGGER.info(e.toString());
        return new Response(null, e.getMessage(), Response.INVALID_API_REQUEST);
    }

    @ExceptionHandler(SecurityConstraintException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody Response handleSecurityConstraint(SecurityConstraintException e) {
        LOGGER.info(e.toString());
        return new Response(null, e.getMessage(), Response.SECURITY_CONSTRAINT);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody Response handleNotFound(NotFoundException e) {
        LOGGER.info(e.toString());
        return new Response(null, e.getMessage(), Response.NOT_FOUND);
    }


}