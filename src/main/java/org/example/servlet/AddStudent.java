package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.EntityDAO;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/addStudent")
public class AddStudent extends HttpServlet {
    private EntityDAO entityDAO;

    @Override
    public void init() throws ServletException {
        entityDAO = new EntityDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String studentName = req.getParameter("studentName");
        int projectId = entityDAO.getProjectIdByName(req.getParameter("projectName"));
        LocalDate registrationDate = LocalDate.parse(req.getParameter("registrationDate"));
        int tuition = Integer.parseInt(req.getParameter("tuition"));
        int grades = Integer.parseInt(req.getParameter("grades"));
        entityDAO.addStudent(id, projectId, studentName, registrationDate, tuition, grades);
        resp.sendRedirect(req.getContextPath() + "/student");
    }
}
