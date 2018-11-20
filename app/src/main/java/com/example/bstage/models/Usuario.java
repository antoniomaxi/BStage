package com.example.bstage.models;

public class Usuario {

    private String _id;
    private String Name;
    private String Username;
    private String Imagen;
    private String Token;
    private String Correo;

    public Usuario(String _id, String name, String username, String imagen, String token, String correo) {
        this._id = _id;
        Name = name;
        Username = username;
        Imagen = imagen;
        Token = token;
        Correo = correo;
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

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
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
