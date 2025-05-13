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
        resp.setContentType("text/html;charset=UTF-8");

        // 获取表单提交的操作类型，假设表单中有一个名为 'action' 的隐藏字段
//        String action = req.getParameter("action");
//
//        if ("add".equals(action)) {
//            // 处理添加学生的逻辑
//            String studentName = req.getParameter("studentName");
//            int projectId = Integer.parseInt(req.getParameter("projectId"));
//
//            // 创建新的学生对象
//            Student newStudent = new Student();
//            newStudent.setStudentName(studentName);
//            newStudent.setProjectId(projectId);
//
//            // 调用 DAO 层方法添加学生
//            entityDAO.addStudent(newStudent);
//        } else if ("update".equals(action)) {
//            // 处理更新学生的逻辑
//            int studentId = Integer.parseInt(req.getParameter("studentId"));
//            String studentName = req.getParameter("studentName");
//            int projectId = Integer.parseInt(req.getParameter("projectId"));
//
//            // 创建更新后的学生对象
//            Student updatedStudent = new Student();
//            updatedStudent.setStudentId(studentId);
//            updatedStudent.setStudentName(studentName);
//            updatedStudent.setProjectId(projectId);
//
//            // 调用 DAO 层方法更新学生信息
//            entityDAO.updateStudent(updatedStudent);
//        }

        // 重定向到学生列表页面
        resp.sendRedirect(req.getContextPath() + "/student");
    }
}
