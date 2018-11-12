package com.example.bstage.models;

public class Evento {

    private String _id;
    private String Name;
    private String Lugar;
    private String Fecha;
    private String Imagen;
    private String Productor;
    private String Calificacion;
    private String Descripcion;
    private String Precio;
    private String Categoria;
    private String Contador;

    public Evento(){

    }

    public Evento(String ID, String name, String lugar, String fecha, String imagen, String productor, String calificacion, String descripcion,
                  String precio, String categoria, String contador) {

        _id = ID;
        Name = name;
        Lugar = lugar;
        Fecha = fecha;
        Imagen = imagen;
        Productor = productor;
        Calificacion = calificacion;
        Descripcion = descripcion;
        Precio = precio;
        Categoria = categoria;
        Contador = contador;
    }

    public String getContador() {
        return Contador;
    }

    public void setContador(String contador) {
        Contador = contador;
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

    public String getLugar() {
        return Lugar;
    }

    public void setLugar(String lugar) {
        Lugar = lugar;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getProductor() {
        return Productor;
    }

    public void setProductor(String productor) {
        Productor = productor;
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
}
