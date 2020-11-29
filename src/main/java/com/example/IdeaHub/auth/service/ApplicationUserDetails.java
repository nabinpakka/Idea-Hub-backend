package com.example.IdeaHub.auth.service;

import com.example.IdeaHub.auth.model.ApplicationUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static com.example.IdeaHub.config.ApplicationUserRole.*;

public class ApplicationUserDetails  implements UserDetails {

    private final ApplicationUser user;

    public ApplicationUserDetails(ApplicationUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.user.getRole().equals("ADMIN")){
            System.out.println(ADMIN.getGrantedAuthorities());
            return ADMIN.getGrantedAuthorities();
        }
        else if(this.user.getRole().equals("PUBLICATION_HOUSE")){
            System.out.println(PUBLICATION_HOUSE.getGrantedAuthorities());
            return PUBLICATION_HOUSE.getGrantedAuthorities();
        }
        else{
            System.out.println(AUTHOR.getGrantedAuthorities());
            return AUTHOR.getGrantedAuthorities();
        }
    }

    public ApplicationUser getApplicationUser(){
        return this.user;
    }

    public String getUserId(){
        return this.user.getUuid();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
