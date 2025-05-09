package org.example.service;

import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUpService {
    private static final Logger log = LoggerFactory.getLogger(SignUpService.class);

    public boolean signUpUser(String username, String email, String password) {
        // 1. 校验用户名是否有效
        if (username == null || username.trim().isEmpty()) {
            log.warn("注册失败：用户名不能为空");
            return false;
        }
        // 2. 校验密码是否有效
        if (!isValidPassword(password)) {
            log.warn("注册失败：密码长度不能小于8且必须包含数字字母特殊符号");
            return false;
        }

        // 3. 检查用户名是否已存在
        if (isUsernameExists(username)) {
            log.warn("注册失败：用户名 {} 已存在", username);
            return false;
        }

        // 4. 校验邮箱格式是否有效
        if (!isValidEmail(email)) {
            log.warn("注册失败：邮箱 {} 格式无效", email);
            return false;
        }

        // 5. 检查邮箱是否已存在
        if (isEmailExists(email)) {
            log.warn("注册失败：邮箱 {} 已存在", email);
            return false;
        }

        // 6. 创建用户对象并保存到数据库
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // 创建用户实体
            User newUser = new User(username, email, password);

            // 保存用户
            session.persist(newUser);

            transaction.commit();
            log.info("用户 {} 注册成功", username);
            return true;
        } catch (Exception e) {
            log.error("用户 {} 注册失败: {}", username, e.getMessage(), e);
            return false;
        }
    }
    
    private boolean isUsernameExists(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(*) FROM User WHERE name = :username", Long.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            log.error("用户名 {} 已存在", username, e);
            return false;
        }
    }

    private boolean isEmailExists(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(*) FROM User WHERE email = :email", Long.class)
                    .setParameter("email", email)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            log.error("邮箱 {} 已存在", email, e);
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasDigit = false;
        boolean hasLetter = false;
        boolean hasSpecialChar = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }
        return hasDigit && hasLetter && hasSpecialChar;
    }

    public static void main(String[] args) {
        SignUpService service = new SignUpService();
        service.signUpUser("admin","dherhf@outlook.com","www.2005");
    }
}