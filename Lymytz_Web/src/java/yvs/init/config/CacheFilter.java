/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.init.config;

/**
 *
 * @author DOWES
 */
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CacheFilter implements Filter {

    private static long maxAge = 86400 * 30; // 30 days in seconds

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String uri = ((HttpServletRequest) request).getRequestURI();
        if (uri.contains(".js") || uri.contains(".css") || uri.contains(".svg") || uri.contains(".gif")
                || uri.contains(".woff") || uri.contains(".png")) {
            httpResponse.setHeader("Cache-Control", "max-age=" + maxAge);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Cache Filter started: ");

    }

    @Override
    public void destroy() {
    }
}
