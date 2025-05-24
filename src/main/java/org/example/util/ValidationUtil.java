package org.example.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationUtil {
    private static final Logger log = LoggerFactory.getLogger(ValidationUtil.class);

    /**
     * 校验邮箱格式是否有效
     *
     * @param email 待校验的邮箱字符串
     * @return 邮箱格式有效返回 true，否则返回 false
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    /**
     * 校验密码是否有效，密码长度不能小于8且必须包含数字、字母和特殊符号
     *
     * @param password 待校验的密码字符串
     * @return 密码有效返回 true，否则返回 false
     */
    public static boolean isValidPassword(String password) {
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
