package com.iesochoa.ejemplodbjavafx.model;

public class Empleado {
    private int id;
    private String dni;
    private String nombre;
    private String apellidos;
    private int edad;
    private Departamento departamento; // Relación N a 1

    // Constructor
    public Empleado(int id, String dni, String nombre, String apellido, int edad, Departamento departamento) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellido;
        this.edad = edad;
        this.departamento = departamento;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getNombreCompleto() { return nombre + " " + apellidos; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento departamento) { this.departamento = departamento; }

    @Override
    public String toString() {
        return "Empleado{" +
                "id=" + id +
                ", dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellidos + '\'' +
                ", edad=" + edad +
                ", departamento=" + (departamento != null ? departamento.getNombre() : "Sin asignar") +
                '}';
    }
}
