package com.raczkowski.springintro.filter;


import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class SessionFilter implements Filter {

    private static final String SESSION_ID_NAME = "JSESSIONID";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> maybeCookie = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(SESSION_ID_NAME))
                    .findFirst();

            if (maybeCookie.isPresent()) {
                Cookie sessionCookie = maybeCookie.get();
                sessionCookie.setHttpOnly(true);
                sessionCookie.setSecure(true);
                response.addCookie(sessionCookie);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
