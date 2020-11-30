package com.example.doorlock;

public class User {
    private String password;
    private String username;
    private String email;

    public User() {
    }

    public User(String username2, String password2 , String email2) {
        this.username = username2;
        this.password = password2;
        this.email = email2;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username2) {
        this.username = username2;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password2) {
        this.password = password2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
