package com.example.IdeaHub.auth.controller;

import com.example.IdeaHub.auth.model.ApplicationUser;
import com.example.IdeaHub.auth.model.LoginDao;
import com.example.IdeaHub.auth.service.AuthService;
import com.example.IdeaHub.message.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

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

    //getting all the authors for admin and publication_house
    //this is helpful while assigning reviewers
    @GetMapping("/getAllAuthors")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_PUBLICATION_HOUSE')")
    public ResponseEntity<List<ApplicationUser>> getAllAuthors(){
        return authService.getAllAuthors();
    }

}
