package com.iesochoa.ejemplodbjavafx.db;

import com.iesochoa.ejemplodbjavafx.model.Departamento;
import com.iesochoa.ejemplodbjavafx.model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase de acceso a datos (DAO) para la entidad Departamento.
 * Proporciona métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre la tabla Departamento en la base de datos.
 * Utiliza el patrón Singleton para asegurar una única instancia de la clase.
 */
public class DepartamentoDAO {
    /**
     * Sentencia SQL predefinida para insertar un nuevo departamento en la tabla Departamento.
     * Los valores para los parámetros (nombre, jefe) se proporcionarán al ejecutar la sentencia preparada.
     */
    private static final String INSERT_QUERY = "INSERT INTO Departamento (nombre, jefe) VALUES (?, ?)";
    /**
     * Sentencia SQL predefinida para seleccionar todos los departamentos de la tabla Departamento.
     */
    private static final String SELECT_ALL_QUERY = "SELECT * FROM Departamento";
    /**
     * Sentencia SQL predefinida (sin usar en el código proporcionado) para seleccionar una persona por su DNI.
     */
    private static final String SELECT_BY_DNI_QUERY = "SELECT * FROM Persona WHERE dni = ?";
    /**
     * Sentencia SQL predefinida (sin usar en el código proporcionado) para actualizar los datos de una persona.
     */
    private static final String UPDATE_QUERY = "UPDATE Persona SET nombre = ?, apellido = ?, edad = ? WHERE dni = ?";
    /**
     * Sentencia SQL predefinida (sin usar en el código proporcionado) para eliminar una persona por su DNI.
     */
    private static final String DELETE_QUERY = "DELETE FROM Persona WHERE dni = ?";
    /**
     * Sentencia SQL predefinida (sin usar en el código proporcionado) para contar el total de personas.
     */
    private static final String TOTAL_PERSONAS_QUERY = "SELECT COUNT(*) FROM Persona";
    /**
     * Sentencia SQL para seleccionar todos los departamentos de la tabla Departamento.
     */
    public static final String SELECT_ALL_DEPARTAMENTOS = """
            SELECT
                *
            FROM
                Departamento
    """;
    /**
     * Sentencia SQL (más antigua) para seleccionar todos los departamentos, incluyendo el nombre y apellido del jefe (empleado).
     * Utiliza un LEFT JOIN para obtener la información del jefe si existe.
     */
    public static final String SELECT_ALL_DEPARTAMENTOS_OLD = """
            SELECT
                Departamento.codigo,
                Departamento.nombre,
                Departamento.jefe,
                Empleado.nombre AS nombre_jefe,
                Empleado.apellido AS apellido_jefe

            FROM
                Departamento
            LEFT JOIN
                Empleado ON Departamento.jefe = Empleado.id
    """;
    /**
     * Instancia única de la clase DepartamentoDAO (implementación del patrón Singleton).
     * El modificador {@code volatile} asegura que la instancia se lea correctamente
     * en entornos multihilo.
     */
    private static volatile DepartamentoDAO instance;
    /**
     * Objeto {@link Connection} para interactuar con la base de datos.
     * Se obtiene a través de la clase {@link DBConnection}.
     */
    private Connection connection;

    /**
     * Constructor privado para evitar la instanciación directa desde fuera de la clase.
     * Inicializa la conexión a la base de datos al crear la instancia.
     */
    private DepartamentoDAO() {
        // Opcionalmente, se pueden inicializar recursos aquí
        this.connection = DBConnection.getConnection();
    }

    /**
     * Método estático para obtener la única instancia de DepartamentoDAO (Singleton).
     * Si la instancia no existe, se crea de forma segura en un entorno multihilo.
     *
     * @return La instancia única de DepartamentoDAO.
     */
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

    /**
     * CREAR: Inserta un nuevo Departamento en la base de datos.
     *
     * @param dept El objeto {@link Departamento} que se va a insertar.
     * @return {@code true} si la inserción fue exitosa, {@code false} en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public boolean createDepartamento(Departamento dept) throws SQLException {
        //String sql = "INSERT INTO Departamento (nombre, jefe) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dept.getNombre());
            if (dept.getJefe() != null) {
                ps.setInt(2, dept.getJefe().getId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        dept.setCodigo(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * LEER: Recupera un Departamento por su código (clave primaria).
     *
     * @param codigo El código del departamento a buscar.
     * @return Un objeto {@link Departamento} si se encuentra, {@code null} en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public Departamento getDepartamento(int codigo) throws SQLException {
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
                    // En este ejemplo, sólo se recupera el id del jefe. Podrías obtener más datos en una consulta adicional.
                    if (!rs.wasNull()) {
                        jefe = new Empleado(jefeId, "", "", "", 0, null); // nombre vacío como placeholder
                    }
                    dept = new Departamento(cod, nombre, jefe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dept;
    }

    /**
     * LEER: Devuelve una lista con todos los Departamentos registrados.
     *
     * @return Una lista de objetos {@link Departamento}. Si no hay departamentos, la lista estará vacía.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public ArrayList<Departamento> listAllDepartamentos() throws SQLException {
        ArrayList<Departamento> lista = new ArrayList<>();
        try (
                PreparedStatement ps = connection.prepareStatement(SELECT_ALL_DEPARTAMENTOS);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int cod = rs.getInt("codigo");
                String nombre = rs.getString("nombre");
                // Nos permite saber si el valor de la columna 'jefe' es null.
                Integer jefeId = (Integer) rs.getObject("jefe");
                Empleado jefe = null;
                if (jefeId != null) {
                    // Buscamos el empleado que es jefe del departamento.
                    jefe = EmpleadoDAO.getInstance().selectEmpleadoPorId(jefeId);
                }
                Departamento dept = new Departamento(cod, nombre, jefe);
                lista.add(dept);
            }
        }
        return lista;
    }

    /**
     * ACTUALIZAR: Actualiza un Departamento existente en la base de datos.
     *
     * @param dept El objeto {@link Departamento} con los datos actualizados. El código del departamento
     * debe estar establecido para identificar el registro a actualizar.
     * @return {@code true} si la actualización fue exitosa, {@code false} en caso contrario.
     */
    public boolean updateDepartamento(Departamento dept) {
        String sql = "UPDATE Departamento SET nombre = ?, jefe = ? WHERE codigo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
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

    /**
     * ELIMINAR: Elimina un Departamento de la base de datos por su código.
     *
     * @param codigo El código del departamento a eliminar.
     * @return {@code true} si la eliminación fue exitosa, {@code false} en caso contrario.
     */
    public boolean deleteDepartamento(int codigo) {
        String sql = "DELETE FROM Departamento WHERE codigo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}