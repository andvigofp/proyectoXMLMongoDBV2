package org.example;


import org.example.ConexionBaseX.ConexionBaseX;
import org.example.Metodos.MetodosMongoDB;
import org.example.Metodos.MetodosXML;

import java.util.Scanner;


public class AplicacionXMLMongoDB {
    private static Scanner teclado;
    private static MetodosXML metodosXML = new MetodosXML();
    private static MetodosMongoDB metodosMongoDB = new MetodosMongoDB();

    public static void main(String[] args) {
        teclado = new Scanner(System.in);

        int opcion;
        do {
            System.out.println("\n===== MENÚ BASE DE DATOS XML =====");
            System.out.println("1. Modificar valor de un elemento según ID");
            System.out.println("2. Eliminar videojuego según ID");
            System.out.println("3. Consulta 1: Listar videojuegos ordenados por plataforma y título");
            System.out.println("4. Consulta 2: Videojuegos con edad mínima recomendada menor o igual a X");
            System.out.println("5. Consulta 3: Videojuego más barato por plataforma");
            System.out.println("6. Consulta 4: Buscar videojuegos por descripción (subcadena)");
            System.out.println("7. Consulta 5: Cantidad total de videojuegos por plataforma y porcentaje");
            System.out.println("8. Consulta 6: Calcular precio total de videojuegos disponibles");
            System.out.println("\n===== MENÚ BASE DE DATOS MONGODB_XML =====");
            System.out.println("9. Crear un nuevo usuario");
            System.out.println("10. Identificar usuario según el email");
            System.out.println("11. Borrar un usuario");
            System.out.println("12. Modificar el valor de un campo del usuario");
            System.out.println("13. Añadir videojuegos al carrito del usuario");
            System.out.println("14. Mostrar el carrito del usuario");
            System.out.println("15. Comprar el carrito del usuario");
            System.out.println("16. Mostrar las compras del usuario");
            System.out.println("17. Consulta 1: Coste de cada carrito ordenado por total");
            System.out.println("18. Consulta 2: Total gastado por cada usuario ordenado por total");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");
            opcion = teclado.nextInt();
            teclado.nextLine(); // Limpiar el buffer
            
            switch (opcion) {
                case 1:
                    metodosXML.modificarElementoPorID(teclado);
                    break;
                case 2:
                    metodosXML.eliminarVideojuegoPorID(teclado);
                    break;
                case 3:
                    metodosXML.listarVideojuegosOrdenados();
                    break;
                case 4:
                    metodosXML.videojuegosPorEdad();
                    break;
                case 5:
                    metodosXML.videojuegoMasBaratoPorPlataforma();
                    break;
                case 6:
                    metodosXML.buscarVideojuegosPorDescripcion();
                    break;
                case 7:
                    metodosXML.cantidadYPorcentajePorPlataforma();
                    break;
                case 8:
                    metodosXML.calcularPrecioTotalDisponibles();
                    break;
                case 9:
                    metodosMongoDB.crearUsuario(teclado);
                    break;
                case 10:
                    metodosMongoDB.identificarUsuarioPorEmail(teclado);
                    break;
                case 11:
                    metodosMongoDB.borrarUsuario(teclado);
                    break;
                case 12:
                    metodosMongoDB.modificarCampoUsuario(teclado);
                    break;
                case 13:
                    metodosMongoDB.anadirVideojuegoAlCarrito(teclado);
                    break;
                case 14:
                    metodosMongoDB.mostrarCarritoUsuario(teclado);
                    break;
                case 15:
                    metodosMongoDB.comprarCarritoUsuario(teclado);
                    break;
                case 16:
                    metodosMongoDB.mostrarComprasUsuario(teclado);
                    break;
                case 17:
                    metodosMongoDB.consultaCosteCarritos();
                    break;
                case 18:
                    metodosMongoDB.consultaTotalGastadoUsuarios();
                    break;
                case 0:
                    System.out.println("Saliendo del programa.");
                    break;
                default:
                    System.out.println("Opción no válida, intenta de nuevo.");
            }
        }while (opcion!=0);
    }
}

