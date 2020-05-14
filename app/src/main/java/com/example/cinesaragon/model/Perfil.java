package com.example.cinesaragon.model;

public class Perfil {


    private String email;
    private String nombre;
    private String apellidos;


    public Perfil(String email, String nombre, String apellidos){
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public Perfil(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
}
