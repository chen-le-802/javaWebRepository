package com.example.login;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private String USERNAME = "admin";
    private String PASSWORD = "password";
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String context = request.getContextPath();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        response.setContentType("text/html;charset:UTF-8");
        PrintWriter out = response.getWriter();
        System.out.println(context);
        if(username.equals(USERNAME) && password.equals(PASSWORD)){
            session.setAttribute("name",username);
            session.setAttribute("pass",password);
            response.sendRedirect(context+"/index.html");
        }else{
            response.sendRedirect(context+"/Login.html");
        }
    }
}
