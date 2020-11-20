package com.example.IdeaHub.auth.service;


import com.example.IdeaHub.auth.model.ApplicationUser;
import com.example.IdeaHub.auth.model.LoginDao;
import com.example.IdeaHub.auth.repo.ApplicationUserRepo;
import com.example.IdeaHub.config.security.JwtProvider;
import com.example.IdeaHub.message.ResponseMessage;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService{

    private final ApplicationUserRepo applicationUserRepo;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

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


    //Jwt token is not blacklisted as it will be deleted in client side
    //blacklist maybe implemented in future
    @Override
    public ResponseEntity<ResponseMessage> logout(HttpServletRequest request, HttpServletResponse response){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth instanceof AnonymousAuthenticationToken || auth ==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("User not found"));
            }
            new SecurityContextLogoutHandler().logout(request,response,auth);
            SecurityContextHolder.getContext().setAuthentication(null);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Successfully logged out."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(e.getMessage()));
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
        List<ApplicationUser> authors ;
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
