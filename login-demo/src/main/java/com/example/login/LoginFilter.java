package com.example.login;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebFilter(filterName = "LoginFilter",urlPatterns = "/*")
public class LoginFilter implements Filter {

    // 排除列表，包含无需登录即可访问的路径
    private final String[] excludedPaths = {"/Login.html","/login"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器已经在客户端和servlet之间建立链接");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // 检查请求路径是否在排除列表中
        if (isExcludedPath(httpRequest.getRequestURI())) {
            // 如果是，则直接放行
            chain.doFilter(request, response);
        } else {
            // 如果不是，检查session中是否存在表示已登录的属性
            if (session != null && session.getAttribute("name") != null) {

                System.out.println("允许访问");
                // 用户已登录，放行
                chain.doFilter(request, response);
                System.out.println("成功访问");
            } else {
                System.out.println("用户未登录");
                // 用户未登录，重定向到登录页面
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/Login.html");
            }
        }
    }

    @Override
    public void destroy() {
        // 清理资源
        System.out.println("过滤器已销毁");
    }

    private boolean isExcludedPath(String path) {
        for (String excluded : excludedPaths) {
            if (path.endsWith(excluded)) {
                return true;
            }
        }
        return false;
    }
}