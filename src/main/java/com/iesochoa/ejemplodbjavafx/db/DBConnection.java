package com.iesochoa.ejemplodbjavafx.db;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    // URL de conexión a la base de datos MySQL
    private static final String URL ="jdbc:mysql://database-1.c9knttsssyuj.us-east-1.rds.amazonaws.com:3306/empresa"; //"jdbc:mysql://localhost:3366/midb";
    private static final String USERNAME = "admin";//"root";
    private static final String PASSWORD = "root123456";
    private static final String CREATE_DB= """
              CREATE TABLE IF NOT EXISTS Departamento (
               codigo INT AUTO_INCREMENT PRIMARY KEY,
               nombre VARCHAR(100),
               jefe INT UNIQUE
           );

           -- Crear tabla Empleado si no existe (sin clave foránea)
           CREATE TABLE IF NOT EXISTS Empleado (
               id INT AUTO_INCREMENT PRIMARY KEY,
               dni VARCHAR(9) NOT NULL UNIQUE,
               nombre VARCHAR(50),
               apellido VARCHAR(50),
               edad INT,
               departamento INT
           );

           -- Añadir clave foránea a Empleado si no existe
           ALTER TABLE Empleado
           ADD CONSTRAINT  fk_empleado_departamento
           FOREIGN KEY (departamento) REFERENCES Departamento(codigo)
               ON DELETE SET NULL ON UPDATE CASCADE;
          

           -- Añadir clave foránea a Departamento si no existe
           ALTER TABLE Departamento
           ADD CONSTRAINT fk_departamento_empleado
           FOREIGN KEY (jefe) REFERENCES Empleado(id)
               ON DELETE SET NULL ON UPDATE CASCADE;                                                                                     ON DELETE SET NULL ON UPDATE CASCADE;
            """;
    private static Connection connection;

    // Constructor privado para evitar instancias directas
    private DBConnection() {}

    // Método estático para obtener la instancia única de la conexión
    public static Connection getConnection() {
        if (connection == null) {
            // Bloqueo sincronizado para evitar concurrencia
            synchronized (DBConnection.class) {
                if (connection == null) {
                    try {
                       // Establecer la conexión
                        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                       // crearTabla(connection);
                        //crearDatosEjemplo();
                    } catch ( SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return connection;
    }
    // Método para cerrar la conexión
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //crea la tabla si no existe
    private static void crearTabla(Connection conexion) throws SQLException {
        try (Statement statement = conexion.createStatement()) {
            /*statement.executeUpdate(
                    CREATE_DB
            );*/
            //System.out.println("Tabla Alumno creada correctamente.");
        }
    }
    //Crea unos datos de ejemplo
    public static void crearDatosEjemplo() throws SQLException{
        String INSERTS_EJEMPLO= """
                -- Insertar departamentos inicialmente sin jefe
                INSERT INTO Departamento (codigo, nombre, jefe) VALUES
                (1, 'Recursos Humanos', NULL),
                (2, 'Tecnología', NULL),
                (3, 'Marketing', NULL);
                
                -- Insertar Empleados (jefes y empleados)
                INSERT INTO Empleado (dni, nombre, apellido, edad, departamento) VALUES
                ('12345678A', 'Laura', 'Gómez', 40, 1),  -- jefe de RRHH
                ('23456789B', 'Carlos', 'López', 35, 2), -- jefe de Tecnología
                ('34567890C', 'Marta', 'Pérez', 42, 3),  -- jefe de Marketing
                ('45678901D', 'Juan', 'Sánchez', 38, 1), 
                ('56789012E', 'Ana', 'Martínez', 37, 2), 
                ('67890123F', 'Pedro', 'Ruiz', 28, 3),
                ('78901234G', 'Lucía', 'Fernández', 30, 2),
                ('89012345H', 'David', 'Torres', 32, 3),
                ('90123456I', 'Sofía', 'Ramírez', 26, 1),
                ('01234567J', 'Jorge', 'Hernández', 29, 2);
                 -- Asignar jefes a los departamentos
                 UPDATE Departamento SET jefe = 1 WHERE codigo = 1;
                 UPDATE Departamento SET jefe = 2 WHERE codigo = 2;
                 UPDATE Departamento SET jefe = 3 WHERE codigo = 3;             
                                
                """;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    INSERTS_EJEMPLO
            );
            //System.out.println("Tabla Alumno creada correctamente.");
        }
    }

}

