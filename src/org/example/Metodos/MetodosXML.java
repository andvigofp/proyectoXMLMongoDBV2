package org.example.Metodos;


import org.basex.examples.api.BaseXClient;
import org.example.ConexionBaseX.ConexionBaseX;

import java.io.IOException;
import java.util.*;

public class MetodosXML {
    private static ConexionBaseX conexionBasex = new ConexionBaseX();

    //Método para modifciar el titulo por id
    public void modificarElementoPorID(Scanner teclado) {
        int id = -1; // Inicializamos con un valor inválido para que entre al ciclo
        boolean idValido = false;

        // Obtener la conexión activa desde conexionBasex
        BaseXClient session = conexionBasex.conexionBaseX();

        if (session != null) {
            try {
                // Ciclo para validar la existencia del ID y asegurar que sea un número
                while (!idValido) {
                    try {
                        // Pedir al usuario el ID del videojuego a modificar
                        id = introducirInt(teclado, "Introduce el ID del videojuego a modificar (solo números):");

                        // Validar si el ID existe en la base de datos BaseX
                        String consultaValidarID = """
                            for $v in //videojuego[id='%d']
                            return data($v/id)
                        """.formatted(id);

                        BaseXClient.Query validarQuery = session.query(consultaValidarID);

                        if (validarQuery.more()) {
                            idValido = true; // El ID es válido si se encuentra en la base de datos
                            validarQuery.close();
                        } else {
                            System.out.println("No se encontró ningún videojuego con el ID especificado. Por favor, introduce un ID válido.");
                            validarQuery.close();
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Error: Solo se permiten números. Intenta nuevamente.");
                        teclado.nextLine(); // Limpiar el buffer del escáner
                    }
                }

                // Mostrar el estado actual del videojuego
                String consultaEstado = """
                    for $v in //videojuego[id='%d']
                    return string-join((
                        "Título actual: ", data($v/titulo),
                        "\nDescripción actual: ", data($v/descripcion),
                        "\nPrecio actual: ", data($v/precio),
                        "\nDisponibilidad actual: ", data($v/disponibilidad),
                        "\nGénero actual: ", data($v/genero),
                        "\nDesarrollador actual: ", data($v/desarrollador),
                        "\nEdad mínima recomendada actual: ", data($v/edad_minima_recomendada),
                        "\nPlataforma actual: ", data($v/plataforma)
                    ), "")
                """.formatted(id);

                BaseXClient.Query query = session.query(consultaEstado);
                System.out.println("Estado actual del videojuego:\n" + query.next());
                query.close();

                // Mostrar el menú para seleccionar el campo a modificar
                System.out.println("Selecciona el campo a modificar:");
                System.out.println("1. Título");
                System.out.println("2. Descripción");
                System.out.println("3. Precio");
                System.out.println("4. Disponibilidad");
                System.out.println("5. Género");
                System.out.println("6. Desarrollador");
                System.out.println("7. Edad mínima recomendada");
                System.out.println("8. Plataforma");

                int opcion = introducirInt(teclado, "Introduce el número de la opción:");
                teclado.nextLine(); // Limpiar el buffer del escáner

                String nuevoValor = null;

                switch (opcion) {
                    case 1:
                        nuevoValor = introducirString(teclado, "Introduce el nuevo valor para el título:");
                        modificarCampo(session, id, "titulo", nuevoValor);
                        break;
                    case 2:
                        nuevoValor = introducirString(teclado, "Introduce el nuevo valor para la descripción:");
                        modificarCampo(session, id, "descripcion", nuevoValor);
                        break;
                    case 3:
                        double nuevoPrecio = pedirDouble(teclado, "Introduce el nuevo valor del precio:");
                        modificarCampo(session, id, "precio", String.valueOf(nuevoPrecio));
                        break;
                    case 4:
                        int nuevaDisponibilidad = introducirInt(teclado, "Introduce el nuevo valor de disponibilidad:");
                        modificarCampo(session, id, "disponibilidad", String.valueOf(nuevaDisponibilidad));
                        break;
                    case 5:
                        nuevoValor = introducirString(teclado, "Introduce el nuevo valor para el género:");
                        modificarCampo(session, id, "genero", nuevoValor);
                        break;
                    case 6:
                        nuevoValor = introducirString(teclado, "Introduce el nuevo valor para el desarrollador:");
                        modificarCampo(session, id, "desarrollador", nuevoValor);
                        break;
                    case 7:
                        int nuevaEdadMinimaRecomendada = introducirInt(teclado, "Introduce el nuevo valor para la edad mínima recomendada:");
                        modificarCampo(session, id, "edad_minima_recomendada", String.valueOf(nuevaEdadMinimaRecomendada));
                        break;
                    case 8:
                        nuevoValor = introducirString(teclado, "Introduce el nuevo valor para la plataforma:");
                        modificarCampo(session, id, "plataforma", nuevoValor);
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        return;
                }

                // Confirmar el cambio
                String consultaConfirmar = """
                    for $v in //videojuego[id='%d']
                    return string-join((
                        "Nuevo título: ", data($v/titulo),
                        "\nNueva descripción: ", data($v/descripcion),
                        "\nNuevo precio: ", data($v/precio),
                        "\nNueva disponibilidad: ", data($v/disponibilidad),
                        "\nNuevo género: ", data($v/genero),
                        "\nNuevo desarrollador: ", data($v/desarrollador),
                        "\nNueva edad mínima recomendada: ", data($v/edad_minima_recomendada),
                        "\nNueva plataforma: ", data($v/plataforma)
                    ), "")
                """.formatted(id);

                query = session.query(consultaConfirmar);

                if (query.more()) {
                    System.out.println("Estado después de la modificación:\n" + query.next());
                }
                query.close();

            } catch (IOException e) {
                System.out.println("Error al modificar el elemento en la base de datos.");
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

    //Método para modificar un campo
    private static void modificarCampo(BaseXClient session, int id, String campo, String nuevoValor) throws IOException {
        try {
            // Modificar el campo específico usando session.query
            String comandoModificar = """
                        for $v in //videojuego[id='%d']
                        return (
                            replace value of node $v/%s with '%s'
                        )
                    """.formatted(id, campo, nuevoValor);

            BaseXClient.Query query = session.query(comandoModificar);
            while (query.more()) {
                query.next(); // Ejecuta la modificación
            }
            System.out.println("¡Elemento modificado correctamente!");
            query.close();
        }catch (Exception e) {
            System.out.println("Error no se pudo modificar el campo");
            e.printStackTrace();
        }
    }


    //Método para elminar el por id
    public void eliminarVideojuegoPorID(Scanner teclado) {
        int id = -1; // Inicializamos con un valor inválido para que entre al ciclo
        boolean idValido = false;

        // Obtener la conexión activa desde conexionBaseX
        BaseXClient session = conexionBasex.conexionBaseX();

        if (session != null) {
            try {
                // Ciclo para validar la existencia del ID y asegurar que sea un número
                while (!idValido) {
                    try {
                        // Pedir al usuario el ID del videojuego a eliminar
                        id = introducirInt(teclado, "Introduce el ID del videojuego a eliminar (solo números):");
                        teclado.nextLine();

                        // Validar si el ID existe en la base de datos BaseX
                        String consultaValidarID = """
                            for $v in //videojuego[id='%d']
                            return data($v/id)
                        """.formatted(id);

                        BaseXClient.Query validarQuery = session.query(consultaValidarID);

                        if (validarQuery.more()) {
                            idValido = true; // El ID es válido si se encuentra en la base de datos
                            validarQuery.close();
                        } else {
                            System.out.println("No se encontró ningún videojuego con el ID especificado. Por favor, introduce un ID válido.");
                            validarQuery.close();
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Error: Solo se permiten números. Intenta nuevamente.");
                        teclado.nextLine(); // Limpiar el buffer del escáner
                    }
                }

                // Mostrar el videojuego que se desea eliminar
                String consultaEstado = """
                    for $v in //videojuego[id='%d']
                    return concat("Videojuego encontrado: ", data($v/titulo))
                """.formatted(id);

                BaseXClient.Query query = session.query(consultaEstado);
                System.out.println("Estado actual del videojuego:\n" + query.next());
                query.close();

                // Confirmar la eliminación con el usuario
                System.out.print("¿Estás seguro de que deseas eliminar este videojuego? (s/n): ");
                String confirmacion = teclado.nextLine().trim().toLowerCase();
                if (!confirmacion.equals("s")) {
                    System.out.println("Operación cancelada.");
                    return;
                }

                // Comando XQuery para eliminar el videojuego
                String comandoEliminar = """
                    for $v in //videojuego[id='%d']
                    return delete node $v
                """.formatted(id);

                // Ejecutar el comando
                BaseXClient.Query queryEliminar = session.query(comandoEliminar);
                while (queryEliminar.more()) {
                    queryEliminar.next(); // Ejecutar la eliminación
                }
                queryEliminar.close();

                System.out.println("¡Videojuego eliminado correctamente!");

                // Mostrar todos los videojuegos restantes para verificar
                String consultaTodos = """
                    for $v in //videojuego
                    return string-join((
                        "ID: ", data($v/id),
                        " | Título: ", data($v/titulo),
                        " | Precio: ", data($v/precio)
                    ), "\n")
                """;

                BaseXClient.Query queryTodos = session.query(consultaTodos);
                System.out.println("\nEstado actual de todos los videojuegos en la base de datos:");
                while (queryTodos.more()) {
                    System.out.println(queryTodos.next());
                }
                queryTodos.close();

            } catch (IOException e) {
                System.out.println("Error al eliminar el videojuego de la base de datos.");
                e.printStackTrace();
            } finally {
                // Cerrar la conexión
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



    //Listar  todos los videojuegos ordenados por plataforma y en segundo lugar por título
    public void listarVideojuegosOrdenados() {
        // Obtener la conexión
        BaseXClient session = conexionBasex.conexionBaseX();

        if (session != null) {
            try {
                System.out.println("Listando videojuegos ordenados por plataforma y título...");

                // Comando XQuery ajustado
                String comando = """
                            for $v in //videojuego
                            order by $v/plataforma, $v/titulo
                            return concat(
                                "ID: ", data($v/id), " | Título: ", data($v/titulo), 
                                " | Precio: $", data($v/precio), 
                                " | Disponibilidad: ", data($v/disponibilidad), 
                                " | Edad Mínima Recomendada: ", data($v/edad_minima_recomendada), 
                                " | Plataforma: ", data($v/plataforma)
                            )
                        """;

                // Ejecutar el comando
                BaseXClient.Query query = session.query(comando);

                // Iterar sobre los resultados y mostrarlos
                while (query.more()) {
                    System.out.println(query.next());
                }

                query.close(); // Cerrar la consulta
            } catch (IOException e) {
                System.out.println("Error al listar los videojuegos.");
                e.printStackTrace();
            } finally {
                try {
                    session.close(); // Cerrar la conexión después de usarla
                } catch (IOException e) {
                    System.out.println("Error al cerrar la conexión.");
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Error: No se pudo establecer la conexión con la base de datos.");
        }
    }


    //Método para mostrar los videojuegos por edad mínima recomendada, ordenada por edad menor a mayor
   public void videojuegosPorEdad() {
        // Edad máxima predefinida
        int edadMaxima = 6;

        // Obtener la conexión activa desde conexionBaseX()
        BaseXClient session = conexionBasex.conexionBaseX();

        if (session != null) {
            try {
                System.out.println("Listando videojuegos con edad mínima recomendada menor o igual a " + edadMaxima + "...");

                // Comando XQuery para filtrar y ordenar videojuegos por edad mínima recomendada
                String comando = String.format("""
                        for $v in //videojuego
                        where $v/edad_minima_recomendada <= %d
                        order by $v/edad_minima_recomendada
                        return concat(
                            "ID: ", data($v/id), " | Título: ", data($v/titulo), 
                            " | Precio: $", data($v/precio), 
                            " | Disponibilidad: ", data($v/disponibilidad), 
                            " | Edad Mínima: ", data($v/edad_minima_recomendada), 
                            " | Plataforma: ", data($v/plataforma)
                        )
                    """, edadMaxima);

                // Ejecutar el comando
                BaseXClient.Query query = session.query(comando);

                // Iterar y mostrar los resultados
                while (query.more()) {
                    System.out.println(query.next());
                }

                query.close();

            } catch (IOException e) {
                System.out.println("Error al listar los videojuegos por edad mínima recomendada.");
                e.printStackTrace();
            } finally {
                try {
                    session.close(); // Finalmente, cierra la conexión cuando hayas terminado todas las operaciones
                } catch (IOException e) {
                    System.out.println("Error al cerrar la conexión.");
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Error: No se pudo establecer la conexión con la base de datos.");
        }
    }

    //Método para buscar por una subcadena
    public void buscarVideojuegosPorDescripcion() {
        // Definir una subcadena fija para buscar en la descripción
        String subcadena = "aventura"; // Cambia esta subcadena según tus necesidades

        // Obtener la conexión activa desde conexionBasex
        BaseXClient session = conexionBasex.conexionBaseX();

        if (session != null) {
            try {
                System.out.println("Buscando videojuegos cuya descripción contiene la subcadena: " + subcadena);

                // Comando XQuery para buscar y ordenar videojuegos por género
                String comando = String.format("""
                        for $v in //videojuego
                        where contains(lower-case($v/descripcion), lower-case('%s'))
                        order by lower-case($v/genero)
                        return concat(
                            "Título: ", data($v/titulo), " | Género: ", data($v/genero)
                        )
                    """, subcadena);

                // Ejecutar el comando XQuery
                String resultado = session.execute("xquery " + comando);

                // Mostrar los resultados
                if (!resultado.isEmpty()) {
                    System.out.println("Resultados:");
                    System.out.println(resultado);
                } else {
                    System.out.println("No se encontraron videojuegos cuya descripción contenga la subcadena: " + subcadena);
                }

            } catch (IOException e) {
                System.out.println("Error al buscar videojuegos por descripción.");
                e.printStackTrace();
            } finally {
                // Cerrar la conexión
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


    //Método para mostrar el videojuego más barato por cada plataforma
    public void videojuegoMasBaratoPorPlataforma() {
        // Obtener la conexión activa desde conexionBasex
        BaseXClient session = conexionBasex.conexionBaseX();

        if (session != null) {
            try {
                // Comando XQuery para obtener el videojuego más barato por plataforma en una sola consulta
                String comandoVideojuegosMasBaratos = """
                for $plataforma in distinct-values(//videojuego/plataforma)
                let $videojuego := (
                    for $v in //videojuego[plataforma = $plataforma]
                    order by $v/precio
                    return $v
                )[1]
                return
                    <resultado>
                        <plataforma>{$plataforma}</plataforma>
                        <titulo>{$videojuego/titulo}</titulo>
                        <precio>{$videojuego/precio}</precio>
                    </resultado>
            """;

                // Ejecutar el comando XQuery
                String resultado = session.execute("xquery " + comandoVideojuegosMasBaratos);

                // Mostrar los resultados
                if (!resultado.isEmpty()) {
                    // Dividir los resultados en elementos individuales
                    String[] resultados = resultado.split("</resultado>");
                    for (String res : resultados) {
                        // Extraer y mostrar la plataforma, título y precio de cada resultado
                        String plataforma = res.replaceAll("(?s).*<plataforma>(.*?)</plataforma>.*", "$1");
                        String titulo = res.replaceAll("(?s).*<titulo>(.*?)</titulo>.*", "$1");
                        String precio = res.replaceAll("(?s).*<precio>(.*?)</precio>.*", "$1");
                        System.out.printf("Plataforma: %s | Título: %s | Precio: $%s%n", plataforma, titulo, precio);
                    }
                } else {
                    System.out.println("No se encontraron videojuegos.");
                }

            } catch (IOException e) {
                System.out.println("Error al listar el videojuego más barato por plataforma.");
                e.printStackTrace();
            } finally {
                // Cerrar la conexión
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


    //Mostrar la cantidad total de videojuegos para cada plataforma (teniendo en cuenta el elemento disponibilidad) y calcular el porcentaje que representa respecto al total de videojuegos.
    //Se deberá mostrar la información ordenada de forma descendente por la cantidad de videojuegos.
    public void cantidadYPorcentajePorPlataforma() {
        // Obtener la conexión activa desde conexionBasex
        BaseXClient session = conexionBasex.conexionBaseX();

        if (session != null) {
            try {
                System.out.println("Mostrando la cantidad total de videojuegos por plataforma...");

                // Comando XQuery para obtener la cantidad total de videojuegos por plataforma y su porcentaje respecto al total
                String comando = """
                let $totalVideojuegos := sum(//videojuego/disponibilidad)
                for $plataforma in distinct-values(//videojuego/plataforma)
                let $cantidad := sum(//videojuego[plataforma = $plataforma]/disponibilidad)
                order by $cantidad descending
                return concat(
                    "Plataforma: ", data($plataforma), 
                    " | Cantidad Total: ", $cantidad, 
                    " | Porcentaje: ", round(($cantidad div $totalVideojuegos) * 100), "%"
                )
            """;

                // Ejecutar el comando XQuery
                String resultado = session.execute("xquery " + comando);

                // Mostrar los resultados
                if (!resultado.isEmpty()) {
                    System.out.println("Resultados:");
                    System.out.println(resultado);
                } else {
                    System.out.println("No se encontraron videojuegos.");
                }

            } catch (IOException e) {
                System.out.println("Error al listar la cantidad total de videojuegos por plataforma.");
                e.printStackTrace();
            } finally {
                // Cerrar la conexión
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

    //Método para calcular el precio total de los videojuegos disponibles
    public void calcularPrecioTotalDisponibles() {
        // Obtener la conexión activa desde conexionBasex
        BaseXClient session = conexionBasex.conexionBaseX();

        if (session != null) {
            try {
                System.out.println("Calculando el precio total de todos los videojuegos disponibles...");

                // Comando XQuery para calcular el precio total de todos los videojuegos disponibles
                String comando = """
                            let $total := sum(
                                for $v in //videojuego
                                return $v/precio * $v/disponibilidad
                            )
                            return concat("Precio total para comprar todos los videojuegos disponibles: $", $total)
                        """;

                // Ejecutar el comando
                BaseXClient.Query query = session.query(comando);

                // Mostrar el resultado
                if (query.more()) {
                    System.out.println(query.next());
                } else {
                    System.out.println("No se encontraron videojuegos disponibles.");
                }

                query.close();

            } catch (IOException e) {
                System.out.println("Error al calcular el precio total de los videojuegos disponibles.");
                e.printStackTrace();
            } finally {
                // Cerrar la conexión
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
