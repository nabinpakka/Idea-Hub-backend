package com.example.IdeaHub.config.security;

import com.example.IdeaHub.auth.service.ApplicationUserDetails;
import com.example.IdeaHub.auth.service.ApplicationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import springfox.documentation.swagger.web.SecurityConfiguration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private ApplicationUserDetailsService applicationUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromRequest(httpServletRequest);

        if(jwt != null && jwtProvider.validateToken(jwt)){
            String username = jwtProvider.getUsernameFromJwt(jwt);

            //loading user and setting authentication context
            ApplicationUserDetails applicationUserDetails = (ApplicationUserDetails) applicationUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(applicationUserDetails,null,applicationUserDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

            //this was missing when react witnessed 403 error
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        //this will execute other filters in the filter chain
        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }

    private String getJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        //checking if the bearer token is present and starts with Bearer
        if (bearerToken != null && bearerToken.startsWith("Bearer ")){
            //for token take all the strings after the space
            //i.e- all the strings after index 7
            return bearerToken.substring(7);
        }
        return bearerToken;

    }
}
