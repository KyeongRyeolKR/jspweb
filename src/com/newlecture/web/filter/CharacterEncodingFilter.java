package com.newlecture.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        servletRequest.setCharacterEncoding("UTF-8");   // 서블릿에 인코딩 방식을 UTF-8로 설정함 (한글지원)
        filterChain.doFilter(servletRequest, servletResponse);  // 다음 필터 or 서블릿 실행

    }

    @Override
    public void destroy() {

    }
}
