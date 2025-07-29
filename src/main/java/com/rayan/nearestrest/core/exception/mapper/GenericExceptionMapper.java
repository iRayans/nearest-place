package com.rayan.nearestrest.core.exception.mapper;

import com.rayan.nearestrest.dto.ErrorMessageDTO;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

/**
 * Catches all unhandled exceptions and returns an appropriate error response
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(GenericExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        // Log the unhandled exception
        LOG.error("Unhandled exception", exception);

        // Build and return a generic error response
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorMessageDTO(
                        "INTERNAL_ERROR",
                        "An unexpected error occurred",
                        uriInfo != null ? uriInfo.getPath() : "unknown"
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}