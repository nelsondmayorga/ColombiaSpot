package com.colombiaspot.colombiaspot;

/**
 * Created by Equipo on 11/12/2017.
 */

public class Comentario {



    private String id;
    private String usuario;
    private String foto;
    private String mensaje;

    public Comentario(){
        super();
    }

    public Comentario(String id, String usuario, String foto, String mensaje){
        this.id = id;
        this.usuario = usuario;
        this.foto = foto;
        this.mensaje = mensaje;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
