package com.bjpowernode.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 2020/4/29
 */
public class OneFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

              HttpServletRequest request = (HttpServletRequest)servletRequest;

           //1.拦截后，通过请求对象向Tomcat索要当前用户的HttpSession。
              HttpSession session = request.getSession(false);
           //2.判断来访用户身份合法性
              if(session == null){
                 request.getRequestDispatcher("/login_error.html").forward(servletRequest, servletResponse);
                 return;
              }
           //3.放行
           filterChain.doFilter(servletRequest, servletResponse);
    }
}
