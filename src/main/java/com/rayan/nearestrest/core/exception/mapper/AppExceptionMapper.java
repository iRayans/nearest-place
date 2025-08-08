package com.rayan.nearestrest.core.exception.mapper;


import com.rayan.nearestrest.core.exception.AppServerException;
import com.rayan.nearestrest.core.exception.EntityInvalidArgumentsException;
import com.rayan.nearestrest.core.exception.GenericException;
import com.rayan.nearestrest.core.exception.ResultNotFoundException;
import com.rayan.nearestrest.dto.ErrorMessageDTO;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

/**
 * Maps application exceptions to appropriate HTTP responses
 */
@Provider
public class AppExceptionMapper implements ExceptionMapper<GenericException> {

    private static final Logger LOG = Logger.getLogger(AppExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(GenericException exception) {
        // Default status
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

        // Determine the appropriate HTTP status based on exception type

        if (exception instanceof AppServerException) {
            status = Response.Status.SERVICE_UNAVAILABLE;
        } else if (exception instanceof ResultNotFoundException) {
            status = Response.Status.NOT_FOUND;
        }


        // Log the exception
        if (exception instanceof EntityInvalidArgumentsException) {
            status = Response.Status.BAD_REQUEST;
        }


        if (status == Response.Status.INTERNAL_SERVER_ERROR) {
            LOG.error("Unhandled exception", exception);
        } else {
            LOG.info("API exception: " + status + " - " + exception.getMessage());
        }

        // Build and return the error response
        return Response
                .status(status)
                .entity(new ErrorMessageDTO(
                        exception.getCode(),
                        exception.getMessage(),
                        uriInfo != null ? uriInfo.getPath() : "unknown"
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}