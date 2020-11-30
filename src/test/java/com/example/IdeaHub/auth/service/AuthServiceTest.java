package com.example.IdeaHub.auth.service;

import com.example.IdeaHub.auth.model.ApplicationUser;
import com.example.IdeaHub.auth.model.LoginDao;
import com.example.IdeaHub.auth.model.repo.ApplicationUserRepo;
import com.example.IdeaHub.config.security.JwtProvider;
import com.example.IdeaHub.message.ResponseMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    private ApplicationUserRepo applicationUserRepo;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager ;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private Authentication authentication;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void signup(){
        String fakeEncodedPassword="@34a5r4435q4ar";
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        when(passwordEncoder.encode("password")).thenReturn(fakeEncodedPassword);

        ResponseEntity<ResponseMessage> response = authService.signup(applicationUser);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).isEqualTo("Registration Successful");
    }

    @Test
    public void login(){
        LoginDao loginDao = mock(LoginDao.class);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = mock(UsernamePasswordAuthenticationToken.class);

        String fakeJwt = "asdkfjakjfkajalka-daofjialkjkfakfakfdakdfadfakj";
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDao.getUsername(),
                loginDao.getPassword()
        ))).thenReturn(authentication);

        when(jwtProvider.generateToken(authentication)).thenReturn(fakeJwt);

        ResponseEntity<ResponseMessage> response = authService.login(loginDao);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).isEqualTo(fakeJwt);
    }

    @Test
    public void getAllAuthors(){
        List<ApplicationUser> authors= new ArrayList<>();
        authors.add(new ApplicationUser("nabin","password","AUTHOR"));
        authors.add(new ApplicationUser("nabin1","password","AUTHOR"));

        when(applicationUserRepo.findAllByRole("AUTHOR")).thenReturn(authors);
        ResponseEntity<List<ApplicationUser>> response = authService.getAllAuthors();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get(0)).isInstanceOf(ApplicationUser.class);
        assertThat(response.getBody().get(0).getRole()).isEqualTo("AUTHOR");

    }

    @Test
    public void getCurrentApplicationUser(){
        ApplicationUser applicationUser = mock(ApplicationUser.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        ApplicationUserDetails applicationUserDetails = mock(ApplicationUserDetails.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUserDetails);
        when(applicationUserDetails.getApplicationUser()).thenReturn(applicationUser);

        ResponseEntity<ApplicationUser> response= authService.getCurrentApplicationUser();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(ApplicationUser.class);



    }
}
