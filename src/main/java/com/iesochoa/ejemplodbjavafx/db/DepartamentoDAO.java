package com.iesochoa.ejemplodbjavafx.db;

import com.iesochoa.ejemplodbjavafx.model.Departamento;
import com.iesochoa.ejemplodbjavafx.model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoDAO {
    // Consultas SQL predefinidas para operaciones CRUD
    private static final String INSERT_QUERY = "INSERT INTO Departamento (nombre, jefe) VALUES (?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM Departamento";
    private static final String SELECT_BY_DNI_QUERY = "SELECT * FROM Persona WHERE dni = ?";
    private static final String UPDATE_QUERY = "UPDATE Persona SET nombre = ?, apellido = ?, edad = ? WHERE dni = ?";
    private static final String DELETE_QUERY = "DELETE FROM Persona WHERE dni = ?";
    private static final String TOTAL_PERSONAS_QUERY = "SELECT COUNT(*) FROM Persona";

    // Instancia única de la clase (Singleton)
    private static volatile DepartamentoDAO instance;
    private Connection connection;

    // Constructor privado para evitar instanciación externa
    private DepartamentoDAO() {
        // Opcionalmente, se pueden inicializar recursos aquí
        this.connection = DBConnection.getConnection();
    }

    // Método para obtener la instancia única de DepartamentoDAO (Singleton)
    public static DepartamentoDAO getInstance() {
        if (instance == null) {
            synchronized (DepartamentoDAO.class) {
                if (instance == null) {
                    instance = new DepartamentoDAO();
                }
            }
        }
        return instance;
    }



    // CREAR: Inserta un nuevo Departamento en la base de datos
    public boolean createDepartamento(Departamento dept) throws SQLException{
        //String sql = "INSERT INTO Departamento (nombre, jefe) VALUES (?, ?)";

            PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, dept.getNombre());
            if (dept.getJefe() != null) {
                ps.setInt(2, dept.getJefe().getId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        dept.setCodigo(generatedKeys.getInt(1));
                    }
                }
                return true;
            }

        return false;
    }

    // LEER: Recupera un Departamento por su código (clave primaria)
    public Departamento getDepartamento(int codigo) throws SQLException{
        //String sql = "SELECT * FROM Departamento WHERE codigo = ?";
        Departamento dept = null;
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL_QUERY)) {

            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int cod = rs.getInt("codigo");
                    String nombre = rs.getString("nombre");
                    int jefeId = rs.getInt("jefe");
                    Empleado jefe = null;
                    // En este ejemplo, sólo se recupera el id del jefe. Podrías obtener más datos en una consulta adicional.\n
                    if (!rs.wasNull()) {
                        jefe = new Empleado(jefeId, "","","",0,null); // nombre vacío como placeholder\n
                    }
                    dept = new Departamento(cod, nombre, jefe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dept;
    }

    // LEER: Devuelve una lista con todos los Departamentos registrados
    public List<Departamento> listAllDepartamentos() {
        List<Departamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM Departamento";
        try (
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int cod = rs.getInt("codigo");
                String nombre = rs.getString("nombre");
                int jefeId = rs.getInt("jefe");
                Empleado jefe = null;
                if (!rs.wasNull()) {
                    jefe = new Empleado(jefeId, "","","",0,null); // nombre vacío como placeholder\n
                }
                Departamento dept = new Departamento(cod, nombre, jefe);
                lista.add(dept);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ACTUALIZAR: Actualiza un Departamento existente
    public boolean updateDepartamento(Departamento dept) {
        String sql = "UPDATE Departamento SET nombre = ?, jefe = ? WHERE codigo = ?";
        try (
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, dept.getNombre());
            if (dept.getJefe() != null) {
                ps.setInt(2, dept.getJefe().getId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setInt(3, dept.getCodigo());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ELIMINAR: Elimina un Departamento por su código
    public boolean deleteDepartamento(int codigo) {
        String sql = "DELETE FROM Departamento WHERE codigo = ?";
        try (
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, codigo);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
