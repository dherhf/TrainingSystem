package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entity.User;
import org.example.service.SignInService;
import org.example.util.HibernateUtil;
import org.example.util.ThymeleafUtil;
import org.hibernate.Session;
import org.thymeleaf.context.Context;

import java.io.IOException;

@WebServlet(value = "/sign-in", loadOnStartup = 1)
public class SignInServlet extends HttpServlet {
    SignInService service;

    @Override
    public void init() throws ServletException {
        service = new SignInService();
        // 触发 Hibernate 预热
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.createQuery("FROM User WHERE name = :username", User.class)
                    .setParameter("username", "dummy_user")
                    .uniqueResult();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        if (req.getSession().getAttribute("SignIn-failed") != null) {
            context.setVariable("failed",true);
            req.getSession().removeAttribute("SignIn-failed");
        }
        if (req.getSession().getAttribute("user") != null){
            resp.sendRedirect("dashboard");
            return;
        }
        ThymeleafUtil.process("sign-in.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //首先设置一下响应类型
        resp.setContentType("text/html;charset=UTF-8");

        //获取POST请求携带的表单数据
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember-me");

        if (service.auth(username, password, req.getSession())) {
            resp.sendRedirect("dashboard");
        } else {
            req.getSession().setAttribute("SignIn-failed", true);
            this.doGet(req, resp);
        }
    }
}
