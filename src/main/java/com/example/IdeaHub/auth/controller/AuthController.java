package com.example.IdeaHub.auth.controller;

import com.example.IdeaHub.auth.model.ApplicationUser;
import com.example.IdeaHub.auth.model.LoginDao;
import com.example.IdeaHub.auth.service.AuthService;
import com.example.IdeaHub.message.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> signup(@RequestBody ApplicationUser applicationUser){
        return authService.signup(applicationUser);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@RequestBody LoginDao loginDao){
        return authService.login(loginDao);
    }
}
