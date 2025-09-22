package com.example.vitaconect_movil;

public class Producto {
    private String id;
    private String nombre;
    private String concentracion;
    private String adicional;
    private String precio;
    private String laboratorio;
    private String tipo;
    private String presentacion;
    private int stockTotal; // Nuevo campo para el stock total

    public Producto(String id, String nombre, String concentracion, String adicional, String precio, String laboratorio, String tipo, String presentacion, int stockTotal) {
        this.id = id;
        this.nombre = nombre;
        this.concentracion = concentracion;
        this.adicional = adicional;
        this.precio = precio;
        this.laboratorio = laboratorio;
        this.tipo = tipo;
        this.presentacion = presentacion;
        this.stockTotal = stockTotal;
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getConcentracion() { return concentracion; }
    public String getAdicional() { return adicional; }
    public String getPrecio() { return precio; }
    public String getLaboratorio() { return laboratorio; }
    public String getTipo() { return tipo; }
    public String getPresentacion() { return presentacion; }
    public int getStockTotal() { return stockTotal; } // Getter para stock total
}
/*package com.example.vitaconect_movil;

public class Producto {
    private String id; // Nuevo campo
    private String nombre;
    private String concentracion;
    private String adicional;
    private String precio;
    private String laboratorio;
    private String tipo;
    private String presentacion;

    public Producto(String id, String nombre, String concentracion, String adicional, String precio,String laboratorio, String tipo, String presentacion) {
        this.id = id;
        this.nombre = nombre;
        this.concentracion = concentracion;
        this.adicional = adicional;
        this.precio = precio;
        this.laboratorio = laboratorio;
        this.tipo = tipo;
        this.presentacion = presentacion;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public String getAdicional() {
        return adicional;
    }

    public String getPrecio() {
        return precio;
    }


    public String getLaboratorio() {
        return laboratorio;
    }

    public String getTipo() {
        return tipo;
    }

    public String getPresentacion() {
        return presentacion;
    }
}*/
