package org.example.servlet;

import jakarta.servlet.ServletException;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
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
        String projectNameParam = req.getParameter("projectName");
        String projectName = projectNameParam != null ? projectNameParam : entityDAO.getProjectNameById(1);

        int projectId = entityDAO.getProjectIdByName(projectName);
        String pageSizeParam = req.getParameter("pageSize");
        int pageSize = pageSizeParam != null ? Integer.parseInt(pageSizeParam) : 10;
        String pageParam = req.getParameter("page");
        int page = pageParam != null ? Integer.parseInt(pageParam) : 1;

        // 防止越界
        int totalStudents = entityDAO.getTotalStudentsByProjectId(projectId);

        int totalPages = (int) Math.ceil((double) totalStudents / pageSize);


        if (page > totalPages) page = totalPages;
        if (page < 1) page = 1;

        List<Student> students = entityDAO.getStudentsByPage(projectId, page, pageSize);
        List<Project> projects = entityDAO.getAllProject();

        // Thymeleaf 上下文
        WebContext context = new WebContext(application.buildExchange(req, resp));
        context.setVariable("projectId", projectId);
        context.setVariable("students", students);
        context.setVariable("projects", projects);
        context.setVariable("page", page);
        context.setVariable("totalPages", totalPages);
        context.setVariable("pageSize", pageSize);
        context.setVariable("projectName", projectName);

        ThymeleafUtil.process("student.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 设置请求和响应的字符编码，防止中文乱码
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        // 获取操作类型
        String method = req.getParameter("method");

        if ("deleteStudent".equals(method)) {
            // 获取学生ID和项目名称
            String idParam = req.getParameter("id");
            String projectName = req.getParameter("projectName");
            int projectId = entityDAO.getProjectIdByName(projectName);
            String page = req.getParameter("page");
            String pageSize = req.getParameter("pageSize");

            try {

                entityDAO.deleteStudent(idParam, projectId);
                String redirectUrl = String.format(
                        "%s/student?projectName=%s&page=%s&pageSize=%s",
                        req.getContextPath(),
                        URLEncoder.encode(projectName, StandardCharsets.UTF_8),
                        page,
                        pageSize
                );
                // 删除成功后重定向回原项目页面
                resp.sendRedirect(redirectUrl);
            } catch (Exception e) {
                // 记录错误并跳转错误页或提示失败
                req.setAttribute("error", "删除失败：" + e.getMessage());
                try {
                    req.getRequestDispatcher("/error.jsp").forward(req, resp);
                } catch (ServletException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if ("addProject".equals(method)) {
            String projectName = req.getParameter("projectName");
            entityDAO.addProject(projectName);
            String page = req.getParameter("page");
            String pageSize = req.getParameter("pageSize");
            String redirectUrl = String.format(
                    "%s/student?projectName=%s&page=%s&pageSize=%s",
                    req.getContextPath(),
                    URLEncoder.encode(projectName, StandardCharsets.UTF_8),
                    page,
                    pageSize
            );
            // 删除成功后重定向回原项目页面
            resp.sendRedirect(redirectUrl);
        } else if ("addStudent".equals(method)) {
            String id = req.getParameter("id");
            String studentName = req.getParameter("studentName");
            String projectName = req.getParameter("projectName");
            int projectId = entityDAO.getProjectIdByName(projectName);
            LocalDate registrationDate = LocalDate.parse(req.getParameter("registrationDate"));
            int tuition = Integer.parseInt(req.getParameter("tuition"));
            int grades = Integer.parseInt(req.getParameter("grades"));
            entityDAO.addStudent(id, projectId, studentName, registrationDate, tuition, grades);

            String page = req.getParameter("page");
            String pageSize = req.getParameter("pageSize");
            String redirectUrl = String.format(
                    "%s/student?projectName=%s&page=%s&pageSize=%s",
                    req.getContextPath(),
                    URLEncoder.encode(projectName, StandardCharsets.UTF_8),
                    page,
                    pageSize
            );
            // 删除成功后重定向回原项目页面
            resp.sendRedirect(redirectUrl);

        }
    }
}
