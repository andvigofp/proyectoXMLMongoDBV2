// Insertar los datos en la colección 'Usuarios'
db.Usuarios.insertMany([
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
]);

// Insertar los datos en la colección 'Carrito'
db.Carrito.insertMany([
    {
        "carrito_id": 1,
        "usuario_id": 1,
        "videojuegos": [
            {
                "videojuego_id": 1, // Ajustado para coincidir con BaseX
                "titulo": "The Legend of Zelda: Breath of the Wild",
                "cantidad": 1,
                "precio": 59.99
            },
            {
                "videojuego_id": 11, // Ajustado para coincidir con BaseX
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
                "videojuego_id": 2, // Ajustado para coincidir con BaseX
                "titulo": "God of War",
                "cantidad": 1,
                "precio": 69.99
            },
            {
                "videojuego_id": 7, // Ajustado para coincidir con BaseX
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
                "videojuego_id": 3, // Ajustado para coincidir con BaseX
                "titulo": "Cyberpunk 2077",
                "cantidad": 1,
                "precio": 49.99
            },
            {
                "videojuego_id": 9, // Ajustado para coincidir con BaseX
                "titulo": "Minecraft",
                "cantidad": 3,
                "precio": 29.99
            }
        ]
    },
    {
        "carrito_id": 4,
        "usuario_id": 4,
        "videojuegos": [
            {
                "videojuego_id": 4, // Ajustado para coincidir con BaseX
                "titulo": "EA Sports FC 25",
                "cantidad": 1,
                "precio": 69.99
            },
            {
                "videojuego_id": 5, // Ajustado para coincidir con BaseX
                "titulo": "Hollow Knight",
                "cantidad": 2,
                "precio": 14.99
            }
        ]
    },
    {
        "carrito_id": 5,
        "usuario_id": 5,
        "videojuegos": [
            {
                "videojuego_id": 6, // Ajustado para coincidir con BaseX
                "titulo": "Spider-Man 2",
                "cantidad": 4,
                "precio": 79.99
            },
            {
                "videojuego_id": 8, // Ajustado para coincidir con BaseX
                "titulo": "Elden Ring",
                "cantidad": 5,
                "precio": 59.99
            }
        ]
    }
]);

// Insertar los datos en la colección 'Compras'
db.Compras.insertMany([
    {
        "compra_id": 1,
        "usuario_id": 1,
        "videojuegos": [
            {
                "videojuego_id": 1, // Ajustado para coincidir con BaseX
                "titulo": "The Legend of Zelda: Breath of the Wild",
                "cantidad": 1,
                "precio": 59.99
            }
        ],
        "fecha_compra": new Date("2025-03-10T14:48:00.000Z"),
        "total": 59.99
    },
    {
        "compra_id": 2,
        "usuario_id": 2,
        "videojuegos": [
            {
                "videojuego_id": 2, // Ajustado para coincidir con BaseX
                "titulo": "God of War",
                "cantidad": 1,
                "precio": 69.99
            }
        ],
        "fecha_compra": new Date("2025-03-11T09:15:00.000Z"),
        "total": 69.99
    },
    {
        "compra_id": 3,
        "usuario_id": 3,
        "videojuegos": [
            {
                "videojuego_id": 3, // Ajustado para coincidir con BaseX
                "titulo": "Cyberpunk 2077",
                "cantidad": 1,
                "precio": 49.99
            },
            {
                "videojuego_id": 9, // Ajustado para coincidir con BaseX
                "titulo": "Minecraft",
                "cantidad": 1,
                "precio": 29.99
            }
        ],
        "fecha_compra": new Date("2025-03-12T16:30:00.000Z"),
        "total": 79.98
    },
    {
        "compra_id": 4,
        "usuario_id": 4,
        "videojuegos": [
            {
                "videojuego_id": 4, // Ajustado para coincidir con BaseX
                "titulo": "EA Sports FC 25",
                "cantidad": 1,
                "precio": 69.99
            }
        ],
        "fecha_compra": new Date("2025-03-13T10:00:00.000Z"),
        "total": 69.99
    },
    {
        "compra_id": 5,
        "usuario_id": 5,
        "videojuegos": [
            {
                "videojuego_id": 6, // Ajustado para coincidir con BaseX
                "titulo": "Spider-Man 2",
                "cantidad": 1,
                "precio": 79.99
            },
            {
                "videojuego_id": 8, // Ajustado para coincidir con BaseX
                "titulo": "Elden Ring",
                "cantidad": 1,
                "precio": 59.99
            }
        ],
        "fecha_compra": new Date("2025-03-14T11:20:00.000Z"),
        "total": 139.98
    }
]);