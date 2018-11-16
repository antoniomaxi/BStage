package com.example.bstage.models;

public class Local {

    private String _id;
    private String Name;
    private String Direccion;
    private String Calificacion;
    private String Descripcion;
    private String Precio;
    private String Categoria;
    private String Imagen;
    private String Contador;
    private String Acumulador;


    public Local(){

    }

    public Local(String ID, String name, String direccion, String calificacion, String descripcion, String precio, String categoria,
                 String imagen, String contador, String acumulador) {

        _id = ID;
        Name = name;
        Direccion = direccion;
        Calificacion = calificacion;
        Descripcion = descripcion;
        Precio = precio;
        Categoria = categoria;
        Imagen = imagen;
        Contador = contador;
        Acumulador = acumulador;

    }

    public String getContador() {
        return Contador;
    }

    public void setContador(String contador) {
        Contador = contador;
    }

    public String getAcumulador() {
        return Acumulador;
    }

    public void setAcumulador(String acumulador) {
        Acumulador = acumulador;
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

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getCalificacion() {
        return Calificacion;
    }

    public void setCalificacion(String calificacion) {
        Calificacion = calificacion;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }
}
