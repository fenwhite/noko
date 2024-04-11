package com.kinya.neko.interceptors;

import com.kinya.neko.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ：white
 * @description：authentication request
 * @date ：2022/5/22
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final static String TOKEN = "Token";
    private static final String LOGIN_MATCH = "/login";
    private static final String LOGOUT_MATCH = "/NULL";

    /**
     * will be called before the actual handler is executed
     *
     * @param request
     * @return authentication result
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if(inWhite(uri)) return true;
        // get token
        String token = request.getHeader(TOKEN);
        if(StringUtils.isBlank(token)) return false;
        // AES decrypt it
        Claims claims = TokenUtils.prase(token);
        //validate token
        return TokenUtils.check(claims);
    }

    private boolean inWhite(String uri) {
        if(StringUtils.isBlank(uri)) {
            return false;
        }
        if(uri.startsWith(LOGIN_MATCH)) {
            return true;
        }
        return false;
    }

    // todo https://cloud.tencent.com/developer/article/1504237
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String token = request.getHeader(TOKEN);
        if(StringUtils.isNoneBlank(token)) {
            // TODO get user name
            response.setHeader(TOKEN, token);
            response.flushBuffer();
            return;
        }
        doWhenLogin(request, response);
        doWhenLogout(request, response);
    }

    /**
     * add token to response when after deal login request
     *
     * @param request login request
     * @param response response of the request
     */
    private void doWhenLogin(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        if(uri.startsWith(LOGIN_MATCH)) {
            String userName = request.getParameter("name");
            // only for test
            userName = "fgl";
            if (StringUtils.isNoneBlank(userName)) {
                String token = TokenUtils.create(userName);
                response.setStatus(400);
                response.addHeader(TOKEN, token);
                try {
                    response.getWriter().write(100);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * invalidate user sending the request
     *
     * @param request logout request
     * @param response response of this request
     */
    private void doWhenLogout(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        if(uri.startsWith(LOGOUT_MATCH)) {
            String expiredToken = TokenUtils.invalid();
            response.setHeader(TOKEN, expiredToken);
        }
    }
}
