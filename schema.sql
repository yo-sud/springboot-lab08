-- Script de creación de base de datos y tabla para el laboratorio
-- Compatible con MySQL/XAMPP y SQLyog

CREATE DATABASE IF NOT EXISTS `laboratorio_api`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE `laboratorio_api`;

CREATE TABLE IF NOT EXISTS `producto` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `precio` DECIMAL(10,2) NOT NULL,
  `stock` INT NOT NULL DEFAULT 0,
  `categoria` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Datos iniciales de ejemplo (3 productos)
INSERT INTO `producto` (`nombre`, `precio`, `stock`, `categoria`) VALUES
('Laptop Gaming ASUS ROG', 1299.99, 15, 'Electrónica'),
('Mouse Inalámbrico Logitech MX Master 3', 99.50, 50, 'Accesorios'),
('Monitor 27" 4K Dell UltraSharp', 449.00, 8, 'Monitores');