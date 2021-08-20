package ru.uzaretskaya.todo.auth.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.uzaretskaya.todo.auth.entity.User;
import ru.uzaretskaya.todo.auth.exception.JwtCommonException;
import ru.uzaretskaya.todo.auth.service.UserDetailsImpl;
import ru.uzaretskaya.todo.auth.utils.CookieUtils;
import ru.uzaretskaya.todo.auth.utils.JwtUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private CookieUtils cookieUtils;

    private final List<String> permiteURL = asList(
            "register",
            "login",
            "activate-account",
            "resend-activate-email",
            "send-request-password-email",
            "test-no-auth",
            "index"
    );

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Autowired
    public void setCookieUtils(CookieUtils cookieUtils) {
        this.cookieUtils = cookieUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isRequestToPublicApi = permiteURL.stream().anyMatch(s -> request.getRequestURI().toLowerCase().contains(s));
        if (!isRequestToPublicApi
                //&& SecurityContextHolder.getContext().getAuthentication() == null
        ) {
            String jwt = cookieUtils.getAccessToken(request);
            if (jwt != null) {
                if (jwtUtils.validate(jwt)) {
                    User user = jwtUtils.getUser(jwt);
                    UserDetailsImpl userDetails = new UserDetailsImpl(user);

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    throw new JwtCommonException("JWT validate exception");
                }
            } else {
                throw new AuthenticationCredentialsNotFoundException("Token not found");
            }
        }

        filterChain.doFilter(request, response);
    }
}
