package com.example.vitaconect_movil;

public class Presentacion {
    private int id;
    private String nombre;

    // Constructor con todos los parámetros
    public Presentacion(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Constructor vacío (si necesitas crear el objeto sin inicializar)
    public Presentacion() {}

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método toString para depuración o mostrar datos como texto
    @Override
    public String toString() {
        return "Presentacion{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
