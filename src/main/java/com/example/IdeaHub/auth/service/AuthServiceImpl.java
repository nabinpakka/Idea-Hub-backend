package com.example.IdeaHub.auth.service;


import com.example.IdeaHub.auth.model.ApplicationUser;
import com.example.IdeaHub.auth.model.LoginDao;
import com.example.IdeaHub.auth.repo.ApplicationUserRepo;
import com.example.IdeaHub.config.security.JwtProvider;
import com.example.IdeaHub.message.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService{

    private ApplicationUserRepo applicationUserRepo;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private JwtProvider jwtProvider;

    public AuthServiceImpl(ApplicationUserRepo applicationUserRepo,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtProvider jwtProvider) {
        this.applicationUserRepo = applicationUserRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public ResponseEntity<ResponseMessage> signup(ApplicationUser applicationUser){
        try{
            //encoding password
            String password = applicationUser.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            applicationUser.setPassword(encodedPassword);
            applicationUserRepo.save(applicationUser);

            String message="Registration Successful";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

        } catch (Exception e) {
            e.printStackTrace();
            String message="Registration Failed/n" + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> login(LoginDao loginDao){
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDao.getUsername(),
                    loginDao.getPassword()
            ));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(jwtProvider.generateToken(authentication)));
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(e.getMessage()));
        }
    }


    @Override
    public ResponseEntity<List<ApplicationUser>> getAllAuthors() {
        List<ApplicationUser> authors = new ArrayList<ApplicationUser>();
        try{
            authors = applicationUserRepo.findAllByRole("AUTHOR");

            if(authors.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(authors);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
