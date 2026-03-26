package com.evergreen.distribucion.domain.enumeration;

/**
 * Historia de Usuario 3: Gestión de Tareas Logísticas
 *
 * Como operador logístico
 * Quiero gestionar las tareas de empaque y separación asociadas a los pedidos
 * Para controlar el proceso de preparación de los productos para entrega
 *
 * Entidades nuevas:
 * - Empaque: Tareas de empaque de productos
 * - Separacion: Tareas de separación de productos por lotes
 *
 * Dependencias: HU-2 (Pedidos)
 *
 * NOTA: Este JDL solo contiene las entidades de logística y sus relaciones.
 * Las entidades de HU-1 y HU-2 ya deben existir en el proyecto.
 */
public enum TipoTransporte {
    ACUATICO,
    AEREO,
    TERRESTRE,
}
