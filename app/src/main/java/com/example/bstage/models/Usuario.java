package com.example.bstage.models;

public class Usuario {

    private String _id;
    private String Name;
    private String Token;
    private String Correo;

    public Usuario(String _id, String name, String token, String correo) {
        this._id = _id;
        Name = name;
        Token = token;
        Correo = correo;
    }

    public Usuario() {

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }
}
