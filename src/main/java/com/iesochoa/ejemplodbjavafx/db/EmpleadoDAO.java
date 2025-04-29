package com.iesochoa.ejemplodbjavafx.db;

import com.iesochoa.ejemplodbjavafx.model.Departamento;
import com.iesochoa.ejemplodbjavafx.model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;
import java.util.ArrayList;

/**
 * Clase de acceso a datos (DAO) para la entidad Empleado.
 * Proporciona métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre la tabla Empleado en la base de datos.
 * Utiliza el patrón Singleton para asegurar una única instancia de la clase.
 */
public class EmpleadoDAO {

    /**
     * Sentencia SQL para insertar un nuevo empleado en la tabla Empleado.
     * Los valores para los parámetros (dni, nombre, apellido, edad, departamento)
     * se proporcionarán al ejecutar la sentencia preparada.
     */
    public static final String INSERT_EMPLEADO = "INSERT INTO Empleado (dni, nombre, apellido, edad, departamento) VALUES (?, ?, ?, ?, ?)";

    /**
     * Sentencia SQL para seleccionar un empleado por su ID, incluyendo el nombre del departamento.
     * Utiliza un LEFT JOIN para obtener información del departamento si existe.
     */
    public static final String SELECT_EMPLEADO_POR_COD = """
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
                Departamento ON Empleado.departamento = Departamento.codigo
             WHERE id = ?;
    """;

    /**
     * Sentencia SQL para seleccionar todos los empleados, incluyendo el nombre del departamento.
     * Utiliza un LEFT JOIN para obtener información del departamento si existe.
     */
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

    /**
     * Sentencia SQL para actualizar los datos de un empleado existente en la tabla Empleado.
     * La condición WHERE asegura que solo se actualice el empleado con el ID especificado.
     */
    public static final String UPDATE_EMPLEADO = "UPDATE Empleado SET dni = ?, nombre = ?, apellido = ?, edad = ?, departamento = ? WHERE id = ?";

    /**
     * Sentencia SQL para eliminar un empleado de la tabla Empleado por su ID.
     */
    public static final String DELETE_EMPLEADO = "DELETE FROM Empleado WHERE id = ?";

    /**
     * Sentencia SQL para seleccionar todos los empleados de un departamento específico,
     * incluyendo el nombre del departamento.
     */
    public static final String SELECT_EMPLEADOS_DEPARTAMENTO = """
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
                Departamento ON Empleado.departamento = Departamento.codigo
            WHERE
                Empleado.departamento = ?;
""";

    /**
     * Sentencia SQL para seleccionar empleados cuyo nombre o apellido coincida parcialmente
     * con el valor proporcionado. Utiliza LIKE para la búsqueda con comodines.
     */
    public static final String SELECT_EMPLEADOS_POR_NOMBRE = """
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
                Departamento ON Empleado.departamento = Departamento.codigo
            WHERE
                Empleado.nombre LIKE ? OR Empleado.apellido LIKE ?;
""";

    /**
     * Sentencia SQL para seleccionar empleados cuyo DNI coincida parcialmente
     * con el valor proporcionado. Utiliza LIKE para la búsqueda con comodines.
     */
    public static final String SELECT_EMPLEADOS_POR_DNI = """
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
                Departamento ON Empleado.departamento = Departamento.codigo
            WHERE
                Empleado.dni LIKE ?;
""";

    /**
     * Sentencia SQL para seleccionar un empleado por su DNI exacto, incluyendo el nombre del departamento.
     */
    public static final String SELECT_EMPLEADO_POR_DNI = """
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
                Departamento ON Empleado.departamento = Departamento.codigo
            WHERE
                Empleado.dni = ?;
""";

    /**
     * Instancia única de la clase EmpleadoDAO (implementación del patrón Singleton).
     * El modificador {@code volatile} asegura que la instancia se lea correctamente
     * en entornos multihilo.
     */
    private static volatile EmpleadoDAO instance;

    /**
     * Objeto {@link Connection} para interactuar con la base de datos.
     * Se obtiene a través de la clase {@link DBConnection}.
     */
    private Connection connection;

    /**
     * Constructor privado para evitar la instanciación directa desde fuera de la clase.
     * Inicializa la conexión a la base de datos al crear la instancia.
     */
    private EmpleadoDAO() {
        this.connection = DBConnection.getConnection();
    }

    /**
     * Método estático para obtener la única instancia de EmpleadoDAO (Singleton).
     * Si la instancia no existe, se crea de forma segura en un entorno multihilo.
     *
     * @return La instancia única de EmpleadoDAO.
     */
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

