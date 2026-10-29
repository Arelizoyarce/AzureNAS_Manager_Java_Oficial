-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 27-10-2026 a las 04:05:35
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `azure_nas_manager`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `auditoria`
--

CREATE TABLE `auditoria` (
  `id_log` bigint(20) NOT NULL,
  `modulo` varchar(50) DEFAULT NULL,
  `accion` varchar(50) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `ip_usuario` varchar(50) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL,
  `usuario_id` bigint(20) DEFAULT NULL,
  `hash_integridad` varchar(64) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `auditoria`
--

INSERT INTO `auditoria` (`id_log`, `modulo`, `accion`, `descripcion`, `ip_usuario`, `fecha`, `usuario_id`, `hash_integridad`) VALUES
(1, 'Explorador', 'ABRIR', 'Abrió: fs-seguridad-info', '127.0.0.1', '2026-10-19 01:41:51', NULL, NULL),
(2, 'Transferencias', 'SUBIR', 'Subió: 1222.pdf', '127.0.0.1', '2026-10-19 01:42:02', NULL, NULL),
(3, 'Explorador', 'ABRIR', 'Abrió: fs-seguridad-info', '127.0.0.1', '2026-10-19 01:42:07', NULL, NULL),
(4, 'Explorador', 'ABRIR', 'Abrió: fs-seguridad-info', '127.0.0.1', '2026-10-19 01:42:22', NULL, NULL),
(5, 'Explorador', 'ABRIR', 'Abrió: fs-seguridad-info', '127.0.0.1', '2026-10-19 01:42:38', NULL, NULL),
(6, 'Explorador', 'ABRIR', 'Abrió: fs-seguridad-info', '127.0.0.1', '2026-10-19 01:42:39', NULL, NULL),
(7, 'Admin', 'CREAR_USUARIO', 'Creó: alexander@pacifico.com.pe', '127.0.0.1', '2026-10-19 01:43:36', NULL, NULL),
(8, 'Explorador', 'ABRIR', 'Abrió: fs-seguridad-info', '127.0.0.1', '2026-10-19 01:44:38', NULL, NULL),
(9, 'Explorador', 'ABRIR', 'Abrió: fs-seguridad-info como admin@test.com', '127.0.0.1', '2026-10-19 01:55:04', NULL, NULL),
(10, 'Transferencias', 'SUBIR', 'Subió: 1222.docx como admin@test.com', '127.0.0.1', '2026-10-19 01:56:01', NULL, NULL),
(11, 'Explorador', 'ABRIR', 'Abrió: fs-seguridad-info como admin@test.com', '127.0.0.1', '2026-10-19 01:56:03', NULL, NULL),
(12, 'Explorador', 'ABRIR', 'Abrió: fs-seguridad-info como alexander@pacifico.com.pe', '127.0.0.1', '2026-10-19 01:56:56', NULL, NULL),
(13, 'Explorador', 'ABRIR', 'Abrió: fs-seguridad-info como alexander@pacifico.com.pe', '127.0.0.1', '2026-10-19 01:56:59', NULL, NULL),
(14, 'Transferencias', 'SUBIR', 'Subió: Reflexion_Inclusion_Educativa (1).docx como alexander@pacifico.com.pe', '127.0.0.1', '2026-10-19 01:57:10', NULL, NULL),
(15, 'Explorador', 'ABRIR', 'Abrió: fs-seguridad-info como alexander@pacifico.com.pe', '127.0.0.1', '2026-10-19 01:57:13', NULL, NULL),
(16, 'Explorador', 'DESCARGAR', 'Descargó: Reflexion_Inclusion_Educativa (1).docx como alexander@pacifico.com.pe', '127.0.0.1', '2026-10-19 01:57:15', NULL, NULL),
(17, 'Explorador', 'ABRIR', 'Abrió: fs-seguridad-info como alexander@pacifico.com.pe', '127.0.0.1', '2026-10-19 01:57:22', NULL, NULL),
(18, 'Explorador', 'ABRIR', 'Abrió: fs-seguridad-info como alexander@pacifico.com.pe', '127.0.0.1', '2026-10-19 02:16:30', NULL, NULL),
(19, 'Explorador', 'ABRIR', 'Abrió: fs-seguridad-info como admin@test.com', '127.0.0.1', '2026-10-25 17:27:26', NULL, NULL),
(20, 'Explorador', 'ABRIR', 'Abrió: fs-seguridad-info como alexander@pacifico.com.pe', '127.0.0.1', '2026-10-25 17:30:19', NULL, NULL),
(21, 'Transferencias', 'SUBIR', 'Subió: Informe_Nas_2026.xlsx', '127.0.0.1', '2026-10-25 17:46:37', 2, 'e8c1a8a77c7b37f29768d67e506aef211df86e9ab55ea1a9d1870b4178f4bec9'),
(22, 'Explorador', 'DESCARGAR', 'Descargó: Informe_Nas_2026.xlsx', '127.0.0.1', '2026-10-25 17:46:45', 2, '5b8eb23f58d2a862cfa9b95d5014f75ebfd5a9c875b0e231924f7bb3a686f549'),
(23, 'Búsqueda', 'FILTRAR', 'Buscó: informe', '127.0.0.1', '2026-10-25 17:47:28', 2, '55db898056ce0e89286dc8404fc6f1b8e93916c23371cc2ff36186be260034ec'),
(24, 'Explorador', 'TRANSFERIR', 'Transfirió Informe_Nas_2026.xlsx a admin@test.com', '127.0.0.1', '2026-10-25 18:00:58', 2, '15410b2bead1eed9f6c6850477f23e425c4d97a8478df16cf2b0781b66507015'),
(25, 'Explorador', 'VISTA_PREVIA', 'Vio: Informe_Nas_2026.xlsx', '127.0.0.1', '2026-10-25 18:34:54', 2, '1e97a3ccdfb1ee31c027361ce2a1b73f29d20ed918a2cdf7c9e1bf5a7cffefcf'),
(26, 'Admin', 'CREAR_USUARIO', 'Creó: juan@pacifico.com.pe', '127.0.0.1', '2026-10-25 19:25:15', NULL, NULL),
(27, 'Admin', 'CREAR_GRUPO', 'Creó grupo: trabajo', '127.0.0.1', '2026-10-25 19:26:51', NULL, NULL),
(28, 'Dashboard', 'CREAR_GRUPO', 'El usuario creó el grupo: reporte', '127.0.0.1', '2026-10-25 19:41:18', 3, 'aee30aded1fb049ec096151161ca2f01cc116041fe7417f1c3f3564c25db8132'),
(29, 'Transferencias', 'SUBIR', 'Subió: Dashboard_PowerBI_Migracion_NAS.png', '127.0.0.1', '2026-10-25 19:42:21', 2, 'a6bddb8469b017c98701e67a0298deabcd2b6a34dd5aae84d333b3aeb9e1ab18'),
(30, 'Grupo', 'SUBIR', 'Subió archivo al grupo ID 2', '127.0.0.1', '2026-10-25 19:46:31', 3, '65777ab295ebc890dd4343fa13ed60ffbb5608df3d408b855436711f7d26f2c0'),
(31, 'Grupo', 'VISTA_PREVIA', 'Accedió a pronu....docx', '127.0.0.1', '2026-10-25 19:46:36', 3, 'd3bb20c2cf1ae5c15be0b32fce64c92b11f5bc7e1d88d56e02cc222e694f4319'),
(32, 'Dashboard', 'CREAR_GRUPO', 'El usuario creó el grupo: informes mensual', '127.0.0.1', '2026-10-25 19:57:12', 2, '7e71ddcfbcd0ed30de50b1ebada53b47e981654186ac06f4ce26a691b83350bd'),
(33, 'Explorador', 'VISTA_PREVIA', 'Vio: 1222.docx', '127.0.0.1', '2026-10-27 02:49:28', 1, '76eeb0e7e04dbcf72da0fb4259f881ab91b4809ed7869edd7b9a5c9b50128089'),
(34, 'Búsqueda', 'FILTRAR', 'Buscó: 122', '127.0.0.1', '2026-10-27 02:49:54', 1, '34428c6da9307648bdc39238f85b43367155f7f3daba9d02c0db9e9b621c6b00'),
(35, 'Grupo', 'VISTA_PREVIA', 'Accedió a pronu....docx', '127.0.0.1', '2026-10-27 02:52:38', 2, '326e301d983d1b0093bd987e671b7701a6000b2b4bbd5d2607f93b41e6974be0'),
(36, 'Búsqueda', 'FILTRAR', 'Buscó: Nas_2026.xlsx', '127.0.0.1', '2026-10-27 02:53:14', 2, '5bc6f836ae7531a32863df3b70e4f887633e04507c9187e60079b9812d854cae'),
(37, 'Transferencias', 'SUBIR', 'Subió: Avance_de_Proyecto_Final_2_GRUPO_2.docx', '127.0.0.1', '2026-10-27 02:53:35', 2, 'bc5d439ee97642bc6361de9724316245788ec8e3fbed7ebcbb2885de0e6fdfbd'),
(38, 'Dashboard', 'CREAR_GRUPO', 'El usuario creó el grupo: presentacion final 3', '127.0.0.1', '2026-10-27 02:54:04', 2, '3d3ae15b6143beb573177696f0aeb09179cba2c603f72790b24c1f9419ea343c'),
(39, 'Grupo', 'SUBIR', 'Subió archivo al grupo ID 4', '127.0.0.1', '2026-10-27 02:54:57', 2, '7c8eabd0981b19be3a40a4d2942f026a691dae6d0c7e6f5f3547739b0c5b58a2'),
(40, 'Grupo', 'VISTA_PREVIA', 'Accedió a (AC-S04-APF1) Avance de Proyecto Final 1_ GRUPO 2.docx', '127.0.0.1', '2026-10-27 02:55:09', 3, 'c994f3d9fe7d520ab85ce6217b5d8346ffb03102a2ff0ae3aea3ac95d41d4593');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grupo`
--

CREATE TABLE `grupo` (
  `id_grupo` bigint(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `creador_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `grupo`
--

INSERT INTO `grupo` (`id_grupo`, `nombre`, `descripcion`, `creador_id`) VALUES
(1, 'trabajo', 'Grupo de colaboración', NULL),
(2, 'reporte', 'Grupo creado por juan@pacifico.com.pe', 3),
(3, 'informes mensual', 'Grupo creado por alexander@pacifico.com.pe', 2),
(4, 'presentacion final 3', 'Grupo creado por alexander@pacifico.com.pe', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grupo_usuario`
--

CREATE TABLE `grupo_usuario` (
  `grupo_id` bigint(20) NOT NULL,
  `usuario_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `grupo_usuario`
--

INSERT INTO `grupo_usuario` (`grupo_id`, `usuario_id`) VALUES
(1, 2),
(2, 2),
(2, 3),
(3, 2),
(3, 3),
(4, 2),
(4, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recurso`
--

CREATE TABLE `recurso` (
  `id_recurso` bigint(20) NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `tipo` varchar(50) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `fecha_registro` datetime DEFAULT NULL,
  `estado` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `recurso`
--

INSERT INTO `recurso` (`id_recurso`, `nombre`, `tipo`, `descripcion`, `fecha_registro`, `estado`) VALUES
(1, 'fs-seguridad-info', 'FILE_SHARE', 'Información colaborativa', '2026-10-18 20:36:00', 'ACTIVO'),
(2, 'fs-riesgos', 'FILE_SHARE', 'Documentos de riesgos', '2026-10-18 20:36:00', 'ACTIVO'),
(3, 'fs-telefonia', 'FILE_SHARE', 'Telefonía', '2026-10-18 20:36:00', 'ACTIVO'),
(4, 'ct-historico-2015', 'CONTAINER', 'Información histórica', '2026-10-18 20:36:00', 'ACTIVO'),
(5, 'ct-pricing-legacy', 'CONTAINER', 'Pricing', '2026-10-18 20:36:00', 'ACTIVO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

CREATE TABLE `rol` (
  `id_rol` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(150) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `rol`
--

INSERT INTO `rol` (`id_rol`, `nombre`, `descripcion`) VALUES
(1, 'ADMINISTRADOR', NULL),
(2, 'USUARIO', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id_usuario` bigint(20) NOT NULL,
  `nombres` varchar(100) DEFAULT NULL,
  `apellidos` varchar(100) DEFAULT NULL,
  `correo` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `fecha_creacion` datetime DEFAULT NULL,
  `estado` tinyint(1) DEFAULT 1,
  `id_rol` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `nombres`, `apellidos`, `correo`, `password_hash`, `fecha_creacion`, `estado`, `id_rol`) VALUES
(1, 'Admin', 'Sistema', 'admin@test.com', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', '2026-10-18 20:36:00', 1, 1),
(2, 'Alexander', 'Apellido', 'alexander@pacifico.com.pe', '$2a$10$lfcXczBDBPoEP1aPOrMln.MRfMCYYGAOhkRkAvffajZy2JV1zGEqS', '2026-10-19 01:43:36', 1, 2),
(3, 'juan', 'Apellido', 'juan@pacifico.com.pe', '$2a$10$ufNhc2nPnnyejLI2fX5IhOsE4/pvPYZ3bCkpgvdFk.SToy0VHdv0e', '2026-10-25 19:25:15', 1, 2);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `auditoria`
--
ALTER TABLE `auditoria`
  ADD PRIMARY KEY (`id_log`);

--
-- Indices de la tabla `grupo`
--
ALTER TABLE `grupo`
  ADD PRIMARY KEY (`id_grupo`),
  ADD UNIQUE KEY `nombre` (`nombre`),
  ADD KEY `fk_grupo_creador` (`creador_id`);

--
-- Indices de la tabla `grupo_usuario`
--
ALTER TABLE `grupo_usuario`
  ADD PRIMARY KEY (`grupo_id`,`usuario_id`),
  ADD KEY `usuario_id` (`usuario_id`);

--
-- Indices de la tabla `recurso`
--
ALTER TABLE `recurso`
  ADD PRIMARY KEY (`id_recurso`);

--
-- Indices de la tabla `rol`
--
ALTER TABLE `rol`
  ADD PRIMARY KEY (`id_rol`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `correo` (`correo`),
  ADD KEY `id_rol` (`id_rol`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `auditoria`
--
ALTER TABLE `auditoria`
  MODIFY `id_log` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT de la tabla `grupo`
--
ALTER TABLE `grupo`
  MODIFY `id_grupo` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `recurso`
--
ALTER TABLE `recurso`
  MODIFY `id_recurso` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `rol`
--
ALTER TABLE `rol`
  MODIFY `id_rol` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `grupo`
--
ALTER TABLE `grupo`
  ADD CONSTRAINT `fk_grupo_creador` FOREIGN KEY (`creador_id`) REFERENCES `usuario` (`id_usuario`);

--
-- Filtros para la tabla `grupo_usuario`
--
ALTER TABLE `grupo_usuario`
  ADD CONSTRAINT `grupo_usuario_ibfk_1` FOREIGN KEY (`grupo_id`) REFERENCES `grupo` (`id_grupo`),
  ADD CONSTRAINT `grupo_usuario_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id_usuario`);

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
