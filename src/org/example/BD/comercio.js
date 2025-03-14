// Insertar los datos en la colección 'Usuarios'
db.Usuarios.insertMany([
    {
        "usuario_id": 1,
        "nombre": "Juan Pérez",
        "correo": "juan.perez@example.com",
        "contrasena": "hashed_password1",
        "direccion": "Calle Falsa 123, Ciudad, País",
        "telefono": "123-456-7890"
    },
    {
        "usuario_id": 2,
        "nombre": "María García",
        "correo": "maria.garcia@example.com",
        "contrasena": "hashed_password2",
        "direccion": "Avenida Siempre Viva 742, Ciudad, País",
        "telefono": "098-765-4321"
    },
    {
        "usuario_id": 3,
        "nombre": "Carlos López",
        "correo": "carlos.lopez@example.com",
        "contrasena": "hashed_password3",
        "direccion": "Plaza Mayor 1, Ciudad, País",
        "telefono": "111-222-3333"
    },
    {
        "usuario_id": 4,
        "nombre": "Ana Fernández",
        "correo": "ana.fernandez@example.com",
        "contrasena": "hashed_password4",
        "direccion": "Calle del Pez 23, Ciudad, País",
        "telefono": "444-555-6666"
    },
    {
        "usuario_id": 5,
        "nombre": "Luis Martínez",
        "correo": "luis.martinez@example.com",
        "contrasena": "hashed_password5",
        "direccion": "Gran Vía 100, Ciudad, País",
        "telefono": "777-888-9999"
    }
]);

// Insertar los datos en la colección 'Carrito'
db.Carrito.insertMany([
    {
        "carrito_id": 1,
        "usuario_id": 1,
        "videojuegos": [
            {
                "videojuego_id": 101,
                "titulo": "The Legend of Zelda: Breath of the Wild",
                "cantidad": 1,
                "precio": 59.99
            },
            {
                "videojuego_id": 102,
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
                "videojuego_id": 103,
                "titulo": "God of War",
                "cantidad": 1,
                "precio": 69.99
            },
            {
                "videojuego_id": 104,
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
                "videojuego_id": 105,
                "titulo": "Cyberpunk 2077",
                "cantidad": 1,
                "precio": 49.99
            },
            {
                "videojuego_id": 106,
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
                "videojuego_id": 107,
                "titulo": "The Witcher 3: Wild Hunt",
                "cantidad": 1,
                "precio": 39.99
            },
            {
                "videojuego_id": 108,
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
                "videojuego_id": 109,
                "titulo": "Minecraft",
                "cantidad": 4,
                "precio": 29.99
            },
            {
                "videojuego_id": 110,
                "titulo": "Among Us",
                "cantidad": 5,
                "precio": 5.99
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
                "videojuego_id": 101,
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
                "videojuego_id": 103,
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
                "videojuego_id": 105,
                "titulo": "Cyberpunk 2077",
                "cantidad": 1,
                "precio": 49.99
            },
            {
                "videojuego_id": 106,
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
                "videojuego_id": 107,
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
                "videojuego_id": 109,
                "titulo": "Minecraft",
                "cantidad": 1,
                "precio": 29.99
            },
            {
                "videojuego_id": 110,
                "titulo": "Among Us",
                "cantidad": 1,
                "precio": 5.99
            }
        ],
        "fecha_compra": "2025-03-14T11:20:00.000Z",
        "total": 35.98
    }
]);