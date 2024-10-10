# �û���¼ϵͳʵ��˵���ĵ�

## ���
����Ŀ�ṩ��һ���򵥵��û���¼���ܣ���ͨ��������ʵ���˷��ʿ��ơ�������������Ҫ�����`LoginServlet` ���ڴ����¼�����Լ� `LoginFilter` ��������δ��Ȩ�ķ��ʡ�

## ���˵��

### 1. LoginServlet
- **����**: �����û��ύ�ĵ�¼�����ݣ���֤�û��������롣
- **�ؼ�����**:

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

- **�ؼ���**:
    - ʹ��ע�� `@WebServlet(urlPatterns = "/login")` ����Servletӳ�䵽URL·�� `/login`��
    - Ԥ��������ȷ�ĵ��û��� (`USERNAME`) ������ (`PASSWORD`) Ϊ `"admin"` �� `"password"`��
    - �� `doPost` �����У�
        - ��ȡ�û��ĻỰ��`HttpSession`����
        - �������л�ȡ�û��ύ���û��������롣
        - ����û��ύ���û�����������Ԥ��ֵƥ�䣬�����ûỰ���Բ����û��ض�����ҳ (`/index.html`)��
        - �����ƥ�䣬���û��ض���ص�¼ҳ�� (`/Login.html`)��

### 2. LoginFilter
- **����**: ��������HTTP����ȷ��ֻ���ѵ�¼���û����ܷ����ܱ�������Դ��
- **�ؼ�����**:

```java
@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

    // �ų��б����������¼���ɷ��ʵ�·��
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

    // ���·���Ƿ����ų��б���
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

- **�ؼ���**:
    - ʹ��ע�� `@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")` ������URL���й��ˡ�
    - �����˲���Ҫ��¼���ɷ��ʵ�·���б� `excludedPaths`��
    - �� `doFilter` �����У�
        - ��鵱ǰ�����Ƿ������ų��б��е�·��������ǣ���ֱ�ӷ��С�
        - ���򣬼���Ƿ������Ч�Ự����������ԡ�������ڣ���������ʣ������ض��򵽵�¼ҳ�档

## ʵ�ֲ��裺
1. �û����Է����κ��ܱ�����ҳ�档
2. `LoginFilter` �������󲢼���û��Ƿ��Ѿ���¼�������URL�Ƿ����ų��б��С�
3. ����û�δ��¼�Ҳ����ų��б��ڣ������ض��򵽵�¼ҳ�档
4. �û��ڵ�¼ҳ�������û�����������ύ����
5. `LoginServlet` ���ձ����ݣ���֤�û��������롣
6. ����֤�ɹ��������Ự��������Ӧ�����ԣ�Ȼ���ض�������ҳ��
7. ����֤ʧ�ܣ��û��ᱻ�ٴ��ض���ص�¼ҳ�档
