package com.example.vitaconect_movil;

public class Product {
    private String nombre;
    private int cantidad;
    private double subtotal;

    // Constructor
    public Product(String nombre, int cantidad, double subtotal) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    // Puedes agregar setters si necesitas modificar los valores
}
