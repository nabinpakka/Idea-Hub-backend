package com.example.IdeaHub.auth.model;

public class LoginDao {

    private String username;

    private String password;

    public LoginDao(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginDao() {
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
}
