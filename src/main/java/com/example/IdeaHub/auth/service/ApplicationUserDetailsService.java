package com.example.IdeaHub.auth.service;

import com.example.IdeaHub.auth.model.ApplicationUser;
import com.example.IdeaHub.auth.repo.ApplicationUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    private final ApplicationUserRepo applicationUserRepo;

    @Autowired
    public ApplicationUserDetailsService(ApplicationUserRepo applicationUserRepo) {
        this.applicationUserRepo = applicationUserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepo.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("The user doesnot exists")
        );

        if (applicationUser != null){
            return new ApplicationUserDetails(
                    applicationUser
            );
        }
        else{
            throw new UsernameNotFoundException(username+" does not exists");
        }
    }


}
