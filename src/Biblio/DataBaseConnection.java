package Biblio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    // Informations de connexion à PostgreSQL:
	// Remplacez par le nom de votre base
    private static final String URL = "jdbc:postgresql://localhost:5432/Bibliotheque"; 
    private static final String USER = "postgres";  // Remplacez par votre utilisateur PostgreSQL
    private static final String PASSWORD = "udev2";  // Remplacez par votre mot de passe PostgreSQL

    // Méthode pour obtenir la connexion
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver"); // Charger le driver PostgreSQL
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);  // Retourne la connexion
    }
}

