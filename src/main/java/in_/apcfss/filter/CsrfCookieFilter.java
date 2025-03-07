package in_.apcfss.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CsrfCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        String method = request.getMethod();

        if ( null != csrfToken) { //!method.equals(HttpMethod.GET.name()) &&
            response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
        }else {
            System.out.println("CSRF FAILED");
        }
        filterChain.doFilter(request, response);
    }

}
