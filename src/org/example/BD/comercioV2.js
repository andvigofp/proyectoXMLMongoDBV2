const { MongoClient } = require('mongodb');

// URI de conexión a MongoDB
const uri = 'mongodb://localhost:27017';

// Crear una nueva instancia de MongoClient
const client = new MongoClient(uri);

async function run() {
    try {
        // Conectar al cliente de MongoDB
        await client.connect();
        console.log('Conexión exitosa a MongoDB');

        // Obtener la base de datos 'comercio'
        const database = client.db('comercio');

        // Crear y llenar la colección 'Usuarios'
        const usuariosCollection = database.collection('Usuarios');
        const usuarios = [
            {
                "usuario_id": 1,
                "nombre": "Juan Pérez",
                "correo": "juan.perez@example.com",
                "contrasena": "hashed_password1",
                "direccion": "Calle Falsa 123, Ciudad, País",
                "telefono": "123-456-7890",
                "edad": 15
            },
            {
                "usuario_id": 2,
                "nombre": "María García",
                "correo": "maria.garcia@example.com",
                "contrasena": "hashed_password2",
                "direccion": "Avenida Siempre Viva 742, Ciudad, País",
                "telefono": "098-765-4321",
                "edad": 20
            },
            {
                "usuario_id": 3,
                "nombre": "Carlos López",
                "correo": "carlos.lopez@example.com",
                "contrasena": "hashed_password3",
                "direccion": "Plaza Mayor 1, Ciudad, País",
                "telefono": "111-222-3333",
                "edad": 18
            },
            {
                "usuario_id": 4,
                "nombre": "Ana Fernández",
                "correo": "ana.fernandez@example.com",
                "contrasena": "hashed_password4",
                "direccion": "Calle del Pez 23, Ciudad, País",
                "telefono": "444-555-6666",
                "edad": 25
            },
            {
                "usuario_id": 5,
                "nombre": "Luis Martínez",
                "correo": "luis.martinez@example.com",
                "contrasena": "hashed_password5",
                "direccion": "Gran Vía 100, Ciudad, País",
                "telefono": "777-888-9999",
                "edad": 30
            }
        ];
        await usuariosCollection.insertMany(usuarios);
        console.log('Usuarios insertados exitosamente');

        // Crear y llenar la colección 'Carrito'
        const carritoCollection = database.collection('Carrito');
        const carritos = [
            {
                "carrito_id": 1,
                "usuario_id": 1,
                "videojuegos": [
                    {
                        "videojuego_id": 1,
                        "titulo": "The Legend of Zelda: Breath of the Wild",
                        "cantidad": 1,
                        "precio": 59.99
                    },
                    {
                        "videojuego_id": 11,
                        "titulo": "Super Mario Odyssey",
                        "cantidad": 2,
                        "precio": 49.99
                    }
                ]
            },
            {
                "carrito_id": 2,
                "usuario_id": 2,
                "videojuegos": [
                    {
                        "videojuego_id": 2,
                        "titulo": "God of War",
                        "cantidad": 1,
                        "precio": 69.99
                    },
                    {
                        "videojuego_id": 7,
                        "titulo": "Red Dead Redemption 2",
                        "cantidad": 1,
                        "precio": 39.99
                    }
                ]
            },
            {
                "carrito_id": 3,
                "usuario_id": 3,
                "videojuegos": [
                    {
                        "videojuego_id": 3,
                        "titulo": "Cyberpunk 2077",
                        "cantidad": 1,
                        "precio": 49.99
                    },
                    {
                        "videojuego_id": 9,
                        "titulo": "Animal Crossing: New Horizons",
                        "cantidad": 3,
                        "precio": 59.99
                    }
                ]
            },
            {
                "carrito_id": 4,
                "usuario_id": 4,
                "videojuegos": [
                    {
                        "videojuego_id": 4,
                        "titulo": "The Witcher 3: Wild Hunt",
                        "cantidad": 1,
                        "precio": 39.99
                    },
                    {
                        "videojuego_id": 5,
                        "titulo": "Persona 5 Royal",
                        "cantidad": 2,
                        "precio": 59.99
                    }
                ]
            },
            {
                "carrito_id": 5,
                "usuario_id": 5,
                "videojuegos": [
                    {
                        "videojuego_id": 6,
                        "titulo": "Minecraft",
                        "cantidad": 4,
                        "precio": 29.99
                    },
                    {
                        "videojuego_id": 8,
                        "titulo": "Among Us",
                        "cantidad": 5,
                        "precio": 5.99
                    }
                ]
            }
        ];
        await carritoCollection.insertMany(carritos);
        console.log('Carritos insertados exitosamente');

        // Crear y llenar la colección 'Compras'
        const comprasCollection = database.collection('Compras');
        const compras = [
            {
                "compra_id": 1,
                "usuario_id": 1,
                "videojuegos": [
                    {
                        "videojuego_id": 1,
                        "titulo": "The Legend of Zelda: Breath of the Wild",
                        "cantidad": 1,
                        "precio": 59.99
                    }
                ],
                "fecha_compra": "2025-03-10T14:48:00.000Z",
                "total": 59.99
            },
            {
                "compra_id": 2,
                "usuario_id": 2,
                "videojuegos": [
                    {
                        "videojuego_id": 2,
                        "titulo": "God of War",
                        "cantidad": 1,
                        "precio": 69.99
                    }
                ],
                "fecha_compra": "2025-03-11T09:15:00.000Z",
                "total": 69.99
            },
            {
                "compra_id": 3,
                "usuario_id": 3,
                "videojuegos": [
                    {
                        "videojuego_id": 3,
                        "titulo": "Cyberpunk 2077",
                        "cantidad": 1,
                        "precio": 49.99
                    },
                    {
                        "videojuego_id": 9,
                        "titulo": "Animal Crossing: New Horizons",
                        "cantidad": 1,
                        "precio": 59.99
                    }
                ],
                "fecha_compra": "2025-03-12T16:30:00.000Z",
                "total": 109.98
            },
            {
                "compra_id": 4,
                "usuario_id": 4,
                "videojuegos": [
                    {
                        "videojuego_id": 4,
                        "titulo": "The Witcher 3: Wild Hunt",
                        "cantidad": 1,
                        "precio": 39.99
                    }
                ],
                "fecha_compra": "2025-03-13T10:00:00.000Z",
                "total": 39.99
            },
            {
                "compra_id": 5,
                "usuario_id": 5,
                "videojuegos": [
                    {
                        "videojuego_id": 6,
                        "titulo": "Minecraft",
                        "cantidad": 1,
                        "precio": 29.99
                    },
                    {
                        "videojuego_id": 8,
                        "titulo": "Among Us",
                        "cantidad": 1,
                        "precio": 5.99
                    }
                ],
                "fecha_compra": "2025-03-14T11:20:00.000Z",
                "total": 35.98
            }
        ];
        await comprasCollection.insertMany(compras);
        console.log('Compras insertadas exitosamente');

    } finally {
        // Cerrar la conexión al cliente de MongoDB
        await client.close();
    }
}

run().catch(console.dir);