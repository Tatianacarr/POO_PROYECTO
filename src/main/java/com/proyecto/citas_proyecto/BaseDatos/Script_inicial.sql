CREATE TABLE usuario (
                         id SERIAL PRIMARY KEY,
                         nombre VARCHAR(50) NOT NULL,
                         apellido VARCHAR(50) NOT NULL,
                         correo VARCHAR(100) UNIQUE NOT NULL,
                         contrasena VARCHAR(255) NOT NULL
);
SELECT *FROM usuario;

CREATE TABLE citas (
                       id SERIAL PRIMARY KEY,

                       especialidad VARCHAR(50) NOT NULL,
                       medico VARCHAR(80) NOT NULL,

                       fecha DATE NOT NULL,
                       hora TIME NOT NULL,

                       estado VARCHAR(20) DEFAULT 'Pendiente',

                       descripcion TEXT,

                       usuario_id INT NOT NULL,

                       CONSTRAINT fk_usuario
                           FOREIGN KEY (usuario_id)
                               REFERENCES usuario(id)
                               ON DELETE CASCADE
);
SELECT *FROM citas;