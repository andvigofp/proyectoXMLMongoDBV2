package org.example.Metodos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import org.basex.examples.api.BaseXClient;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.ConexionBaseX.ConexionBaseX;
import org.example.ConexionMongoDB.ConexionMongoDB;
import org.example.Modelo.Videojuego;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MetodosMongoDB {
    private static ConexionBaseX conexionBasex = new ConexionBaseX();
    private static ConexionMongoDB conexionMongoDB = new ConexionMongoDB();
    private static MongoDatabase database = conexionMongoDB.conexionMongoDB();
    private static ObjectId usuarioIdActual = null;
    private static Integer usuarioIdIntegerActual = null;
    private static String usuarioEmailActual = null;


    //Método para crear el usuario
    public void crearUsuario(Scanner teclado) {
        MongoCollection<Document> usuariosCollection = database.getCollection("Usuarios");

        try {
            String nombre = introducirString(teclado, "Introduce el nombre del usuario:");
            String correo = introducirString(teclado,"Introduce el correo del usuario:");

            // Comprobar si el correo ya existe
            if (usuariosCollection.find(Filters.eq("correo", correo)).first() != null) {
                System.out.println("Error: Ya existe un usuario con ese correo.");
                return;
            }

            System.out.print("Introduce la contraseña del usuario: ");
            String contrasena = teclado.nextLine();
            System.out.print("Introduce la dirección del usuario: ");
            String direccion = teclado.nextLine();
            System.out.print("Introduce el teléfono del usuario: ");
            String telefono = teclado.nextLine();
            System.out.print("Introduce la edad del usuario: ");
            int edad = teclado.nextInt();

            // Encontrar el último usuario_id y asignar el siguiente usuario_id
            Document ultimoUsuario = usuariosCollection.find().sort(Sorts.descending("usuario_id")).first();
            int nuevoUsuarioId = (ultimoUsuario != null) ? Integer.parseInt(ultimoUsuario.get("usuario_id").toString()) + 1 : 1;

            Document nuevoUsuario = new Document("usuario_id", nuevoUsuarioId)
                    .append("nombre", nombre)
                    .append("correo", correo)
                    .append("contrasena", contrasena)
                    .append("direccion", direccion)
                    .append("telefono", telefono)
                    .append("edad", edad);


            usuariosCollection.insertOne(nuevoUsuario);
            System.out.println("Usuario creado exitosamente con usuario_id: " + nuevoUsuarioId);

            // Mostrar la información del usuario recién creado
            Document usuarioCreado = usuariosCollection.find(Filters.eq("usuario_id", nuevoUsuarioId)).first();
            if (usuarioCreado != null) {
                System.out.println("Información del usuario creado:");
                System.out.println("ID: " + usuarioCreado.getInteger("usuario_id"));
                System.out.println("Nombre: " + usuarioCreado.getString("nombre"));
                System.out.println("Correo: " + usuarioCreado.getString("correo"));
                System.out.println("Dirección: " + usuarioCreado.getString("direccion"));
                System.out.println("Teléfono: " + usuarioCreado.getString("telefono"));
                System.out.println("Edad: " + usuarioCreado.getInteger("edad"));
            }
        } catch (Exception e) {
            System.out.println("Error al crear el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Método para identificarUsarioPorEmail
    public void identificarUsuarioPorEmail(Scanner teclado) {
        MongoCollection<Document> usuariosCollection = database.getCollection("Usuarios");
        System.out.print("Introduce el correo del usuario: ");
        String correo = teclado.nextLine();

        try {
            Document usuario = usuariosCollection.find(Filters.eq("correo", correo)).first();

            if (usuario != null) {
                usuarioEmailActual = correo;
                usuarioIdActual = usuario.getObjectId("_id");
                usuarioIdIntegerActual = usuario.getInteger("usuario_id");
                System.out.println("Usuario identificado exitosamente. ID del usuario: " + usuarioIdIntegerActual);
            } else {
                System.out.println("Error: No se encontró un usuario con ese correo.");
            }
        } catch (Exception e) {
            System.out.println("Error al identificar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Método para borrar el usuario de la base de datos
    public void borrarUsuario(Scanner teclado) {
        // Identificar el usuario primero
        identificarUsuarioPorEmail(teclado);

        // Si no se identificó al usuario, finalizar el método
        if (usuarioIdIntegerActual == null) {
            System.out.println("Error: No se ha identificado ningún usuario.");
            return;
        }

        MongoCollection<Document> usuariosCollection = database.getCollection("Usuarios");
        MongoCollection<Document> carritoCollection = database.getCollection("Carrito");
        MongoCollection<Document> comprasCollection = database.getCollection("Compras");

        try {
            // Buscar el usuario en la colección Usuarios
            Document usuario = usuariosCollection.find(Filters.eq("usuario_id", usuarioIdIntegerActual)).first();
            if (usuario != null) {
                System.out.println("Usuario encontrado: " + usuario.toJson());

                // Confirmación antes de borrar
                System.out.print("¿Estás seguro de que deseas borrar este usuario y sus datos asociados? (s/n): ");
                String respuesta = teclado.nextLine();
                if (!respuesta.equalsIgnoreCase("s")) {
                    System.out.println("Operación cancelada.");
                    return;
                }

                // Borrar el usuario de la colección Usuarios
                usuariosCollection.deleteOne(Filters.eq("usuario_id", usuarioIdIntegerActual));
                System.out.println("Usuario borrado de la colección Usuarios");

                // Borrar el carrito asociado en la colección Carrito
                carritoCollection.deleteMany(Filters.eq("usuario_id", usuarioIdIntegerActual));
                System.out.println("Carritos asociados borrados de la colección Carrito");

                // Borrar las compras asociadas en la colección Compras
                comprasCollection.deleteMany(Filters.eq("usuario_id", usuarioIdIntegerActual));
                System.out.println("Compras asociadas borradas de la colección Compras");

                System.out.println("Usuario y datos asociados borrados exitosamente.");

                // Restablecer la identificación del usuario si coincide
                if (usuarioIdActual != null && usuarioIdActual.equals(usuario.getObjectId("_id"))) {
                    usuarioEmailActual = null;
                    usuarioIdActual = null;
                    usuarioIdIntegerActual = null;
                }
            } else {
                System.out.println("Error: No se encontró un usuario con ese ID.");
            }
        } catch (Exception e) {
            System.out.println("Error al borrar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Método para modificar el campo del usuario
    public void modificarCampoUsuario(Scanner teclado) {
        // Identificar al usuario primero
        identificarUsuarioPorEmail(teclado);

        // Si no se identificó al usuario, finalizar el método
        if (usuarioIdIntegerActual == null) {
            System.out.println("Error: No se ha identificado ningún usuario. No se puede proceder.");
            return;
        }

        MongoCollection<Document> usuariosCollection = database.getCollection("Usuarios");

        // Pedir el campo a modificar y el nuevo valor
        String campo = introducirString(teclado, "Introduce el campo a modificar (nombre, contrasena, direccion, telefono, edad): ");
        String nuevoValor = introducirString(teclado, "Introduce el nuevo valor para " + campo + ": ");

        try {
            // Validar que el campo y el valor no estén vacíos
            if (campo.isEmpty() || nuevoValor.isEmpty()) {
                throw new IllegalArgumentException("El campo y el valor no pueden estar vacíos.");
            }

            // Preparar el documento de actualización
            Document actualizacion;

            // Si el campo a modificar es "edad", validar y convertir el valor a Integer
            if (campo.equalsIgnoreCase("edad")) {
                try {
                    int nuevoValorEntero = Integer.parseInt(nuevoValor); // Convertir a Integer
                    actualizacion = new Document("$set", new Document(campo, nuevoValorEntero));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("El valor para el campo 'edad' debe ser un número entero válido.");
                }
            } else {
                // Para otros campos, usar el valor tal cual
                actualizacion = new Document("$set", new Document(campo, nuevoValor));
            }

            // Actualizar el campo en la colección Usuarios
            usuariosCollection.updateOne(
                    Filters.eq("usuario_id", usuarioIdIntegerActual),
                    actualizacion
            );
            System.out.println("Campo modificado exitosamente.");

            // Mostrar la información del usuario después de la modificación
            Document usuarioModificado = usuariosCollection.find(Filters.eq("usuario_id", usuarioIdIntegerActual)).first();
            if (usuarioModificado != null) {
                System.out.println("Información del usuario modificado:");
                System.out.println("ID: " + usuarioModificado.getInteger("usuario_id"));
                System.out.println("Nombre: " + usuarioModificado.getString("nombre"));
                System.out.println("Correo: " + usuarioModificado.getString("correo"));
                System.out.println("Dirección: " + usuarioModificado.getString("direccion"));
                System.out.println("Teléfono: " + usuarioModificado.getString("telefono"));
                System.out.println("Edad: " + usuarioModificado.getInteger("edad"));
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al modificar el campo del usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private static int obtenerUltimoUsuarioId() {
        MongoCollection<Document> usuariosCollection = database.getCollection("Usuarios");
        Document ultimoUsuario = usuariosCollection.find().sort(Sorts.descending("usuario_id")).first();
        return (ultimoUsuario != null) ? ultimoUsuario.getInteger("usuario_id") : 0;
    }


    private static int obtenerUltimoCarritoId() {
        MongoCollection<Document> carritoCollection = database.getCollection("Carrito");
        Document ultimoCarrito = carritoCollection.find().sort(Sorts.descending("carrito_id")).first();
        return (ultimoCarrito != null) ? ultimoCarrito.getInteger("carrito_id") : 0;
    }

    public int obtenerEdadUsuario(ObjectId usuarioId) {
        MongoCollection<Document> usuariosCollection = database.getCollection("Usuarios");
        Document usuario = usuariosCollection.find(Filters.eq("_id", usuarioId)).first();

        if (usuario != null) {
            // Asumiendo que el documento del usuario tiene un campo "edad"
            return usuario.getInteger("edad_minima_recomendada");
        } else {
            throw new IllegalArgumentException("No se encontró un usuario con el ID proporcionado.");
        }
    }


    //Método para ñadir videojuegos al carrito
    public void anadirVideojuegoAlCarrito(Scanner teclado) {
        // Si el usuario no está identificado, intentar identificarlo
        if (usuarioIdIntegerActual == null) {
            identificarUsuarioPorEmail(teclado);
            if (usuarioIdIntegerActual == null) {
                System.out.println("Error: No se ha identificado ningún usuario. No se puede proceder.");
                return; // Si el usuario sigue sin identificarse, salir del método
            }
        }

        MongoCollection<Document> carritoCollection = database.getCollection("Carrito");

        while (true) {
            mostrarVideojuegosPorEdad(teclado);

            System.out.print("Introduce el ID del videojuego a añadir al carrito: ");
            int idVideojuego = teclado.nextInt();
            System.out.print("Introduce la cantidad a añadir al carrito: ");
            int cantidad = teclado.nextInt();
            teclado.nextLine(); // Limpiar el buffer

            try {
                Document nuevoItemCarrito = new Document("usuario_id", usuarioIdIntegerActual)
                        .append("videojuego_id", idVideojuego)
                        .append("cantidad", cantidad);

                carritoCollection.insertOne(nuevoItemCarrito);
                System.out.println("Videojuego añadido al carrito exitosamente.");

            } catch (Exception e) {
                System.out.println("Error al añadir el videojuego al carrito: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.print("¿Deseas seguir añadiendo videojuegos al carrito? (s/n): ");
            String respuesta = teclado.nextLine();
            if (!respuesta.equalsIgnoreCase("s")) {
                break;
            }
        }
    }




    public void mostrarTodosLosUsuarios() {
        MongoCollection<Document> usuariosCollection = database.getCollection("Usuarios");
        for (Document usuario : usuariosCollection.find()) {
            System.out.println(usuario.toJson());
        }
    }


    public void mostrarVideojuegosPorEdad(Scanner teclado) {
        if (usuarioIdIntegerActual == null) {
            System.out.println("Error: No se ha identificado ningún usuario.");
            identificarUsuarioPorEmail(teclado);
            if (usuarioIdIntegerActual == null) {
                return; // Si el usuario sigue sin identificarse, salir del método
            }
        }

        MongoCollection<Document> usuariosCollection = database.getCollection("Usuarios");
        Document usuario = usuariosCollection.find(Filters.eq("usuario_id", usuarioIdIntegerActual)).first();

        if (usuario == null) {
            System.out.println("Error: No se encontró un usuario con el ID proporcionado.");
            return;
        }

        Integer edadUsuario = usuario.getInteger("edad");

        if (edadUsuario == null || edadUsuario <= 0) {
            System.out.println("Error: La edad del usuario no se encontró o es inválida.");
            return;
        }

        BaseXClient session = conexionBasex.conexionBaseX();

        if (session != null) {
            try {
                String consulta = String.format("""
            for $v in //videojuego
            where number($v/edad_minima_recomendada) <= %d
            return <videojuego>
                       <id>{data($v/id)}</id>
                       <titulo>{data($v/titulo)}</titulo>
                       <descripcion>{data($v/descripcion)}</descripcion>
                       <precio>{data($v/precio)}</precio>
                       <disponibilidad>{data($v/disponibilidad)}</disponibilidad>
                       <genero>{data($v/genero)}</genero>
                       <desarrollador>{data($v/desarrollador)}</desarrollador>
                       <edad_minima_recomendada>{data($v/edad_minima_recomendada)}</edad_minima_recomendada>
                       <plataforma>{data($v/plataforma)}</plataforma>
                   </videojuego>
            """, edadUsuario);

                BaseXClient.Query query = session.query(consulta);
                List<Videojuego> videojuegosDisponibles = new ArrayList<>();

                while (query.more()) {
                    String resultado = query.next();
                    Videojuego videojuego = parsearVideojuego(resultado);
                    videojuegosDisponibles.add(videojuego);
                }
                query.close();

                if (!videojuegosDisponibles.isEmpty()) {
                    System.out.println("Videojuegos disponibles según la edad mínima recomendada:");
                    for (Videojuego videojuego : videojuegosDisponibles) {
                        System.out.printf("ID: %d | Título: %s | Edad Mínima Recomendada: %d%n", videojuego.getId(), videojuego.getTitulo(), videojuego.getEdadMinimaRecomendada());
                    }
                } else {
                    System.out.println("No se encontraron videojuegos.");
                }

            } catch (IOException e) {
                System.out.println("Error al listar los videojuegos por edad mínima recomendada.");
                e.printStackTrace();
            } finally {
                try {
                    session.close();
                } catch (IOException e) {
                    System.out.println("Error al cerrar la conexión.");
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Error: No se pudo establecer la conexión con la base de datos.");
        }
    }


    // Método para consultar videojuegos disponibles desde MongoDB
    private static List<Videojuego> consultarVideojuegosDesdeMongoDB() {
        List<Videojuego> videojuegosDisponibles = new ArrayList<>();
        MongoCollection<Document> carritoCollection = database.getCollection("Carrito");

        for (Document carrito : carritoCollection.find()) {
            List<Document> videojuegos = (List<Document>) carrito.get("videojuegos");

            for (Document videojuegoDoc : videojuegos) {
                int id = videojuegoDoc.getInteger("videojuego_id");
                String titulo = videojuegoDoc.getString("titulo");
                double precio = videojuegoDoc.getDouble("precio");
                int cantidad = videojuegoDoc.getInteger("cantidad");

                Videojuego videojuego = new Videojuego(id, titulo, "", precio, cantidad, "", "", 0, "");
                videojuegosDisponibles.add(videojuego);
            }
        }

        return videojuegosDisponibles;
    }


    // Método para parsear el resultado de la consulta y convertirlo en un objeto Videojuego
    private static Videojuego parsearVideojuego(String resultado) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(resultado));
            org.w3c.dom.Document doc = builder.parse(is);

            int id = Integer.parseInt(doc.getElementsByTagName("id").item(0).getTextContent());
            String titulo = doc.getElementsByTagName("titulo").item(0).getTextContent();
            String descripcion = doc.getElementsByTagName("descripcion").item(0).getTextContent();
            double precio = Double.parseDouble(doc.getElementsByTagName("precio").item(0).getTextContent());
            int disponibilidad = Integer.parseInt(doc.getElementsByTagName("disponibilidad").item(0).getTextContent());
            String genero = doc.getElementsByTagName("genero").item(0).getTextContent();
            String desarrollador = doc.getElementsByTagName("desarrollador").item(0).getTextContent();
            int edadMinimaRecomendada = Integer.parseInt(doc.getElementsByTagName("edad_minima_recomendada").item(0).getTextContent());
            String plataforma = doc.getElementsByTagName("plataforma").item(0).getTextContent();

            return new Videojuego(id, titulo, descripcion, precio, disponibilidad, genero, desarrollador, edadMinimaRecomendada, plataforma);
        } catch (Exception e) {
            System.out.println("Error al parsear el videojuego: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //Método para introducir por teclado tipo string
    private static String introducirString(Scanner teclado, String mensaje) {
        while (true) {
            try {
                System.out.println(mensaje + ": ");
                return teclado.nextLine();
            } catch (Exception e) {
                System.out.println("Error al introducir los datos. Vuelva a introducirlos");
            }
        }
    }

    //Método para introducir por teclado tipo double
    private static double pedirDouble(Scanner teclado,String mensaje) {
        System.out.println(mensaje);
        while (true) {
            try {
                return teclado.nextDouble();
            } catch (Exception ignored) {
            }
        }
    }

    //Método para introducir por teclado tipo int
    private static int introducirInt(Scanner teclado, String mensaje) {
        while (true) {
            try {
                System.out.println(mensaje + ": ");
                return teclado.nextInt();
            } catch (Exception e) {
                System.out.println("Error al introducir los datos. Vuelva a introducirlos");
                teclado.next(); // Limpiar la entrada no válida
            }
        }
    }
}

