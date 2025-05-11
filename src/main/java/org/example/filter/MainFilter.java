package org.example.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class MainFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String url = req.getRequestURL().toString();
        // 检查请求 URL 是否需要跳过过滤
        if (!shouldSkipFilter(url)) {
            HttpSession session = req.getSession(false); // 获取现有会话，避免创建新会话
            if (session == null || session.getAttribute("user") == null) {
                // 如果会话不存在或未登录，重定向到登录页面
                res.sendRedirect("sign-in");
                return; // 确保后续逻辑不会执行
            }
        }
        chain.doFilter(req, res);
    }

    /**
     * 判断是否应该跳过过滤
     *
     * @param url 请求的完整 URL
     * @return 如果 URL 应该跳过过滤，则返回 true；否则返回 false
     */
    private boolean shouldSkipFilter(String url) {
        return url.contains("https://cdn.tailwindcss.com/")
                || url.contains("/static")
                || url.endsWith("sign-in") 
                || url.endsWith("forgot-pws.html") 
                || url.endsWith("sign-up.html");
    }
}