    /**
     * Inserta un nuevo empleado en la base de datos.
     *
     * @param empleado El objeto {@link Empleado} que se va a insertar.
     * @return {@code true} si la inserción fue exitosa, {@code false} en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public boolean createEmpleado(Empleado empleado) throws SQLException {
        String sql = INSERT_EMPLEADO;
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, empleado.getDni());
            ps.setString(2, empleado.getNombre());
            ps.setString(3, empleado.getApellidos());
            ps.setInt(4, empleado.getEdad());
            if (empleado.getDepartamento() != null) {
                ps.setInt(5, empleado.getDepartamento().getCodigo());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        empleado.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    /**
     * Recupera un empleado de la base de datos por su ID.
     *
     * @param id El ID del empleado que se va a buscar.
     * @return Un objeto {@link Empleado} si se encuentra, {@code null} en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public Empleado selectEmpleadoPorId(int id) throws SQLException {
        Empleado empleado = null;
        try (PreparedStatement ps = connection.prepareStatement(SELECT_EMPLEADO_POR_COD)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    empleado = resultSetToEmpleado(rs);
                }
            }
        }
        return empleado;
    }

    /**
     * Recupera todos los empleados almacenados en la base de datos.
     *
     * @return Una lista de objetos {@link Empleado}. Si no hay empleados, la lista estará vacía.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public ArrayList<Empleado> listAllEmpleados() throws SQLException {
        ArrayList<Empleado> lista = new ArrayList<>();
        try (
                PreparedStatement ps = connection.prepareStatement(SELECT_ALL_EMPLEADOS);
                ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                Empleado empleado = resultSetToEmpleado(rs);
                lista.add(empleado);
            }
        }
        return lista;
    }

    /**
     * Convierte una fila de un {@link ResultSet} a un objeto {@link Empleado}.
     *
     * @param rs El {@link ResultSet} que contiene los datos del empleado y su departamento.
     * @return Un nuevo objeto {@link Empleado} con los datos extraídos del {@link ResultSet}.
     * @throws SQLException Si ocurre un error al acceder a los datos del {@link ResultSet}.
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
        } else {
            dep = new Departamento(0, "Sin Departamento", null);
        }
        return new Empleado(id, dni, nombre, apellido, edad, dep);
    }

    /**
     * Actualiza los datos de un empleado existente en la base de datos.
     *
     * @param empleado El objeto {@link Empleado} con los datos actualizados. El ID del empleado
     * debe estar establecido para identificar el registro a actualizar.
     * @return {@code true} si la actualización fue exitosa, {@code false} en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public boolean updateEmpleado(Empleado empleado) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_EMPLEADO)) {
            ps.setString(1, empleado.getDni());
            ps.setString(2, empleado.getNombre());
            ps.setString(3, empleado.getApellidos());
            ps.setInt(4, empleado.getEdad());
            if (empleado.getDepartamento() != null) {
                ps.setInt(5, empleado.getDepartamento().getCodigo());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setInt(6, empleado.getId());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    /**
     * Elimina un empleado de la base de datos por su ID.
     *
     * @param id El ID del empleado que se va a eliminar.
     * @return {@code true} si la eliminación fue exitosa, {@code false} en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public boolean deleteEmpleado(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_EMPLEADO)) {
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    /**
     * Recupera todos los empleados de un departamento específico.
     *
     * @param codigoDepartamento El código del departamento para filtrar los empleados.
     * @return Una lista de objetos {@link Empleado} que pertenecen al departamento especificado.
     * Si no hay empleados en el departamento, la lista estará vacía.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public ArrayList<Empleado> empleadosDepartamento(int codigoDepartamento) throws SQLException {
        ArrayList<Empleado> empleados = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_EMPLEADOS_DEPARTAMENTO)) {
            ps.setInt(1, codigoDepartamento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Empleado empleado = resultSetToEmpleado(rs);
                    empleados.add(empleado);
                }
            }
        }
        return empleados;
    }

    /**
     * Recupera todos los empleados cuyo nombre o apellido coincida parcialmente
     * con la cadena de búsqueda proporcionada.
     *
     * @param nombre La cadena de búsqueda para el nombre o apellido del empleado.
     * @return Una lista de objetos {@link Empleado} que coinciden con la búsqueda.
     * Si no hay coincidencias, la lista estará vacía.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public ArrayList<Empleado> selectEmpleadosPorNombre(String nombre) throws SQLException {
        ArrayList<Empleado> lista = new ArrayList<>();
        String nombreLike = "%" + nombre + "%";
        String apellidoLike = "%" + nombre + "%";
        try (PreparedStatement ps = connection.prepareStatement(SELECT_EMPLEADOS_POR_NOMBRE)) {
            ps.setString(1, nombreLike);
            ps.setString(2, apellidoLike);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Empleado empleado = resultSetToEmpleado(rs);
                    lista.add(empleado);
                }
            }
        }
        return lista;
    }

    /**
     * Recupera todos los empleados cuyo DNI coincida parcialmente con la cadena
     * de búsqueda proporcionada.
     *
     * @param dni La cadena de búsqueda para el DNI del empleado.
     * @return Una lista de objetos {@link Empleado} que coinciden con la búsqueda.
     * Si no hay coincidencias, la lista estará vacía.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public ArrayList<Empleado> selectEmpleadosPorDNI(String dni) throws SQLException {
        ArrayList<Empleado> lista = new ArrayList<>();
        String dniLike = "%" + dni + "%";
        try (PreparedStatement ps = connection.prepareStatement(SELECT_EMPLEADOS_POR_DNI)) {
            ps.setString(1, dniLike);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Empleado empleado = resultSetToEmpleado(rs);
                    lista.add(empleado);
                }
            }
        }
        return lista;
    }

    /**
     * Recupera un empleado por su DNI exacto.
     *
     * @param dni El DNI del empleado que se va a buscar.
     * @return Un objeto {@link Empleado} si se encuentra, {@code null} en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public Empleado selectEmpleadoPorDNI(String dni) throws SQLException {
        Empleado empleado = null;
        try (PreparedStatement ps = connection.prepareStatement(SELECT_EMPLEADO_POR_DNI)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    empleado = resultSetToEmpleado(rs);
                }
            }
        }
        return empleado;
    }
}