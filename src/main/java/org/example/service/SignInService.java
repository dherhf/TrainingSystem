package org.example.service;

import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignInService {
    private static final Logger log = LoggerFactory.getLogger(SignInService.class);

    private User fetchUser(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE name = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
        } catch (Exception e) {
            log.error("查询用户失败: {}", username, e);
            return null;
        }
    }

    public boolean authenticate(String username, String password) {
        User user = fetchUser(username);
        if (user == null || !user.checkPassword(password)) {
            log.warn("用户密码或账号错误: {}", username);
            return false;
        } else {
            log.info("用户成功登录: {}", username);
            return true;
        }
    }
}