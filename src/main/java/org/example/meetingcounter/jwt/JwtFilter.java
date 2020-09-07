package org.example.meetingcounter.jwt;


import org.example.meetingcounter.model.User;
import org.example.meetingcounter.services.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

@Component
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);


    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserServiceImpl userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        logger.info("**doFilter Start");
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        if ((token != null) && jwtProvider.validateToken(token)) {
            String login = jwtProvider.getLoginFromToken(token);
            User user = userService.loadUserByUsername(login);
            user.setExpirationDate(jwtProvider.getExpirationDate(token));
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user,
                    null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(servletRequest, servletResponse);
        logger.info("**doFilter done");
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith(BEARER)) {
            return bearer.substring(BEARER.length());
        }
        return null;
    }
}
