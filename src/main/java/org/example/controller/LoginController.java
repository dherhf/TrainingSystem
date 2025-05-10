package org.example.controller;

import lombok.Getter;
import lombok.Setter;
import org.example.service.SignInService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

//    private final SignInService signInService;
//
//    public LoginController(SignInService signInService) {
//        this.signInService = signInService;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
//        String username = loginRequest.getUsername();
//        String password = loginRequest.getPassword();
//
//        // 调用服务层进行身份验证
//        boolean isAuthenticated = signInService.authenticate(username, password);
//
//        if (isAuthenticated) {
//            return ResponseEntity.ok("登录成功");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
//        }
//    }
}

// 请求体类
@Setter
@Getter
class LoginRequest {
    // Getters and Setters
    private String username;
    private String password;

}