package com.example.lisener.lisenerdemo;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

@WebListener
public class RequestLoggingListener implements ServletRequestListener {
    private long startTime;

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        // 记录请求开始时间
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();

        // 获取请求相关信息
        String clientIp = request.getRemoteAddr();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String userAgent = request.getHeader("User-Agent");
        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;

        // 日志格式化
        String logMessage = String.format(
                "请求时间: %s, 客户端IP: %s, 请求方法: %s, URI: %s, 查询字符串: %s, User-Agent: %s, 处理时间: %d ms",
                new Date(startTime), clientIp, method, uri, queryString, userAgent, processingTime
        );

        // 输出日志
        System.out.println(logMessage);
    }
}
