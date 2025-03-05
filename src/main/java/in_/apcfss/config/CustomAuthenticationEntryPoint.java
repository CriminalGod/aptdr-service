package in_.apcfss.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import in_.apcfss.dto.ApiResponse;
import in_.apcfss.util.ApiResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        try {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            if (authException != null && authException.getMessage() != null) {
                logger.error("🚨 Unauthorized access attempt: {} {}", authException.getClass(),
                        authException.getMessage());
                ApiResponse<Void> authExceptionResponse = ApiResponseUtil.error(authException.getMessage(),
                        "UnAuthorized access attempt",
                        1001,
                        request.getRequestURI());
                if (response.getWriter() != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    response.getWriter().write(mapper.writeValueAsString(authExceptionResponse));
                } else {
                    logger.error("Failed to write response: response.getWriter() is null");
                }
            } else {
                logger.error("Failed to create authExceptionResponse: ex or ex.getMessage() is null");
            }
        } catch (IOException e) {
            logger.error("Failed to write response", e);
        }
    }
}
