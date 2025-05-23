package org.example.service;

import org.example.EntityDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUpService {
    private static final Logger log = LoggerFactory.getLogger(SignUpService.class);
    private static final EntityDAO entityDAO = new EntityDAO();
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
        if (entityDAO.isUsernameExists(username)) {
            log.warn("注册失败：用户名 {} 已存在", username);
            return false;
        }

        // 4. 校验邮箱格式是否有效
        if (!isValidEmail(email)) {
            log.warn("注册失败：邮箱 {} 格式无效", email);
            return false;
        }

        // 5. 检查邮箱是否已存在
        if (entityDAO.isEmailExists(email)) {
            log.warn("注册失败：邮箱 {} 已存在", email);
            return false;
        }

        // 6. 创建用户对象并保存到数据库
        return entityDAO.addUser(username, email, password);
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

}