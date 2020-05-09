package com.example.cinesaragon.model;

import java.util.ArrayList;
import java.util.List;

public class Cine {



    private String nombre;
    private String direccion;
    private String telefono;

    private String horario;

    private List<Double> location;
    private List<Pelicula> cartelera;
    private List<Sala> salas;


    public Cine(){}

    public Cine(String nombre, String direccion, String telefono, String horario, Double lat, Double lng){

        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.horario = horario;
        this.location = new ArrayList<Double>();
        this.location.add(lat);
        this.location.add(lng);
        cartelera = new ArrayList<Pelicula>();
        salas = new ArrayList<Sala>();

    }

    @Override
    public String toString() {
        return nombre;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void addPelicula(Pelicula peli){
        this.cartelera.add(peli);
    }


    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }




    public List<Pelicula> getCartelera() {
        return cartelera;
    }

    public void setCartelera(List<Pelicula> cartelera) {
        this.cartelera = cartelera;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public void setSalas(List<Sala> salas) {
        this.salas = salas;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }



}
