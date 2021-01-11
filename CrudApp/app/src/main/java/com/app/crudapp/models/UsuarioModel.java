package com.app.crudapp.models;

import java.io.Serializable;

public class UsuarioModel implements Serializable {
    private int id;
    private String User;
    private String Password;

    public String getID() {
        return String.valueOf(id);
    }

    public void setID(int ID) {
        this.id = ID;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public UsuarioModel(int id, String user, String password) {
        this.id = id;
        User = user;
        Password = password;
    }

    public UsuarioModel()
    {

    }

    public UsuarioModel(String user, String password) {
        User = user;
        Password = password;
    }
}
