package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.EntityDAO;
import org.example.entity.Project;
import org.example.entity.Student;
import org.example.util.ThymeleafUtil;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.List;

@WebServlet("/student")
public class StudentServlet extends HttpServlet {
    private EntityDAO entityDAO;
    private JakartaServletWebApplication application;
    @Override
    public void init() {
        entityDAO = new EntityDAO();
        application = JakartaServletWebApplication.buildApplication(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String projectIdParam = req.getParameter("projectId");
        System.out.println("test" + projectIdParam);
        List<Student> students;
        if (projectIdParam == null) {
            students = entityDAO.getStudentByProjectId(1);
        } else {
            students = entityDAO.getStudentByProjectId(Integer.parseInt(projectIdParam));
        }

        List<Project> projects = entityDAO.getAllProject();

        // Thymeleaf 上下文
        WebContext context = new WebContext(application.buildExchange(req, resp));
        context.setVariable("students", students);
        context.setVariable("projects", projects);
        context.setVariable("pageNumber", 1);
        context.setVariable("totalPages", 10);


        ThymeleafUtil.process("student.html", context, resp.getWriter());
    }
}
