package org.example.ConexionMongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionMongoDB {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static MongoDatabase conexionMongoDB() {
        try {
            if (mongoClient == null) { // Solo crear la conexión si no está inicializada
                Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
                mongoClient = MongoClients.create("mongodb://localhost:27017");
                database = mongoClient.getDatabase("comercio");
                System.out.println("Conexión exitosa a la base de datos 'comercio'");
            }
            return database;
        } catch (Exception e) {
            System.out.println("Error de conexión con el servidor MongoDB");
            e.printStackTrace();
        }
        return null;
    }

    public static void cerrarConexion() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null; // Resetear la variable para futuras conexiones
            database = null;
            System.out.println("Conexión MongoDB cerrada correctamente.");
        }
    }
}
