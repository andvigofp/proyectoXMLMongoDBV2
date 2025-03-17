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
import org.example.Modelo.CarritoCoste;
import org.example.Modelo.UsuarioGasto;
import org.example.Modelo.Videojuego;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

            String contrasena = introducirString(teclado,"Introduce la contraseña del usuario");
            String direccion = introducirString(teclado,"Introduce la dirección del usuario: ");
            String telefono = introducirString(teclado, "Introduce el teléfono del usuario: ");
            int edad = introducirInt(teclado,"Introduce la edad del usuario:");

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
        String correo = introducirString(teclado,"Introduce el correo del usuario:");

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
                String respuesta = introducirString(teclado, "¿Estás seguro de que deseas borrar este usuario y sus datos asociados? (s/n): ");
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

    //Método para obtener el ultimo id del usuario
    private static int obtenerUltimoUsuarioId() {
        MongoCollection<Document> usuariosCollection = database.getCollection("Usuarios");
        Document ultimoUsuario = usuariosCollection.find().sort(Sorts.descending("usuario_id")).first();
        return (ultimoUsuario != null) ? ultimoUsuario.getInteger("usuario_id") : 0;
    }

    //Método para obtener
    private static int obtenerUltimoCarritoId() {
        MongoCollection<Document> carritoCollection = database.getCollection("Carrito");
        Document ultimoCarrito = carritoCollection.find().sort(Sorts.descending("carrito_id")).first();
        return (ultimoCarrito != null) ? ultimoCarrito.getInteger("carrito_id") : 0;
    }

    private static int obtenerUltimoCompraId() {
        MongoCollection<Document> carritoCollection = database.getCollection("Compras");
        Document ultimoCarrito = carritoCollection.find().sort(Sorts.descending("compra_id")).first();
        return (ultimoCarrito != null) ? ultimoCarrito.getInteger("compra_id") : 0;
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
        if (usuarioIdIntegerActual == null) {
            identificarUsuarioPorEmail(teclado);
            if (usuarioIdIntegerActual == null) {
                System.out.println("Error: No se ha identificado ningún usuario. No se puede proceder.");
                return;
            }
        }

        MongoCollection<Document> carritoCollection = database.getCollection("Carrito");

        BaseXClient session = conexionBasex.conexionBaseX();
        if (session != null) {
            try {
                Document usuario = database.getCollection("Usuarios")
                        .find(Filters.eq("usuario_id", usuarioIdIntegerActual)).first();
                if (usuario == null) {
                    System.out.println("Error: Usuario no encontrado en MongoDB.");
                    return;
                }

                Integer edadUsuario = usuario.getInteger("edad");

                // Consulta en BaseX
                String consulta = String.format("""
                for $v in //videojuego
                where number($v/edad_minima_recomendada) <= %d
                return <videojuego>
                           <id>{data($v/id)}</id>
                           <titulo>{data($v/titulo)}</titulo>
                           <precio>{data($v/precio)}</precio>
                       </videojuego>
            """, edadUsuario);

                BaseXClient.Query query = session.query(consulta);
                List<Videojuego> videojuegosDisponibles = new ArrayList<>();

                System.out.println("Videojuegos disponibles para tu edad:");
                while (query.more()) {
                    String resultado = query.next();
                    Videojuego videojuego = parsearVideojuego(resultado);
                    if (videojuego != null) {
                        videojuegosDisponibles.add(videojuego);
                        System.out.printf("ID: %d | Título: %s | Precio: %.2f%n",
                                videojuego.getId(), videojuego.getTitulo(), videojuego.getPrecio());
                    }
                }
                query.close();

                if (videojuegosDisponibles.isEmpty()) {
                    System.out.println("No hay videojuegos disponibles para tu edad.");
                    return;
                }

                // Lista temporal para almacenar videojuegos seleccionados
                List<Document> videojuegosSeleccionados = new ArrayList<>();

                // Permitir al usuario seleccionar videojuegos
                while (true) {
                    System.out.print("Introduce el ID del videojuego que deseas añadir al carrito: ");
                    int idVideojuego = teclado.nextInt();
                    System.out.print("Introduce la cantidad que deseas añadir al carrito: ");
                    int cantidad = teclado.nextInt();
                    teclado.nextLine(); // Limpiar el buffer del escáner

                    // Validar el ID
                    Videojuego videojuegoSeleccionado = null;
                    for (Videojuego v : videojuegosDisponibles) {
                        if (v.getId() == idVideojuego) {
                            videojuegoSeleccionado = v;
                            break;
                        }
                    }

                    if (videojuegoSeleccionado == null) {
                        System.out.println("El ID ingresado no corresponde a un videojuego disponible. Inténtalo nuevamente.");
                        continue;
                    }

                    // Añadir el videojuego a la lista temporal
                    videojuegosSeleccionados.add(new Document("videojuego_id", idVideojuego)
                            .append("titulo", videojuegoSeleccionado.getTitulo())
                            .append("precio", videojuegoSeleccionado.getPrecio())
                            .append("cantidad", cantidad));

                    System.out.print("¿Deseas seguir añadiendo videojuegos al carrito? (s/n): ");
                    String respuesta = teclado.nextLine();
                    if (!respuesta.equalsIgnoreCase("s")) {
                        break;
                    }
                }

                // Procesar los videojuegos seleccionados al finalizar
                if (!videojuegosSeleccionados.isEmpty()) {
                    Document carritoExistente = carritoCollection.find(Filters.eq("usuario_id", usuarioIdIntegerActual)).first();

                    if (carritoExistente != null) {
                        // Añadir los videojuegos al carrito existente
                        List<Document> videojuegos = carritoExistente.getList("videojuegos", Document.class, new ArrayList<>());
                        videojuegos.addAll(videojuegosSeleccionados);

                        carritoCollection.updateOne(
                                Filters.eq("usuario_id", usuarioIdIntegerActual),
                                new Document("$set", new Document("videojuegos", videojuegos))
                        );
                        System.out.println("Videojuegos añadidos al carrito existente.");
                    } else {
                        // Crear un nuevo carrito si no existe
                        Document ultimoCarrito = carritoCollection.find().sort(Sorts.descending("carrito_id")).first();
                        int nuevoCarritoId = (ultimoCarrito != null) ? ultimoCarrito.getInteger("carrito_id") + 1 : 1;

                        Document nuevoCarrito = new Document("carrito_id", nuevoCarritoId)
                                .append("usuario_id", usuarioIdIntegerActual)
                                .append("videojuegos", videojuegosSeleccionados);

                        carritoCollection.insertOne(nuevoCarrito);
                        System.out.println("Nuevo carrito creado y videojuegos añadidos.");
                    }
                } else {
                    System.out.println("No se añadieron videojuegos al carrito.");
                }

                // Mostrar videojuegos directamente desde BaseX después de añadirlos
                System.out.println("\nVideojuegos disponibles en BaseX después de añadir al carrito:");
                for (Videojuego v : videojuegosDisponibles) {
                    System.out.printf("ID: %d | Título: %s | Precio: %.2f%n",
                            v.getId(), v.getTitulo(), v.getPrecio());
                }

            } catch (IOException e) {
                System.out.println("Error al consultar videojuegos o añadir al carrito: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    session.close();
                } catch (IOException e) {
                    System.out.println("Error al cerrar la conexión con BaseX.");
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Error: No se pudo establecer la conexión con BaseX.");
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

    private static String getNodeValue(org.w3c.dom.Document document, String tagName) {
        Node node = document.getElementsByTagName(tagName).item(0);
        return (node != null) ? node.getTextContent() : null;
    }


    // Método para parsear el resultado de la consulta y convertirlo en un objeto Videojuego
    private static Videojuego parsearVideojuego(String resultado) {
        try {
            // Preparar el documento XML desde el string
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(resultado));
            org.w3c.dom.Document doc = builder.parse(is);

            // Obtener valores del documento XML
            String idStr = getNodeValue(doc, "id");
            String titulo = getNodeValue(doc, "titulo");
            String descripcion = getNodeValue(doc, "descripcion");
            String precioStr = getNodeValue(doc, "precio");
            String disponibilidadStr = getNodeValue(doc, "disponibilidad");
            String genero = getNodeValue(doc, "genero");
            String desarrollador = getNodeValue(doc, "desarrollador");
            String edadMinimaStr = getNodeValue(doc, "edad_minima_recomendada");
            String plataforma = getNodeValue(doc, "plataforma");

            // Validar y convertir valores
            if (idStr == null || titulo == null || precioStr == null) {
                System.out.println("Error: Nodos esenciales (id, titulo o precio) están ausentes.");
                return null;
            }

            int id = Integer.parseInt(idStr);
            double precio = Double.parseDouble(precioStr);
            int disponibilidad = (disponibilidadStr != null) ? Integer.parseInt(disponibilidadStr) : 0;
            int edadMinima = (edadMinimaStr != null) ? Integer.parseInt(edadMinimaStr) : 0;

            // Crear y devolver el objeto Videojuego
            return new Videojuego(id, titulo, descripcion != null ? descripcion : "", precio, disponibilidad,
                    genero != null ? genero : "", desarrollador != null ? desarrollador : "", edadMinima,
                    plataforma != null ? plataforma : "");

        } catch (Exception e) {
            System.out.println("Error al parsear el videojuego: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }




    //Método para mostrar el carrito y coste total
    public void mostrarCarritoUsuario(Scanner teclado) {
        // Identificar al usuario primero
        identificarUsuarioPorEmail(teclado);

        // Si no se identificó al usuario, finalizar el método
        if (usuarioIdIntegerActual == null) {
            System.out.println("Error: No se ha identificado ningún usuario. No se puede proceder.");
            return;
        }

        MongoCollection<Document> carritoCollection = database.getCollection("Carrito");

        // Buscar el carrito del usuario identificado
        Document carrito = carritoCollection.find(Filters.eq("usuario_id", usuarioIdIntegerActual)).first();
        if (carrito == null) {
            System.out.println("No se encontró un carrito para el usuario identificado.");
            return;
        }

        List<Document> videojuegosEnCarrito = (List<Document>) carrito.get("videojuegos");

        double costeTotal = 0;

        // Mostrar datos del carrito
        System.out.println("Carrito del usuario:");
        for (Document videojuego : videojuegosEnCarrito) {
            int videojuegoId = videojuego.getInteger("videojuego_id");
            String titulo = videojuego.getString("titulo");
            int cantidad = videojuego.getInteger("cantidad");
            double precio = videojuego.getDouble("precio");

            costeTotal += precio * cantidad;

            System.out.println("ID del videojuego: " + videojuegoId);
            System.out.println("Título: " + titulo);
            System.out.println("Cantidad: " + cantidad);
            System.out.println("Precio: " + precio);
            System.out.println("Subtotal: " + (precio * cantidad));
            System.out.println();
        }

        // Mostrar coste total
        System.out.println("Coste total del carrito: " + costeTotal);
    }

    //Método para Comprar el carrito del usuario
    public void comprarCarritoUsuario(Scanner teclado) {
        // Identificar al usuario primero
        identificarUsuarioPorEmail(teclado);

        // Validar si se identificó al usuario
        if (usuarioIdIntegerActual == null) {
            System.out.println("Error: No se ha identificado ningún usuario. No se puede proceder.");
            return;
        }

        MongoCollection<Document> carritoCollection = database.getCollection("Carrito");
        MongoCollection<Document> comprasCollection = database.getCollection("Compras");

        // Buscar el carrito del usuario identificado
        Document carrito = carritoCollection.find(Filters.eq("usuario_id", usuarioIdIntegerActual)).first();
        if (carrito == null) {
            System.out.println("No se encontró un carrito para el usuario identificado.");
            return;
        }

        // Validar que el campo "videojuegos" exista y no sea nulo
        List<Document> videojuegosEnCarrito = (List<Document>) carrito.get("videojuegos");
        if (videojuegosEnCarrito == null || videojuegosEnCarrito.isEmpty()) {
            System.out.println("El carrito del usuario está vacío o no tiene videojuegos.");
            return;
        }

        double costeTotal = 0.0;

        // Mostrar datos del carrito
        System.out.println("Carrito del usuario:");
        for (Document videojuego : videojuegosEnCarrito) {
            // Validar los campos dentro de cada videojuego
            Integer videojuegoId = videojuego.getInteger("videojuego_id", 0);
            String titulo = videojuego.getString("titulo");
            Integer cantidad = videojuego.getInteger("cantidad", 0);
            Double precio = videojuego.getDouble("precio");

            // Verificar si alguno de los campos requeridos es nulo
            if (videojuegoId == null || titulo == null || cantidad == null || precio == null) {
                System.out.println("Advertencia: Videojuego con datos incompletos encontrado. Saltando este videojuego.");
                continue;
            }

            costeTotal += precio * cantidad;

            System.out.println("ID del videojuego: " + videojuegoId);
            System.out.println("Título: " + titulo);
            System.out.println("Cantidad: " + cantidad);
            System.out.println("Precio: " + precio);
            System.out.println("Subtotal: " + (precio * cantidad));
            System.out.println();
        }

        // Mostrar coste total
        System.out.println("Coste total del carrito: " + costeTotal);

        // Pedir confirmación para la compra
        System.out.print("¿Deseas confirmar la compra del carrito? (s/n): ");
        String confirmacion = teclado.nextLine().trim().toLowerCase();

        if (confirmacion.equals("s")) {
            // Generar un nuevo ID de compra
            int nuevaCompraId = obtenerUltimoCompraId() + 1;

            // Crear un documento de compra
            Document compra = new Document("compra_id", nuevaCompraId)
                    .append("usuario_id", usuarioIdIntegerActual)
                    .append("videojuegos", videojuegosEnCarrito)
                    .append("fecha_compra", new Date())
                    .append("total", costeTotal);

            // Insertar el documento de compra en la colección Compras
            comprasCollection.insertOne(compra);

            // Eliminar el carrito del usuario
            carritoCollection.deleteOne(Filters.eq("usuario_id", usuarioIdIntegerActual));

            System.out.println("Compra realizada exitosamente. El carrito ha sido vaciado.");
        } else {
            System.out.println("Compra cancelada.");
        }
    }



    //Método para mostrar compras del usuario
    public void mostrarComprasUsuario(Scanner teclado) {
        // Identificar al usuario primero
        identificarUsuarioPorEmail(teclado);

        // Si no se identificó al usuario, finalizar el método
        if (usuarioIdIntegerActual == null) {
            System.out.println("Error: No se ha identificado ningún usuario. No se puede proceder.");
            return;
        }

        MongoCollection<Document> comprasCollection = database.getCollection("Compras");

        // Buscar las compras del usuario identificado
        List<Document> comprasUsuario = comprasCollection.find(Filters.eq("usuario_id", usuarioIdIntegerActual)).into(new ArrayList<>());

        if (comprasUsuario.isEmpty()) {
            System.out.println("No se encontraron compras para el usuario identificado.");
            return;
        }

        // Mostrar datos de las compras
        System.out.println("Compras del usuario:");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (Document compra : comprasUsuario) {
            int compraId = compra.getInteger("compra_id");
            Object fechaCompraObj = compra.get("fecha_compra");
            Date fechaCompra = null;

            if (fechaCompraObj instanceof String) {
                try {
                    fechaCompra = dateFormat.parse((String) fechaCompraObj);
                } catch (ParseException e) {
                    System.out.println("Error al convertir la fecha de compra: " + e.getMessage());
                }
            } else if (fechaCompraObj instanceof Date) {
                fechaCompra = (Date) fechaCompraObj;
            }

            double total = compra.getDouble("total");
            List<Document> videojuegos = (List<Document>) compra.get("videojuegos");

            System.out.println("ID de la compra: " + compraId);
            System.out.println("Fecha de la compra: " + fechaCompra);
            System.out.println("Total: " + total);

            // Mostrar datos de los videojuegos en la compra
            for (Document videojuego : videojuegos) {
                int videojuegoId = videojuego.getInteger("videojuego_id");
                String titulo = videojuego.getString("titulo");
                int cantidad = videojuego.getInteger("cantidad");
                double precio = videojuego.getDouble("precio");

                System.out.println("\tID del videojuego: " + videojuegoId);
                System.out.println("\tTítulo: " + titulo);
                System.out.println("\tCantidad: " + cantidad);
                System.out.println("\tPrecio: " + precio);
                System.out.println("\tSubtotal: " + (precio * cantidad));
            }

            System.out.println();
        }
    }

    //Método para consultar el coste del carrito
    public void consultaCosteCarritos() {
        MongoCollection<Document> carritoCollection = database.getCollection("Carrito");

        // Obtener todos los carritos
        List<Document> carritos = carritoCollection.find().into(new ArrayList<>());

        // Lista para almacenar los resultados
        List<CarritoCoste> resultados = new ArrayList<>();

        // Calcular el coste total de cada carrito
        for (Document carrito : carritos) {
            // Validar campos necesarios en el documento
            Integer carritoId = carrito.getInteger("carrito_id");
            Integer usuarioId = carrito.getInteger("usuario_id");
            List<Document> videojuegosEnCarrito = (List<Document>) carrito.get("videojuegos");

            if (carritoId == null || usuarioId == null || videojuegosEnCarrito == null) {
                System.out.println("Advertencia: Carrito con datos incompletos encontrado. Saltando este carrito.");
                continue; // Omitir este documento
            }

            double costeTotal = 0;

            for (Document videojuego : videojuegosEnCarrito) {
                Double precio = videojuego.getDouble("precio");
                Integer cantidad = videojuego.getInteger("cantidad");

                // Validar que el videojuego contiene los datos necesarios
                if (precio == null || cantidad == null) {
                    System.out.println("Advertencia: Videojuego con datos incompletos en el carrito " + carritoId + ". Saltando este videojuego.");
                    continue; // Omitir este videojuego
                }

                costeTotal += precio * cantidad;
            }

            // Agregar el resultado a la lista
            resultados.add(new CarritoCoste(carritoId, usuarioId, costeTotal));
        }

        // Ordenar los resultados por el coste total de forma descendente
        resultados.sort((c1, c2) -> Double.compare(c2.getCosteTotal(), c1.getCosteTotal()));

        // Mostrar los resultados
        System.out.println("Coste de cada carrito ordenado por el total de forma descendente:");
        for (CarritoCoste resultado : resultados) {
            System.out.println("Carrito ID: " + resultado.getCarritoId());
            System.out.println("Usuario ID: " + resultado.getUsuarioId());
            System.out.println("Coste Total: " + resultado.getCosteTotal());
            System.out.println();
        }
    }

    //Método para consultar gasto total de los usuarios
    public void consultaTotalGastadoUsuarios() {
        MongoCollection<Document> comprasCollection = database.getCollection("Compras");

        // Obtener todas las compras
        List<Document> compras = comprasCollection.find().into(new ArrayList<>());

        // Mapa para almacenar el total gastado por cada usuario
        Map<Integer, Double> totalGastadoPorUsuario = new HashMap<>();

        // Calcular el total gastado por cada usuario
        for (Document compra : compras) {
            int usuarioId = compra.getInteger("usuario_id");
            double totalCompra = compra.getDouble("total");

            totalGastadoPorUsuario.put(usuarioId, totalGastadoPorUsuario.getOrDefault(usuarioId, 0.0) + totalCompra);
        }

        // Convertir el mapa a una lista de objetos UsuarioGasto
        List<UsuarioGasto> resultados = totalGastadoPorUsuario.entrySet().stream()
                .map(entry -> new UsuarioGasto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        // Ordenar los resultados por el total gastado de forma ascendente
        resultados.sort(Comparator.comparingDouble(UsuarioGasto::getTotalGastado));

        // Mostrar los resultados
        System.out.println("Total gastado por cada usuario ordenado por el total de forma ascendente:");
        for (UsuarioGasto resultado : resultados) {
            System.out.println("Usuario ID: " + resultado.getUsuarioId());
            System.out.println("Total Gastado: " + resultado.getTotalGastado());
            System.out.println();
        }
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

