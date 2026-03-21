# language: es
Característica: Gestión de pedidos
  Como operador del sistema de distribución
  Quiero gestionar pedidos asociando clientes, productos, canales y transportes
  Para coordinar el flujo de distribución de productos

  Antecedentes:
    Dado que el usuario está autenticado en el sistema
    Y se encuentra en la página principal
    Y existen los siguientes clientes en el sistema:
      | identificador | nombre                       |
      | CLI-001       | Distribuidora El Pino S.A.   |
      | CLI-002       | Comercial Norte LTDA         |
    Y existen los siguientes productos en el sistema:
      | identificador | nombre              | cantidad |
      | PROD-001      | Leche entera 1L     | 1000     |
      | PROD-002      | Yogurt natural      | 500      |
      | PROD-003      | Queso fresco        | 300      |
    Y existen los siguientes canales de comercialización:
      | identificador | nombre           | activo |
      | CANAL-001     | Tienda Online    | Sí     |
      | CANAL-002     | Teléfono         | Sí     |
    Y existen los siguientes transportes:
      | identificador | tipoTransporte | estado     |
      | TRANS-001     | TERRESTRE      | Disponible |
      | TRANS-002     | AEREO          | Disponible |

  # ========== CREACIÓN DE PEDIDOS ==========

  Escenario: Crear un pedido básico con un solo producto
    Dado que el usuario navega a la sección "Pedidos"
    Y hace clic en el botón "Crear nuevo pedido"
    Cuando el usuario completa el formulario con:
      | campo                  | valor                      |
      | identificador          | PED-001                    |
      | fechaEntrada           | 2026-03-20                 |
      | estado                 | Pendiente                  |
      | cliente                | Distribuidora El Pino S.A. |
      | canalComercializacion  | Tienda Online              |
    Y selecciona el producto "Leche entera 1L"
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Pedido creado exitosamente"
    Y el pedido "PED-001" aparece en el listado con estado "Pendiente"
    Y el pedido incluye 1 producto

  Escenario: Crear un pedido con múltiples productos
    Dado que el usuario navega a la sección "Pedidos"
    Y hace clic en el botón "Crear nuevo pedido"
    Cuando el usuario completa el formulario con:
      | campo                  | valor                      |
      | identificador          | PED-002                    |
      | fechaEntrada           | 2026-03-21                 |
      | estado                 | Pendiente                  |
      | cliente                | Comercial Norte LTDA       |
      | canalComercializacion  | Teléfono                   |
    Y selecciona los productos:
      | producto         |
      | Leche entera 1L  |
      | Yogurt natural   |
      | Queso fresco     |
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Pedido creado exitosamente"
    Y el pedido "PED-002" aparece en el listado
    Y el pedido incluye 3 productos

  Escenario: Crear un pedido con transporte asignado
    Dado que el usuario navega a la sección "Pedidos"
    Y hace clic en el botón "Crear nuevo pedido"
    Cuando el usuario completa el formulario con:
      | campo                  | valor                      |
      | identificador          | PED-003                    |
      | fechaEntrada           | 2026-03-21                 |
      | estado                 | En proceso                 |
      | cliente                | Distribuidora El Pino S.A. |
      | canalComercializacion  | Tienda Online              |
      | transporte             | TRANS-001                  |
    Y selecciona el producto "Leche entera 1L"
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Pedido creado exitosamente"
    Y el pedido "PED-003" aparece en el listado
    Y el pedido muestra el transporte "TRANS-001" asignado

  # ========== EDICIÓN DE PEDIDOS ==========

  Escenario: Agregar un producto a un pedido existente
    Dado que existe un pedido "PED-001" con 1 producto
    Y el usuario navega a la sección "Pedidos"
    Cuando el usuario selecciona editar el pedido "PED-001"
    Y agrega el producto "Yogurt natural"
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Pedido actualizado exitosamente"
    Y el pedido "PED-001" incluye 2 productos
    Y los productos incluyen "Leche entera 1L" y "Yogurt natural"

  Escenario: Asignar transporte a un pedido sin transporte
    Dado que existe un pedido "PED-001" sin transporte asignado
    Y el usuario navega a la sección "Pedidos"
    Cuando el usuario selecciona editar el pedido "PED-001"
    Y selecciona el transporte "TRANS-002"
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Pedido actualizado exitosamente"
    Y el pedido "PED-001" muestra el transporte "TRANS-002" asignado

  Escenario: Cambiar el estado de un pedido a "Despachado"
    Dado que existe un pedido "PED-001" con estado "Pendiente"
    Y el usuario navega a la sección "Pedidos"
    Cuando el usuario selecciona editar el pedido "PED-001"
    Y cambia el estado a "Despachado"
    Y establece la fecha de salida a "2026-03-22"
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Pedido actualizado exitosamente"
    Y el pedido "PED-001" aparece con estado "Despachado"
    Y el pedido muestra la fecha de salida "2026-03-22"

  # ========== CONSULTA Y FILTRADO DE PEDIDOS ==========

  Escenario: Listar todos los pedidos ordenados por fecha de entrada
    Dado que existen los siguientes pedidos:
      | identificador | fechaEntrada | estado     |
      | PED-001       | 2026-03-20   | Pendiente  |
      | PED-002       | 2026-03-21   | En proceso |
      | PED-003       | 2026-03-19   | Despachado |
    Y el usuario navega a la sección "Pedidos"
    Cuando el usuario ordena por "fechaEntrada" descendente
    Entonces los pedidos se muestran en el orden:
      | identificador |
      | PED-002       |
      | PED-001       |
      | PED-003       |

  Escenario: Filtrar pedidos por cliente
    Dado que existen los siguientes pedidos:
      | identificador | cliente                      | estado     |
      | PED-001       | Distribuidora El Pino S.A.   | Pendiente  |
      | PED-002       | Comercial Norte LTDA         | En proceso |
      | PED-003       | Distribuidora El Pino S.A.   | Despachado |
    Y el usuario navega a la sección "Pedidos"
    Cuando el usuario filtra por cliente "Distribuidora El Pino S.A."
    Entonces se muestran 2 pedidos en los resultados
    Y los resultados incluyen "PED-001"
    Y los resultados incluyen "PED-003"

  Escenario: Filtrar pedidos por estado "Pendiente"
    Dado que existen los siguientes pedidos:
      | identificador | estado     |
      | PED-001       | Pendiente  |
      | PED-002       | En proceso |
      | PED-003       | Despachado |
      | PED-004       | Pendiente  |
    Y el usuario navega a la sección "Pedidos"
    Cuando el usuario filtra por estado "Pendiente"
    Entonces se muestran 2 pedidos en los resultados
    Y los resultados incluyen "PED-001"
    Y los resultados incluyen "PED-004"

  Escenario: Buscar pedidos por rango de fechas
    Dado que existen los siguientes pedidos:
      | identificador | fechaEntrada |
      | PED-001       | 2026-03-15   |
      | PED-002       | 2026-03-20   |
      | PED-003       | 2026-03-22   |
      | PED-004       | 2026-03-25   |
    Y el usuario navega a la sección "Pedidos"
    Cuando el usuario filtra por rango de fechas desde "2026-03-20" hasta "2026-03-22"
    Entonces se muestran 2 pedidos en los resultados
    Y los resultados incluyen "PED-002"
    Y los resultados incluyen "PED-003"

  # ========== VISUALIZACIÓN DE DETALLES ==========

  Escenario: Ver detalles completos de un pedido
    Dado que existe un pedido "PED-001" con:
      | campo                  | valor                      |
      | identificador          | PED-001                    |
      | fechaEntrada           | 2026-03-20                 |
      | fechaSalida            | 2026-03-22                 |
      | estado                 | Despachado                 |
      | cliente                | Distribuidora El Pino S.A. |
      | canalComercializacion  | Tienda Online              |
      | transporte             | TRANS-001                  |
    Y el pedido incluye los productos "Leche entera 1L" y "Yogurt natural"
    Y el usuario navega a la sección "Pedidos"
    Cuando el usuario hace clic en "Ver detalles" del pedido "PED-001"
    Entonces se muestra la página de detalles del pedido
    Y se visualizan todos los campos correctamente:
      | campo                  | valor                      |
      | Identificador          | PED-001                    |
      | Fecha de Entrada       | 2026-03-20                 |
      | Fecha de Salida        | 2026-03-22                 |
      | Estado                 | Despachado                 |
      | Cliente                | Distribuidora El Pino S.A. |
      | Canal                  | Tienda Online              |
      | Transporte             | TRANS-001                  |
    Y se muestra el listado de productos con 2 items

  # ========== VALIDACIONES ==========

  Escenario: Intentar crear un pedido sin cliente
    Dado que el usuario navega a la sección "Pedidos"
    Y hace clic en el botón "Crear nuevo pedido"
    Cuando el usuario completa el formulario con:
      | campo                  | valor         |
      | identificador          | PED-005       |
      | fechaEntrada           | 2026-03-20    |
      | estado                 | Pendiente     |
      | canalComercializacion  | Tienda Online |
    Y deja el campo "cliente" vacío
    Y hace clic en "Guardar"
    Entonces se muestra un mensaje de validación indicando que el cliente es obligatorio
    Y el formulario permanece abierto

  Escenario: Intentar crear un pedido con identificador duplicado
    Dado que existe un pedido con identificador "PED-001"
    Y el usuario navega a la sección "Pedidos"
    Y hace clic en el botón "Crear nuevo pedido"
    Cuando el usuario completa el formulario con:
      | campo                  | valor                      |
      | identificador          | PED-001                    |
      | fechaEntrada           | 2026-03-21                 |
      | estado                 | Pendiente                  |
      | cliente                | Distribuidora El Pino S.A. |
    Y hace clic en "Guardar"
    Entonces se muestra un mensaje de error indicando que el identificador ya existe
    Y el formulario permanece abierto

  Escenario: Intentar establecer fecha de salida anterior a fecha de entrada
    Dado que el usuario navega a la sección "Pedidos"
    Y hace clic en el botón "Crear nuevo pedido"
    Cuando el usuario completa el formulario con:
      | campo                  | valor                      |
      | identificador          | PED-006                    |
      | fechaEntrada           | 2026-03-25                 |
      | fechaSalida            | 2026-03-20                 |
      | estado                 | Pendiente                  |
      | cliente                | Distribuidora El Pino S.A. |
      | canalComercializacion  | Tienda Online              |
    Y hace clic en "Guardar"
    Entonces se muestra un mensaje de validación indicando que la fecha de salida no puede ser anterior a la fecha de entrada

  # ========== ELIMINACIÓN DE PEDIDOS ==========

  Escenario: Eliminar un pedido sin tareas asociadas
    Dado que existe un pedido "PED-007" sin tareas de empaque ni separación
    Y el usuario navega a la sección "Pedidos"
    Cuando el usuario selecciona eliminar el pedido "PED-007"
    Y confirma la eliminación
    Entonces se muestra el mensaje "Pedido eliminado exitosamente"
    Y el pedido "PED-007" no aparece en el listado

  # ========== PAGINACIÓN ==========

  Escenario: Navegar por múltiples páginas de pedidos
    Dado que existen 25 pedidos en el sistema
    Y el usuario navega a la sección "Pedidos"
    Y la paginación está configurada para mostrar 10 pedidos por página
    Cuando el usuario se encuentra en la página 1
    Entonces se muestran 10 pedidos
    Cuando el usuario hace clic en "Página siguiente"
    Entonces se muestran 10 pedidos diferentes
    Cuando el usuario hace clic en "Página siguiente"
    Entonces se muestran 5 pedidos

