package com.architecture.admin.libraries.filter;

import javax.servlet.*;
import java.io.IOException;

public interface Filter extends javax.servlet.Filter {
    @Override
    public default void init(FilterConfig filterConfig) throws ServletException {}

    // 고객의 요청이 올 때마다 해당 메서드가 호출
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException;
    @Override
    public default void destroy() {}
}
