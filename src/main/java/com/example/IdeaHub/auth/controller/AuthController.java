package com.example.IdeaHub.auth.controller;

import com.example.IdeaHub.auth.model.ApplicationUser;
import com.example.IdeaHub.auth.model.LoginDao;
import com.example.IdeaHub.auth.service.AuthService;
import com.example.IdeaHub.message.ResponseMessage;
import io.swagger.annotations.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
@ApiOperation(value = "/api/auth",tags = "Auth Controller")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ApiResponse(code=200,message = "Signed up successfully")
    @ApiOperation(value="Sign ups new users")
    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> signup(@RequestBody ApplicationUser applicationUser){
        return authService.signup(applicationUser);
    }

    @ApiOperation(value = "login users using username and password and returns jwt token")
    @PostMapping("/login")
    @ApiResponse(code = 200,
            message = "Login Successful",
            response = String.class
    )
    public ResponseEntity<ResponseMessage> login(@RequestBody LoginDao loginDao){
        return authService.login(loginDao);
    }


    @ApiOperation(value = "logs out current user")
    @ApiResponse(code=200,message = "Successfully logged out")
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> logout(HttpServletRequest request, HttpServletResponse response){
        return authService.logout(request,response);
    }

    //getting all the authors for admin and publication_house
    //this is helpful while assigning reviewers
    @GetMapping("/authors")
    @ApiOperation(value = "return all available authors",response = Iterable.class)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN,ROLE_PUBLICATION_HOUSE')")
    public ResponseEntity<List<ApplicationUser>> getAllAuthors(){
        return authService.getAllAuthors();
    }

    @GetMapping("/user")
    @ApiOperation(value = "returns current user",response = ApplicationUser.class)
    public ResponseEntity<ApplicationUser> getCurrentApplicationUser(){
        return authService.getCurrentApplicationUser();
    }

}
