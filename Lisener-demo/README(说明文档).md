# 请求日志记录（ServletRequestListener）实现说明文档

## 项目概述
本项目包含两个主要的 Java 类，它们用于演示如何在 Java Web 应用中实现请求日志记录以及一个简单的测试 Servlet。
`RequestLoggingListener` 是一个实现了 `ServletRequestListener` 接口的监听器类，它会在每个 HTTP 请求开始和结束时被调用，并记录相关信息。
`TestServlet` 则是一个简单的 Servlet，用于响应 `/test` 路径的 GET 请求。



## 1. RequestLoggingListener 类

### 功能描述
`RequestLoggingListener` 监听器在每次 HTTP 请求初始化时记录请求开始的时间，在请求处理完成后输出一条日志信息到控制台。日志信息包括客户端 IP 地址、请求方法（如 GET 或 POST）、请求 URI、查询字符串、User-Agent 头信息以及请求处理所花费的时间。

### 关键代码解析
```java
@WebListener
public class RequestLoggingListener implements ServletRequestListener {
    private long startTime;

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        this.startTime = System.currentTimeMillis(); // 记录请求开始时间
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();

        String clientIp = request.getRemoteAddr(); // 获取客户端IP
        String method = request.getMethod(); // 获取请求方法
        String uri = request.getRequestURI(); // 获取请求URI
        String queryString = request.getQueryString(); // 获取查询字符串
        String userAgent = request.getHeader("User-Agent"); // 获取User-Agent
        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime; // 计算请求处理时间

        String logMessage = String.format(
            "请求时间: %s, 客户端IP: %s, 请求方法: %s, URI: %s, 查询字符串: %s, User-Agent: %s, 处理时间: %d ms",
            new Date(startTime), clientIp, method, uri, queryString, userAgent, processingTime
        );

        System.out.println(logMessage); // 输出日志信息
    }
}
```

- **`@WebListener` 注解**：标记这个类为一个 Web 应用程序的监听器。
- **`requestInitialized` 方法**：在请求初始化时调用，记录请求开始的时间。
- **`requestDestroyed` 方法**：在请求销毁时调用，计算并记录请求处理时间，并输出日志信息。

## 3. TestServlet 类

### 功能描述
`TestServlet` 是一个简单的 Servlet，当用户访问路径 `/test` 时，它会返回一个文本响应 "hello, World!"。

### 关键代码解析
```java
@WebServlet("/test")
public class TestServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("hello, World!"); // 返回简单的文本响应
    }
}
```

- **`@WebServlet("/test")` 注解**：指定该 Servlet 响应 `/test` 路径的请求。
- **`doGet` 方法**：处理 GET 请求，向客户端发送 "hello, World!" 的响应。

## 4. 实现效果
- 在浏览器中访问 `http://<your-server>/lisenerdemo/test`，将看到 "hello, World!" 的响应。
- 查看控制台日志，可以看到 `RequestLoggingListener` 输出的日志信息。

