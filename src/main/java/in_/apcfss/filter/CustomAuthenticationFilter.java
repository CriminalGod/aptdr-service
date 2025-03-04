//package in_.apcfss.filter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.core.log.LogMessage;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.context.SecurityContextHolderStrategy;
//import org.springframework.security.web.authentication.AuthenticationConverter;
//import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
//import org.springframework.security.web.context.SecurityContextRepository;
//import org.springframework.util.StringUtils;
//
//import java.io.IOException;
//
//public class CustomAuthenticationFilter extends BasicAuthenticationFilter {
//
//    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
//        super(authenticationManager);
//    }
//
//    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
//    private AuthenticationManager authenticationManager;
//    private AuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();
//    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws IOException, ServletException {
//        try {
//            String header = request.getHeader("Authorization");
//
//            if (!StringUtils.hasText(header)) {
//                this.logger.trace("Did not process authentication request since no Basic Authorization header was found");
//                filterChain.doFilter(request, response);
//                return;
//            }
//            Authentication authRequest = this.authenticationConverter.convert(request);
//            if (authRequest == null) {
//                this.logger.trace("Did not process authentication request since failed to find username and password in Basic Authorization header");
//                filterChain.doFilter(request, response);
//                return;
//            }
//            String username = authRequest.getName();
//            this.logger.trace(LogMessage.format("Found username '%s' in Basic Authorization header", username));
//            if (this.authenticationIsRequired(username)) {
//                Authentication authResult = this.authenticationManager.authenticate(authRequest);
//                SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
//                context.setAuthentication(authResult);
//                this.securityContextHolderStrategy.setContext(context);
//                if (this.logger.isDebugEnabled()) {
//                    this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authResult));
//                }
//
//                this.securityContextRepository.saveContext(context, request, response);
//                this.onSuccessfulAuthentication(request, response, authResult);
//            }
//        } catch (AuthenticationException ex) {
//            this.securityContextHolderStrategy.clearContext();
//            this.logger.debug("Failed to process authentication request", ex);
//            this.onUnsuccessfulAuthentication(request, response, ex);
//            return;
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
//    }
//
//    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
//    }
//
//    protected AuthenticationManager getAuthenticationManager() {
//        return this.authenticationManager;
//    }
//
//}
