package com.iesochoa.ejemplodbjavafx.model;
import java.util.List;

public class Departamento {
    private int codigo;
    private String nombre;
    private Empleado jefe; // Relación 1 a 1 (un jefe es una Empleado)
    private List<Empleado> empleados; // Relación 1 a N (un departamento tiene muchos empleados)

    // Constructores
    public Departamento(int codigo, String nombre, Empleado jefe) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.jefe = jefe;
    }
    public Departamento(int codigo, String nombre, Empleado jefe, List<Empleado> empleados) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.jefe = jefe;
        this.empleados = empleados;
    }

    // Getters y Setters
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Empleado getJefe() { return jefe; }
    public void setJefe(Empleado jefe) { this.jefe = jefe; }

    public List<Empleado> getEmpleados() { return empleados; }
    public void setEmpleados(List<Empleado> empleados) { this.empleados = empleados; }

    @Override
    public String toString() {
        return "Departamento{" +
                "codigo=" + codigo +
                ", nombre='" + nombre + '\'' +
                ", jefe=" + (jefe != null ? jefe.getNombre() : "N/A") +
                '}';
    }
}
