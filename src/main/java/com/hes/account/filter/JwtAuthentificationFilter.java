package com.hes.account.filter;

import com.hes.account.service.AccountServiceImpl;
import com.hes.account.service.JwtServiceImpl;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthentificationFilter implements Filter {
    public static final String AUTHORIZATION = "Authorization";
    public static final String LOGIN = "/login";
    final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Autowired
    public JwtServiceImpl jwtService;
    @Autowired
    public UserDetailsService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = request.getRequestURI();
        if (path.startsWith(LOGIN)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String token = request.getHeader(AUTHORIZATION);
            if (token != null && !token.isEmpty()) {
                String userName = jwtService.extractUsername(token);
                if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userService.loadUserByUsername(userName);
                    if (jwtService.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }

                }
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                logger.info("Not valid token");
            }
        }
    }

    @Override
    public void destroy() {

    }
}
