package com.example.IdeaHub.auth.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "applicationUser")
public class ApplicationUser {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    private String uuid;

    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(128)")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    public ApplicationUser(String username, String password,
                           String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public ApplicationUser() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "ApplicationUser{" +
                "uuid='" + uuid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
