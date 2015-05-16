package com.ahimsa.utils;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.isNullOrEmpty;

public class ResponseUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseUtils.class);

    // Utility class with private constructor
    private ResponseUtils() {
    }

    public static Response ok() {
        return Response.ok().build();
    }

    public static <T> Response ok(T value) {
        return Response.ok(value).build();
    }

    public static Response created() {
        return Response.status(Response.Status.CREATED).build();
    }

    public static <T> Response created(T value) {
        return Response.ok(value).status(Response.Status.CREATED).build();
    }

    public static Response notFound() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public static Response unauthorized() {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    public static Response forbidden() {
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    public static <T> Response okOrNotFoundIfNull(T value) {
        if (value == null) {
            return notFound();
        } else {
            return ok(value);
        }
    }

    public static <T> T notFoundIfNull(T value) {
        if (value == null) {
            throw createNotFoundException();
        }
        return value;
    }

    public static <T> T badRequestIfNull(T parameter) {
        return badRequestIfNull(parameter, "Null value not allowed");
    }

    public static <T> T badRequestIfNull(T parameter, String errorMessage) {
        if (parameter == null) {
            throw createBadRequestException(errorMessage);
        }
        return parameter;
    }

    public static <T> void badRequestIfNotNull(T parameter) {
        badRequestIfNotNull(parameter, "Value not allowed");
    }

    public static <T> void badRequestIfNotNull(T parameter, String errorMessage) {
        if (parameter != null) {
            throw createBadRequestException(errorMessage);
        }
    }

    public static String badRequestIfEmpty(String parameter) {
        return badRequestIfEmpty(parameter, "Empty value not allowed");
    }

    public static String badRequestIfEmpty(String parameter, String errorMessage) {
        if (isNullOrEmpty(parameter)) {
            throw createBadRequestException(errorMessage);
        }
        return parameter;
    }

    public static <T> T badRequestIfNotEqual(T a, T b) {
        return badRequestIfNotEqual(a, b, "Inconsistent data");
    }

    public static <T> T badRequestIfNotEqual(T a, T b, String errorMessage) {
        if (!Objects.equals(a, b)) {
            throw createBadRequestException(errorMessage);
        }
        return a;
    }

    public static WebApplicationException createBadRequestException(String content) {
        Response response = Response
                .status(Response.Status.BAD_REQUEST)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity(content)
                .build();
        WebApplicationException exception = new WebApplicationException(response);
        if (LOG.isInfoEnabled()) {
            // Get first 5 frames of the stack trace
            String stackTrace = Arrays.stream(ExceptionUtils.getStackFrames(exception))
                    .limit(5)
                    .collect(Collectors.joining("\n"));
            LOG.info("createBadRequestException(\"{}\"):\n{}", content, stackTrace);
        }
        return exception;
    }

    public static WebApplicationException createNotFoundException() {
        Response response = Response
                .status(Response.Status.NOT_FOUND)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity("Not Found")
                .build();
        return new WebApplicationException(response);
    }

    public static WebApplicationException createUnauthorizedException() {
        Response response = Response
                .status(Response.Status.UNAUTHORIZED)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity("Unauthorized")
                .build();
        return new WebApplicationException(response);
    }

}
