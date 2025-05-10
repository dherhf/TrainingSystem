package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.SignInService;

import java.io.IOException;
import java.util.Map;

@WebServlet(value = "/sign-in", loadOnStartup = 1)
public class SignInServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //首先设置一下响应类型
        resp.setContentType("text/html;charset=UTF-8");

        //获取POST请求携带的表单数据
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        SignInService signInService = new SignInService();
        if (signInService.authenticate(username,password)) {
            resp.sendRedirect("test.html");
        } else {
            resp.sendRedirect("/web");
        }
    }
}
