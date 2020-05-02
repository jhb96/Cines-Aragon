package com.example.cinesaragon.model;

public class Cine {

    private String nombre;
    private String informacion;

    public Cine(String nombre, String info){

        this.nombre = nombre;
        this.informacion  = info;
    }
    @Override
    public String toString() {
        return nombre;
    }


}
