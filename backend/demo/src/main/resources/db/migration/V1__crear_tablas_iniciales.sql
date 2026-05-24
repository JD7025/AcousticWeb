CREATE TABLE app_users (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE parlantes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    tipo VARCHAR(80),
    potencia_watts NUMERIC(10,2),
    rango_frecuencia_min NUMERIC(10,2),
    rango_frecuencia_max NUMERIC(10,2),
    descripcion TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE microfonos (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    tipo VARCHAR(80),
    tiene_calibracion BOOLEAN NOT NULL DEFAULT FALSE,
    archivo_calibracion_url TEXT,
    descripcion TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE salas (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(80),
    ancho_m NUMERIC(10,2),
    largo_m NUMERIC(10,2),
    alto_m NUMERIC(10,2),
    material_paredes VARCHAR(120),
    material_piso VARCHAR(120),
    descripcion TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE mediciones (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    parlante_id BIGINT NOT NULL REFERENCES parlantes(id) ON DELETE CASCADE,
    microfono_id BIGINT NOT NULL REFERENCES microfonos(id) ON DELETE CASCADE,
    sala_id BIGINT NOT NULL REFERENCES salas(id) ON DELETE CASCADE,
    nombre VARCHAR(120) NOT NULL,
    nivel_spl NUMERIC(10,2),
    frecuencia_inicio NUMERIC(10,2) NOT NULL,
    frecuencia_fin NUMERIC(10,2) NOT NULL,
    ruido_ambiente_db NUMERIC(10,2),
    estado VARCHAR(30) NOT NULL,
    observaciones TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE datos_frecuencia (
    id BIGSERIAL PRIMARY KEY,
    medicion_id BIGINT NOT NULL REFERENCES mediciones(id) ON DELETE CASCADE,
    frecuencia_hz NUMERIC(12,4) NOT NULL,
    nivel_db NUMERIC(12,4) NOT NULL,
    fase_grados NUMERIC(12,4)
);

CREATE TABLE perfiles_ecualizacion (
    id BIGSERIAL PRIMARY KEY,
    medicion_id BIGINT NOT NULL REFERENCES mediciones(id) ON DELETE CASCADE,
    nombre VARCHAR(120) NOT NULL,
    preamp_db NUMERIC(10,2) NOT NULL,
    curva_objetivo VARCHAR(80) NOT NULL,
    descripcion TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE filtros_eq (
    id BIGSERIAL PRIMARY KEY,
    perfil_id BIGINT NOT NULL REFERENCES perfiles_ecualizacion(id) ON DELETE CASCADE,
    tipo VARCHAR(40) NOT NULL,
    frecuencia_hz NUMERIC(12,4) NOT NULL,
    ganancia_db NUMERIC(10,4) NOT NULL,
    q NUMERIC(10,4) NOT NULL,
    orden INTEGER NOT NULL
);