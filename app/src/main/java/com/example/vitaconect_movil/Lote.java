package com.example.vitaconect_movil;

public class Lote {
    private int id;
    private String nombre;
    private int stock;
    private String vencimiento;
    private String concentracion;
    private String adicional;
    private String laboratorio;
    private String tipo;
    private String presentacion;
    private String proveedor;
    private String estadoVencimiento; // Campo agregado

    public Lote(int id, String nombre, int stock, String vencimiento, String concentracion, String adicional,
                String laboratorio, String tipo, String presentacion, String proveedor, String estadoVencimiento) {
        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.vencimiento = vencimiento;
        this.concentracion = concentracion;
        this.adicional = adicional;
        this.laboratorio = laboratorio;
        this.tipo = tipo;
        this.presentacion = presentacion;
        this.proveedor = proveedor;
        this.estadoVencimiento = estadoVencimiento; // Asigna el valor en el constructor
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getStock() { return stock; }
    public String getVencimiento() { return vencimiento; }
    public String getConcentracion() { return concentracion; }
    public String getAdicional() { return adicional; }
    public String getLaboratorio() { return laboratorio; }
    public String getTipo() { return tipo; }
    public String getPresentacion() { return presentacion; }
    public String getProveedor() { return proveedor; }
    public String getEstadoVencimiento() { return estadoVencimiento; } // Getter agregado
}
