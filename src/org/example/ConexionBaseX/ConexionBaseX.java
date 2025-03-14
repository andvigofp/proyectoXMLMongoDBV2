package org.example.ConexionBaseX;

import org.basex.examples.api.BaseXClient;

import java.io.IOException;

public class ConexionBaseX {

    public static BaseXClient conexionBaseX() {
        try {
            BaseXClient sesion = new BaseXClient("localhost", 1984, "admin", "abc123");
            sesion.execute("open videojuegos");

            System.out.println("Conexión exitosa a la base de datos 'videojuegos'");
            return sesion;
        } catch (IOException e) {
            System.out.println("Error de conexión con el servidor BaseX");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error inesperado");
            e.printStackTrace();
        }
        return null;
    }
}
