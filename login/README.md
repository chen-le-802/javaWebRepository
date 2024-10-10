# 用户登录系统实现说明文档

## 简介
本项目提供了一个简单的用户登录功能，并通过过滤器实现了访问控制。它包含两个主要组件：`LoginServlet` 用于处理登录请求，以及 `LoginFilter` 用于拦截未授权的访问。

## 组件说明

### 1. LoginServlet
- **作用**: 处理用户提交的登录表单数据，验证用户名和密码。
- **关键代码**:

```java
@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private String USERNAME = "admin";
    private String PASSWORD = "password";

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username.equals(USERNAME) && password.equals(PASSWORD)) {
            session.setAttribute("name", username);
            session.setAttribute("pass", password);
            response.sendRedirect(request.getContextPath() + "/index.html");
        } else {
            response.sendRedirect(request.getContextPath() + "/Login.html");
        }
    }
}
```

- **关键点**:
    - 使用注解 `@WebServlet(urlPatterns = "/login")` 将该Servlet映射到URL路径 `/login`。
    - 预定义了正确的的用户名 (`USERNAME`) 和密码 (`PASSWORD`) 为 `"admin"` 和 `"password"`。
    - 在 `doPost` 方法中：
        - 获取用户的会话（`HttpSession`）。
        - 从请求中获取用户提交的用户名和密码。
        - 如果用户提交的用户名和密码与预设值匹配，则设置会话属性并将用户重定向到主页 (`/index.html`)。
        - 如果不匹配，则将用户重定向回登录页面 (`/Login.html`)。

### 2. LoginFilter
- **作用**: 拦截所有HTTP请求，确保只有已登录的用户才能访问受保护的资源。
- **关键代码**:

```java
@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

    // 排除列表，包含无需登录即可访问的路径
    private final String[] excludedPaths = {"/Login.html", "/login"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        if (isExcludedPath(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
        } else {
            if (session != null && session.getAttribute("name") != null) {
                chain.doFilter(request, response);
            } else {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/Login.html");
            }
        }
    }

    // 检查路径是否在排除列表中
    private boolean isExcludedPath(String path) {
        for (String excluded : excludedPaths) {
            if (path.endsWith(excluded)) {
                return true;
            }
        }
        return false;
    }
}
```

- **关键点**:
    - 使用注解 `@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")` 对所有URL进行过滤。
    - 定义了不需要登录即可访问的路径列表 `excludedPaths`。
    - 在 `doFilter` 方法中：
        - 检查当前请求是否属于排除列表中的路径。如果是，则直接放行。
        - 否则，检查是否存在有效会话及其相关属性。如果存在，则允许访问；否则，重定向到登录页面。

## 实现步骤：
1. 用户尝试访问任何受保护的页面。
2. `LoginFilter` 拦截请求并检查用户是否已经登录或请求的URL是否在排除列表中。
3. 如果用户未登录且不在排除列表内，将被重定向到登录页面。
4. 用户在登录页面输入用户名和密码后提交表单。
5. `LoginServlet` 接收表单数据，验证用户名和密码。
6. 若验证成功，创建会话并设置相应的属性，然后重定向至主页。
7. 若验证失败，用户会被再次重定向回登录页面。
