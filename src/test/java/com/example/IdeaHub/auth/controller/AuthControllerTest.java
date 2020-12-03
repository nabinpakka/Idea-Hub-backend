package com.example.IdeaHub.auth.controller;

import com.example.IdeaHub.auth.model.LoginDao;
import com.example.IdeaHub.auth.service.AuthService;
import com.example.IdeaHub.message.ResponseMessage;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

//    @Autowired
//    WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    public void login() throws Exception {
        LoginDao loginDao = new LoginDao();
        loginDao.setPassword("password");
        loginDao.setUsername("Nabin");

        String jwt = "455d45a5-asd45a-ads5fa4-asd4f";
        ResponseMessage responseMessage = new ResponseMessage(jwt);
        when(authService.login(loginDao)).thenReturn(ResponseEntity.status(HttpStatus.OK).body(responseMessage));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/login")
                .param(String.valueOf(loginDao))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }
}