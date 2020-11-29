package com.example.IdeaHub.auth.service;

import com.example.IdeaHub.auth.model.ApplicationUser;
import com.example.IdeaHub.auth.model.LoginDao;
import com.example.IdeaHub.message.ResponseMessage;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AuthService {
    ResponseEntity<ResponseMessage> signup(ApplicationUser applicationUser);

    ResponseEntity<ResponseMessage> login(LoginDao loginDao);

    ResponseEntity<List<ApplicationUser>> getAllAuthors();

    ResponseEntity<ResponseMessage> logout(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<ApplicationUser> getCurrentApplicationUser();
}
