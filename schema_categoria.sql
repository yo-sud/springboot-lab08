-- Script para agregar tabla categoria a la base de datos laboratorio_api
-- Ejecutar en MySQL (XAMPP) o SQLyog conectado a localhost:3306

USE `laboratorio_api`;

-- Tabla categoria
CREATE TABLE IF NOT EXISTS `categoria` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(100) NOT NULL,
    `descripcion` VARCHAR(255) NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_categoria_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Datos iniciales: 3 categorías de ejemplo
INSERT INTO `categoria` (`nombre`, `descripcion`) VALUES
('Electrónica', 'Productos electrónicos y tecnológicos'),
('Hogar', 'Artículos para el hogar y decoración'),
('Oficina', 'Suministros y mobiliario de oficina');

-- Mejora opcional para normalización (comentada - el lab pide solo CRUD de Categoria independiente)
-- ALTER TABLE `producto` ADD COLUMN `categoria_id` BIGINT NULL AFTER `categoria`;
-- ALTER TABLE `producto` ADD CONSTRAINT `fk_producto_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categoria`(`id`) ON DELETE SET NULL ON UPDATE CASCADE;
-- CREATE INDEX `idx_producto_categoria_id` ON `producto`(`categoria_id`);