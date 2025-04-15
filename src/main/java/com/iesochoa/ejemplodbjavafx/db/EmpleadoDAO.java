package com.iesochoa.ejemplodbjavafx.db;

import com.iesochoa.ejemplodbjavafx.model.Departamento;
import com.iesochoa.ejemplodbjavafx.model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    // Parámetros de conexión a la base de datos
    public static final String INSERT_EMPLEADO = "INSERT INTO Empleado (dni, nombre, apellido, edad, departamento) VALUES (?, ?, ?, ?, ?)";
    public static final String SELECT_EMPLEADO_POR_COD = "SELECT id, dni, nombre, apellido, edad, departamento FROM Empleado WHERE id = ?";
    public static final String SELECT_ALL_EMPLEADOS = """
            SELECT
                Empleado.id,
                Empleado.dni,
                Empleado.nombre,
                Empleado.apellido,
                Empleado.edad,
                Empleado.departamento AS codigo_departamento,
                Departamento.nombre AS nombre_departamento
            FROM
                Empleado
            LEFT JOIN
                Departamento ON Empleado.departamento = Departamento.codigo;
    """;
    public static final String UPDATE_EMPLEADO = "UPDATE Empleado SET dni = ?, nombre = ?, apellido = ?, edad = ?, departamento = ? WHERE id = ?";
    public static final String DELETE_EMPLEADO = "DELETE FROM Empleado WHERE id = ?";
    public static final String SELECT_EMPLEADOS_DEPARTAMENTO = "SELECT * FROM Empleado WHERE departamento = ?";

    // Instancia única de EmpleadoDAO (Singleton)
    private static volatile EmpleadoDAO instance;
    private Connection connection;

    // Constructor privado para evitar instanciación externa
    private EmpleadoDAO() {
        // Opcionalmente, se pueden inicializar recursos aquí
        this.connection = DBConnection.getConnection();
    }

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



    // CREAR: Inserta una nueva Empleado en la base de datos.
    // Este método propaga SQLException al llamador.
    public boolean createEmpleado(Empleado Empleado) throws SQLException {
        String sql = INSERT_EMPLEADO;
        try (
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, Empleado.getDni());
            ps.setString(2, Empleado.getNombre());
            ps.setString(3, Empleado.getApellidos());
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

    // LEER: Recupera un Empleado por su ID.
    public Empleado selectEmpleadoPorId(int id) throws SQLException {

        Empleado empleado = null;
        try (
             PreparedStatement ps = connection.prepareStatement(SELECT_EMPLEADO_POR_COD)
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    empleado = resultSetToEmpleado(rs);
                }
            }
        }
        return empleado;
    }
    
    // LEER: Recupera todos los Empleados almacenados
    public List<Empleado> listAllEmpleados() throws SQLException {
        List<Empleado> lista = new ArrayList<>();
       
        try (
                PreparedStatement ps = connection.prepareStatement(SELECT_ALL_EMPLEADOS);
                ResultSet rs = ps.executeQuery();
        )
        {
            while (rs.next()) {
                Empleado Empleado = resultSetToEmpleado(rs);
                lista.add(Empleado);
            }
        }
        return lista;
    }

    /**
     * Convierte un ResultSet en un objeto Empleado
     * @param rs:  ResultSet que contiene los datos de un Empleado
     * @return Empleado: Objeto Empleado con los datos del ResultSet
     * @throws SQLException
     */
    private static Empleado resultSetToEmpleado(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String dni = rs.getString("dni");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        int edad = rs.getInt("edad");
        int codigoDep = rs.getInt("codigo_departamento");
        Departamento dep = null;
        if (!rs.wasNull()) {
            dep = new Departamento(codigoDep, rs.getString("nombre_departamento"), null);
        }else {
            dep = new Departamento(0, "Sin Departamento", null);
        }
        Empleado Empleado = new Empleado(id, dni, nombre, apellido, edad, dep);
        return Empleado;
    }

    // ACTUALIZAR: Actualiza los datos de un Empleado existente
    public boolean updateEmpleado(Empleado Empleado) throws SQLException {

        try (
             PreparedStatement ps = connection.prepareStatement(UPDATE_EMPLEADO)
        ) {
            ps.setString(1, Empleado.getDni());
            ps.setString(2, Empleado.getNombre());
            ps.setString(3, Empleado.getApellidos());
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

    // ELIMINAR: Elimina un Empleado por su ID
    public boolean deleteEmpleado(int id) throws SQLException {

        try (
             PreparedStatement ps = connection.prepareStatement(DELETE_EMPLEADO)
        ) {

            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }
    public ArrayList<Empleado> empleadosDepartamento(int codigoDepartamento) throws SQLException {
        ArrayList<Empleado> empleados = new ArrayList<>();

          try(
                PreparedStatement ps = connection.prepareStatement(SELECT_EMPLEADOS_DEPARTAMENTO);

                )
          {// Crear la sentencia SQL para seleccionar empleados por departamento
              ps.setInt(1, codigoDepartamento);
              ResultSet rs = ps.executeQuery();

            // Procesar el resultado y crear objetos Empleado
            while (rs.next()) {
                Empleado empleado =resultSetToEmpleado(rs);
                empleados.add(empleado);
            }
        }
        return empleados;
    }
}

