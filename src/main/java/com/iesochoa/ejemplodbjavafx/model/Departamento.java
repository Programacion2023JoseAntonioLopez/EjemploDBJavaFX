package com.iesochoa.ejemplodbjavafx.model;
import java.util.List;

/**
 * La clase {@code Departamento} representa un departamento dentro de la organización.
 * Contiene información sobre el código del departamento, su nombre, el jefe asignado
 * y la lista de empleados que pertenecen a este departamento.
 */
public class Departamento {
    /**
     * Identificador único del departamento en la base de datos.
     */
    private int codigo;
    /**
     * Nombre del departamento.
     */
    private String nombre;
    /**
     * Empleado que es el jefe de este departamento.
     * Representa una relación Uno a Uno (1:1) donde un departamento tiene un jefe
     * que es una instancia de la clase {@code Empleado}. Puede ser {@code null}
     * si el departamento no tiene un jefe asignado.
     */
    private Empleado jefe; // Relación 1 a 1 (un jefe es una Empleado)
    /**
     * Lista de empleados que pertenecen a este departamento.
     * Representa una relación Uno a Muchos (1:N) donde un departamento puede tener
     * múltiples instancias de la clase {@code Empleado} asociadas.
     */
    private List<Empleado> empleados; // Relación 1 a N (un departamento tiene muchos empleados)

    /**
     * Constructor para crear una nueva instancia de la clase {@code Departamento}.
     * Inicializa el código, nombre y jefe del departamento. La lista de empleados
     * se inicializa implícitamente como {@code null}.
     *
     * @param codigo El código único del departamento.
     * @param nombre El nombre del departamento.
     * @param jefe   El empleado que es el jefe del departamento (puede ser {@code null}).
     */
    public Departamento(int codigo, String nombre, Empleado jefe) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.jefe = jefe;
    }

    /**
     * Constructor para crear una nueva instancia de la clase {@code Departamento}.
     * Inicializa el código, nombre, jefe y la lista de empleados del departamento.
     *
     * @param codigo    El código único del departamento.
     * @param nombre    El nombre del departamento.
     * @param jefe      El empleado que es el jefe del departamento (puede ser {@code null}).
     * @param empleados La lista de empleados que pertenecen al departamento.
     */
    public Departamento(int codigo, String nombre, Empleado jefe, List<Empleado> empleados) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.jefe = jefe;
        this.empleados = empleados;
    }

    /**
     * Obtiene el código único del departamento.
     *
     * @return El código del departamento.
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Establece el código único del departamento.
     *
     * @param codigo El nuevo código del departamento.
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene el nombre del departamento.
     *
     * @return El nombre del departamento.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del departamento.
     *
     * @param nombre El nuevo nombre del departamento.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el jefe del departamento.
     *
     * @return El empleado que es el jefe del departamento (puede ser {@code null}).
     */
    public Empleado getJefe() {
        return jefe;
    }

    /**
     * Establece el jefe del departamento.
     *
     * @param jefe El nuevo jefe del departamento (puede ser {@code null}).
     */
    public void setJefe(Empleado jefe) {
        this.jefe = jefe;
    }

    /**
     * Obtiene la lista de empleados que pertenecen al departamento.
     *
     * @return La lista de empleados del departamento (puede ser {@code null} o vacía).
     */
    public List<Empleado> getEmpleados() {
        return empleados;
    }

    /**
     * Establece la lista de empleados que pertenecen al departamento.
     *
     * @param empleados La nueva lista de empleados del departamento.
     */
    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    /**
     * Devuelve una representación en cadena del objeto {@code Departamento}.
     * Actualmente, devuelve una cadena con el formato "codigo- nombre".
     *
     * @return Una cadena que representa el departamento.
     */
    @Override
    public String toString() {
        return codigo + "- " + nombre;
    }
}