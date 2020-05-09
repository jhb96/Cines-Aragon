package com.example.cinesaragon.model;

public class Pelicula {

    private String nombre;
    private String genero;
    private String informacion;
    private String director;



    private String descripción;
    private String fechaEstreno;
    private String duración;

    public Pelicula(){}

    public Pelicula(String nombre){
        this.nombre = nombre;

    }

    @Override
    public String toString(){
        return "CustomerModel{" +
                "nombre=" + nombre +
                ", genero" + genero +
                ", director" + director + "}";
    }




    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }


    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDescripción() {
        return descripción;
    }

    public void setDescripción(String descripción) {
        this.descripción = descripción;
    }

    public String getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(String fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public String getDuración() {
        return duración;
    }

    public void setDuración(String duración) {
        this.duración = duración;
    }
}
