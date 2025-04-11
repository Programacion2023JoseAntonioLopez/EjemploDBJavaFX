package com.iesochoa.ejemplodbjavafx.db;

import com.iesochoa.ejemplodbjavafx.model.Departamento;
import com.iesochoa.ejemplodbjavafx.model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    // Parámetros de conexión a la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/empresa?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";             // Cambia según tu configuración
    private static final String PASSWORD = "tu_password";    // Cambia según tu configuración

    // Instancia única de EmpleadoDAO (Singleton)
    private static volatile EmpleadoDAO instance;

    // Constructor privado para evitar instanciación externa
    private EmpleadoDAO() { }

    // Método para obtener la única instancia de EmpleadoDAO
    public static EmpleadoDAO getInstance() {
        if (instance == null) {
            synchronized (EmpleadoDAO.class) {
                if (instance == null) {
                    instance = new EmpleadoDAO();
                }
            }
        }
        return instance;
    }

    // Método privado para obtener una conexión a la base de datos
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // CREAR: Inserta una nueva Empleado en la base de datos.
    // Este método propaga SQLException al llamador.
    public boolean createEmpleado(Empleado Empleado) throws SQLException {
        String sql = "INSERT INTO Empleado (dni, nombre, apellido, edad, departamento) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, Empleado.getDni());
            ps.setString(2, Empleado.getNombre());
            ps.setString(3, Empleado.getApellido());
            ps.setInt(4, Empleado.getEdad());
            if (Empleado.getDepartamento() != null) {
                ps.setInt(5, Empleado.getDepartamento().getCodigo());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Empleado.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    // LEER: Recupera una Empleado por su ID.
    public Empleado getEmpleado(int id) throws SQLException {
        String sql = "SELECT id, dni, nombre, apellido, edad, departamento FROM Empleado WHERE id = ?";
        Empleado Empleado = null;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int identificador = rs.getInt("id");
                    String dni = rs.getString("dni");
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    int edad = rs.getInt("edad");
                    int codigoDep = rs.getInt("departamento");
                    Departamento dep = null;
                    if (!rs.wasNull()) {
                        dep = new Departamento(codigoDep, "", null);
                    }
                    Empleado = new Empleado(identificador, dni, nombre, apellido, edad, dep);
                }
            }
        }
        return Empleado;
    }

    // LEER: Recupera todas las Empleados almacenadas
    public List<Empleado> listAllEmpleados() throws SQLException {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT id, dni, nombre, apellido, edad, departamento FROM Empleado";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String dni = rs.getString("dni");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                int edad = rs.getInt("edad");
                int codigoDep = rs.getInt("departamento");
                Departamento dep = null;
                if (!rs.wasNull()) {
                    dep = new Departamento(codigoDep, "", null);
                }
                Empleado Empleado = new Empleado(id, dni, nombre, apellido, edad, dep);
                lista.add(Empleado);
            }
        }
        return lista;
    }

    // ACTUALIZAR: Actualiza los datos de una Empleado existente
    public boolean updateEmpleado(Empleado Empleado) throws SQLException {
        String sql = "UPDATE Empleado SET dni = ?, nombre = ?, apellido = ?, edad = ?, departamento = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, Empleado.getDni());
            ps.setString(2, Empleado.getNombre());
            ps.setString(3, Empleado.getApellido());
            ps.setInt(4, Empleado.getEdad());
            if (Empleado.getDepartamento() != null) {
                ps.setInt(5, Empleado.getDepartamento().getCodigo());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setInt(6, Empleado.getId());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    // ELIMINAR: Elimina una Empleado por su ID
    public boolean deleteEmpleado(int id) throws SQLException {
        String sql = "DELETE FROM Empleado WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }
}

