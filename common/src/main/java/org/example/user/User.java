package org.example.user;

import java.io.Serializable;

public class User implements Serializable {
    private String login;
    private String hashPassword;

    public User(String login, String hashPassword) {
        this.login = login;
        this.hashPassword = hashPassword;
    }

    public String getLogin() {
        return login;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", hashPassword='" + hashPassword + '\'' +
                '}';
    }
}
