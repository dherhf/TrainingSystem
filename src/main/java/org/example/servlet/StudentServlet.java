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
        int projectId = projectIdParam != null ? Integer.parseInt(projectIdParam) : 1;
        String pageSizeParam = req.getParameter("pageSize");
        int pageSize = pageSizeParam != null ? Integer.parseInt(pageSizeParam) : 10;
        String pageNumberParam = req.getParameter("page");
        int pageNumber = pageNumberParam != null ? Integer.parseInt(pageNumberParam) : 1;

        // 防止越界
        int totalStudents = entityDAO.getTotalStudentsByProjectId(projectId);
        int totalPages = (int) Math.ceil((double) totalStudents / pageSize);

        if (pageNumber < 1) pageNumber = 1;
        if (pageNumber > totalPages) pageNumber = totalPages;

        List<Student> students = entityDAO.getStudentsByPage(projectId, pageNumber, pageSize);
        List<Project> projects = entityDAO.getAllProject();

        // Thymeleaf 上下文
        WebContext context = new WebContext(application.buildExchange(req, resp));
        context.setVariable("projectId", projectId);
        context.setVariable("students", students);
        context.setVariable("projects", projects);
        context.setVariable("pageNumber", pageNumber);
        context.setVariable("totalPages", totalPages);
        context.setVariable("pageSize", pageSize);


        ThymeleafUtil.process("student.html", context, resp.getWriter());
    }
}
