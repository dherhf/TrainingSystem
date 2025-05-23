package org.example.service;

import jakarta.servlet.http.HttpSession;
import org.example.EntityDAO;
import org.example.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignInService {
    private static final Logger log = LoggerFactory.getLogger(SignInService.class);
    private static final EntityDAO entityDAO = new EntityDAO();

    public boolean auth(String username, String password, HttpSession session) {
        User user = entityDAO.fetchUser(username);
        if (user == null || !user.checkPassword(password)) {
            log.warn("用户密码或账号错误");
            return false;
        } else {
            session.setAttribute("user", user);
            log.info("用户成功登录");
            return true;
        }

    }
}