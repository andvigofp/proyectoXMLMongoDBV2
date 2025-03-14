package org.example.ConexionMongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionMongoDB {
    public static MongoDatabase conexionMongoDB() {
        try {
            Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);

            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongoClient.getDatabase("comercio");
            System.out.println("Conexión exitosa a la base de datos 'comercio'");
            return database;
        }catch (Exception e) {
            System.out.println("Error de conexión con el servidor MongoDB");
            e.printStackTrace();
        }
        return null;
    }
}
