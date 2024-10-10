package com.example.login;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;

@WebServlet(urlPatterns = "/hello")
public class helloServlet extends HttpServlet {
    public void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws jakarta.servlet.ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<html><body><h1>Hello, World!</h1></body></html>");
    }
}

