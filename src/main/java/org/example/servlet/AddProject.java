package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.EntityDAO;

import java.io.IOException;

@WebServlet("/addProject")
public class AddProject extends HttpServlet {
    private EntityDAO entityDAO;

    @Override
    public void init() throws ServletException {
        entityDAO = new EntityDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String projectName = req.getParameter("projectName");
        entityDAO.addProject(projectName);
        resp.sendRedirect(req.getContextPath() + "/student");
    }
}
