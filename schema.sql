-- Crear la base de dades
-- Connectar-se a la base de dades
-- Eliminar la taula si ja existeix
-- Crear la taula "users" amb només la columna "nom"
-- Inserir usuaris amb noms catalans
-- Donar permissos a usuari usuaris_admin per la taula
CREATE DATABASE usuaris;

\c usuaris

DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

INSERT INTO users (email, full_name, password) VALUES
('anna@example.cat', 'Anna Puig', 'clau123'),
('joan@example.cat', 'Joan Garcia', 'segur456'),
('marta@example.cat', 'Marta Valls', 'contrase789');

GRANT ALL PRIVILEGES ON TABLE users TO usuaris_admin;
GRANT ALL PRIVILEGES ON DATABASE usuaris TO usuaris_admin;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO usuaris_admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO usuaris_admin;
