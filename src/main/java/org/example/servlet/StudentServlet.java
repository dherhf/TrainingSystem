package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.EntityDAO;
import org.example.entity.Student;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class StudentServlet extends HttpServlet {

    TemplateEngine engine;
    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();
        //设定模板解析器决定了从哪里获取模板文件，这里直接使用ClassLoaderTemplateResolver表示加载内部资源文件
        ClassLoaderTemplateResolver r = new ClassLoaderTemplateResolver();
        r.setPrefix("/templates/");
        r.setSuffix(".html");
        r.setTemplateMode("HTML");

        engine.setTemplateResolver(r);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EntityDAO entityDAO = new EntityDAO();
        List<Student> students = entityDAO.getAllStudent();
        //创建上下文，上下文中包含了所有需要替换到模板中的内容
        Context context = new Context();
        context.setVariable("students", students);
        // 处理模板
        engine.process("dashboard", context, resp.getWriter());
    }
}