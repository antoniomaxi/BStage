package com.example.bstage;

public class Evento {

    private String Name;
    private String Lugar;
    private String Fecha;
    private String Imagen;
    private String Productor;
    private String Calificacion;
    private String Descripcion;
    private String Precio;
    private String Categoria;

    public Evento(String name, String lugar, String fecha,
                  String imagen, String productor, String calificacion,
                  String descripcion, String precio, String categoria) {

        Name = name;
        Lugar = lugar;
        Fecha = fecha;
        Imagen = imagen;
        Productor = productor;
        Calificacion = calificacion;
        Descripcion = descripcion;
        Precio = precio;
        Categoria = categoria;
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
