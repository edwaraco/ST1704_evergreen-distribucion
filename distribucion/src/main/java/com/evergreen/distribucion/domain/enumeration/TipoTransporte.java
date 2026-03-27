package com.evergreen.distribucion.domain.enumeration;

/**
 * Historia de Usuario 1: Gestión de Catálogos Base
 *
 * Como administrador del sistema de distribución
 * Quiero gestionar los catálogos fundamentales (clientes, productos, canales y transportes)
 * Para tener disponible la información base que usarán los pedidos
 *
 * Entidades:
 * - Cliente: Clientes que realizan pedidos
 * - Producto: Productos disponibles para distribución
 * - CanalComercializacion: Canales por los que se reciben pedidos
 * - Transporte: Vehículos disponibles para entrega
 *
 * Dependencias: Ninguna (historia base)
 */
public enum TipoTransporte {
    ACUATICO,
    AEREO,
    TERRESTRE,
}
