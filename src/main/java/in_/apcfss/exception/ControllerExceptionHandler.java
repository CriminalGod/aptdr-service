package in_.apcfss.exception;

import in_.apcfss.dto.ApiResponse;
import in_.apcfss.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> globalExceptionHandler(Exception ex, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        ApiResponse<Void> response = ApiResponseUtil.error(errors, "An error occurred", 1000, "");
        logExceptionDetails(ex, logger);
        return response;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> globalRuntimeExceptionHandler(Exception ex, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        ApiResponse<Void> response = ApiResponseUtil.error(errors, "An error occurred", 1000, "");
        logExceptionDetails(ex, logger);
        return response;
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ApiResponse<Void> authenticationExceptionHandler(AuthenticationException ex, HttpServletRequest request) {
        ApiResponse<Void> response = ApiResponseUtil.error(ex.getMessage(),
                "Authentication Failure",
                2000,
                request.getRequestURI());
        logExceptionDetails(ex, logger);
        return response;
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ApiResponse<Void> resourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        ApiResponse<Void> response = ApiResponseUtil.error(ex.getMessage(),
                "Resource not found",
                1001,
                request.getRequestURI());
        logExceptionDetails(ex, logger);
        return response;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> badCredentialsExceptionHandler(BadCredentialsException ex, WebRequest                          request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        ApiResponse<Void> response = ApiResponseUtil.error(errors, "An error occurred", 1000, "");
        logExceptionDetails(ex, logger);
        return response;
    }

    private static void logExceptionDetails(Exception ex, Logger logger) {
        Throwable throwable = Optional.ofNullable(ex.getCause()).orElse(ex);
        logger.error(throwable.getMessage(), throwable);
    }

}
