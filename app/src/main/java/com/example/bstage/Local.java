package com.example.bstage;

public class Local {

    private String Name;
    private String Direccion;
    private String Calificacion;
    private String Descripcion;
    private String Precio;
    private String Categoria;
    private String Imagen;

    public Local(String name, String direccion, String calificacion,
                 String descripcion, String precio, String categoria,
                 String imagen) {

        Name = name;
        Direccion = direccion;
        Calificacion = calificacion;
        Descripcion = descripcion;
        Precio = precio;
        Categoria = categoria;
        Imagen = imagen;
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
