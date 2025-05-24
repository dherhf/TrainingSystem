package org.example.controller;

import org.example.EntityDAO;
import org.example.entity.Project;
import org.example.entity.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.example.util.ValidationUtil.isValidEmail;
import static org.example.util.ValidationUtil.isValidPassword;

@Controller
public class HelloController {
    EntityDAO entityDAO = new EntityDAO();

    @RequestMapping(value = "/sign-in")
    public ModelAndView showSignIn() {
        return new ModelAndView("sign-in");
    }

    @PostMapping(value = "/sign-in")
    public ModelAndView signIn(@RequestParam("username") String username, @RequestParam("password") String password) {
        ModelAndView modelAndView;
        if (entityDAO.auth(username, password)) {
            // 认证成功，跳转到主页
            modelAndView = new ModelAndView("home");
        } else {
            // 认证失败，返回登录页面并显示错误信息
            modelAndView = new ModelAndView("sign-in");
            modelAndView.addObject("failed", "true");
        }
        return modelAndView;

    }

    @RequestMapping("/sign-up")
    public ModelAndView signUp() {
        return new ModelAndView("sign-up");
    }

    @PostMapping("/sign-up")
    public ModelAndView signUp(@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("password") String password) {
        ModelAndView modelAndView;
        // 1. 校验用户名是否有效
        if (username == null || username.trim().isEmpty()) {
            modelAndView = new ModelAndView("sign-up");
            modelAndView.addObject("error", "注册失败：用户名不能为空");
            return modelAndView;
        }
        // 2. 校验密码是否有效
        if (!isValidPassword(password)) {
            modelAndView = new ModelAndView("sign-up");
            modelAndView.addObject("error", "注册失败：密码长度不能小于8且必须包含数字字母特殊符号");
            return modelAndView;
        }

        // 3. 检查用户名是否已存在
        if (entityDAO.isUsernameExists(username)) {
            modelAndView = new ModelAndView("sign-up");
            modelAndView.addObject("error", "注册失败：用户名已存在");
            return modelAndView;
        }

        // 4. 校验邮箱格式是否有效
        if (!isValidEmail(email)) {
            modelAndView = new ModelAndView("sign-up");
            modelAndView.addObject("error", "注册失败：邮箱格式无效");
            return modelAndView;
        }

        // 5. 检查邮箱是否已存在
        if (entityDAO.isEmailExists(email)) {
            modelAndView = new ModelAndView("sign-up");
            modelAndView.addObject("error", "注册失败：邮箱已存在");
            return modelAndView;
        }
        if (entityDAO.addUser(username, email, password)) {
            modelAndView = new ModelAndView("sign-in");
        } else {
            modelAndView = new ModelAndView("sign-up");
            modelAndView.addObject("error", "注册失败：服务器错误,请联系管理员");
        }
        return modelAndView;
    }

    @RequestMapping("/forgot-pws")
    public ModelAndView forgetPws() {
        return new ModelAndView("forgot-pws");
    }

    @RequestMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("home");
    }

    @RequestMapping("student")
    public ModelAndView student(@RequestParam(value = "projectName", required = false) String projectName,
                                @RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("student");
        try {
            int projectId = 1;
            if (projectName != null && !projectName.isEmpty()) {
                projectId = entityDAO.getProjectIdByName(projectName);
            }

            // 获取学生总数
            int totalStudents = entityDAO.getTotalStudentsByProjectId(projectId);
            int totalPages = (int) Math.ceil((double) totalStudents / pageSize);

            if (page > totalPages) {
                page = totalPages;
            }

            // 获取指定页码和每页数量的学生列表
            List<Student> students = entityDAO.getStudentsByPage(projectId, page, pageSize);
            // 获取项目列表
            List<Project> projects = entityDAO.getAllProject();
            // 将数据添加到 ModelAndView 中
            modelAndView.addObject("projectName", projectName);
            modelAndView.addObject("students", students);
            modelAndView.addObject("projects", projects);
            modelAndView.addObject("page", page);
            modelAndView.addObject("pageSize", pageSize);
            modelAndView.addObject("totalPages", totalPages);
        } catch (Exception e) {
            // 出现异常时添加错误信息
            modelAndView = new ModelAndView("500");
        }
        return modelAndView;
    }
}
