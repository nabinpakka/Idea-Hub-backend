package com.example.IdeaHub.config;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.IdeaHub.config.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    ADMIN(Sets.newHashSet(READ_ALL_PUBLICATION,READ_ALL_USERS)),
    AUTHOR(Sets.newHashSet(WRITE_PUBLICATION, READ_ALL_PUBLICATION,REVIEW)),
    PUBLICATION_HOUSE(Sets.newHashSet(READ_ALL_USERS,READ_ALL_PUBLICATION,ASSIGN_REVIEWER,LIST_SUBMITTED_PUBLICATION));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public  Set<ApplicationUserPermission> getPermissions(){
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return permissions;
    }
}