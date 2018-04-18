package com.colombiaspot.colombiaspot;

/**
 * Created by Equipo on 1/06/2017.
 */

public class Puntos {

    private String id;
    private String nombre;
    private String descripcion;
    private String tipo;
    private String imagen;
    private String coordenadas;

    public Puntos(){
        super();
    }

    public Puntos(String id, String nombre, String descripcion, String tipo, String imagen, String coordenadas) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.imagen = imagen;
        this.coordenadas = coordenadas;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }
}
