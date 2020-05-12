package com.example.cinesaragon.model;

import java.util.Date;

public class Ticket {




    private String userID;
    private String nombre;
    private String pelicula;
    private String tipo;
    private double coste;
    private String fecha;



    public Ticket(){}

    public Ticket(String userID, String nombre, String pelicula, String tipo, double coste, String fecha){
        this.userID = userID;
        this.nombre = nombre;
        this.pelicula = pelicula;
        this.tipo = tipo;
        this.coste = coste;
        this.fecha = fecha;

    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPelicula() {
        return pelicula;
    }

    public void setPelicula(String pelicula) {
        this.pelicula = pelicula;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getCoste() {
        return coste;
    }

    public void setCoste(double coste) {
        this.coste = coste;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
