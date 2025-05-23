package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.util.ThymeleafUtil;
import org.thymeleaf.context.Context;

import java.io.IOException;
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ThymeleafUtil.process("dashboard.html",new Context(), resp.getWriter());
    }
}
