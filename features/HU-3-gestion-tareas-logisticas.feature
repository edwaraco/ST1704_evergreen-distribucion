# language: es
Característica: Gestión de tareas logísticas
  Como operador logístico
  Quiero gestionar las tareas de empaque y separación asociadas a los pedidos
  Para controlar el proceso de preparación de los productos para entrega

  Antecedentes:
    Dado que el usuario está autenticado en el sistema
    Y se encuentra en la página principal
    Y existen los siguientes pedidos en el sistema:
      | identificador | cliente                      | estado     |
      | PED-001       | Distribuidora El Pino S.A.   | En proceso |
      | PED-002       | Comercial Norte LTDA         | Pendiente  |
      | PED-003       | Distribuidora El Pino S.A.   | En proceso |

  # ========== GESTIÓN DE TAREAS DE EMPAQUE ==========

  Escenario: Crear una tarea de empaque para un pedido
    Dado que el usuario navega a la sección "Tareas de Empaque"
    Y hace clic en el botón "Crear nueva tarea de empaque"
    Cuando el usuario completa el formulario con:
      | campo            | valor                    |
      | identificador    | EMP-001                  |
      | pedido           | PED-001                  |
      | tipo             | CAJA                     |
      | tamanio          | MEDIANO                  |
      | cantidad         | 10                       |
      | tiempoMinutos    | 45                       |
      | fechaRealizacion | 2026-03-21               |
      | responsable      | Juan Pérez               |
      | observaciones    | Empacar con cuidado      |
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Tarea de empaque creada exitosamente"
    Y la tarea "EMP-001" aparece en el listado
    Y la tarea está asociada al pedido "PED-001"

  Escenario: Crear una tarea de empaque tipo PALLET de gran tamaño
    Dado que el usuario navega a la sección "Tareas de Empaque"
    Y hace clic en el botón "Crear nueva tarea de empaque"
    Cuando el usuario completa el formulario con:
      | campo            | valor                    |
      | identificador    | EMP-002                  |
      | pedido           | PED-002                  |
      | tipo             | PALLET                   |
      | tamanio          | EXTRA_GRANDE             |
      | cantidad         | 5                        |
      | tiempoMinutos    | 120                      |
      | responsable      | María González           |
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Tarea de empaque creada exitosamente"
    Y la tarea "EMP-002" muestra tipo "PALLET" y tamaño "EXTRA_GRANDE"

  Escenario: Editar la cantidad de una tarea de empaque
    Dado que existe una tarea de empaque "EMP-001" con cantidad 10
    Y el usuario navega a la sección "Tareas de Empaque"
    Cuando el usuario selecciona editar la tarea "EMP-001"
    Y actualiza el campo "cantidad" a 15
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Tarea de empaque actualizada exitosamente"
    Y la tarea "EMP-001" muestra cantidad 15

  Escenario: Listar todas las tareas de empaque de un pedido específico
    Dado que existen las siguientes tareas de empaque:
      | identificador | pedido  | tipo        | cantidad |
      | EMP-001       | PED-001 | CAJA        | 10       |
      | EMP-002       | PED-002 | PALLET      | 5        |
      | EMP-003       | PED-001 | CONTENEDOR  | 2        |
    Y el usuario navega a la sección "Tareas de Empaque"
    Cuando el usuario filtra por pedido "PED-001"
    Entonces se muestran 2 tareas en los resultados
    Y los resultados incluyen "EMP-001"
    Y los resultados incluyen "EMP-003"

  Escenario: Calcular tiempo total de empaque por pedido
    Dado que existen las siguientes tareas de empaque para el pedido "PED-001":
      | identificador | tiempoMinutos |
      | EMP-001       | 45            |
      | EMP-003       | 60            |
      | EMP-004       | 30            |
    Y el usuario navega al detalle del pedido "PED-001"
    Cuando el usuario visualiza la sección de tareas de empaque
    Entonces se muestra el tiempo total de empaque: 135 minutos

  # ========== GESTIÓN DE TAREAS DE SEPARACIÓN ==========

  Escenario: Crear una tarea de separación para un pedido
    Dado que el usuario navega a la sección "Tareas de Separación"
    Y hace clic en el botón "Crear nueva tarea de separación"
    Cuando el usuario completa el formulario con:
      | campo            | valor                         |
      | identificador    | SEP-001                       |
      | pedido           | PED-001                       |
      | lote             | L20260315-A                   |
      | cantidad         | 500                           |
      | fechaRealizacion | 2026-03-21                    |
      | ubicacion        | Bodega A - Estante 12         |
      | responsable      | Carlos Ramírez                |
      | observaciones    | Separar productos del lote A  |
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Tarea de separación creada exitosamente"
    Y la tarea "SEP-001" aparece en el listado
    Y la tarea está asociada al pedido "PED-001"

  Escenario: Crear múltiples tareas de separación para diferentes lotes del mismo pedido
    Dado que el usuario navega a la sección "Tareas de Separación"
    Cuando el usuario crea la tarea de separación:
      | identificador | pedido  | lote         | cantidad | ubicacion            |
      | SEP-001       | PED-002 | L20260315-A  | 200      | Bodega A - Estante 5 |
    Y crea otra tarea de separación:
      | identificador | pedido  | lote         | cantidad | ubicacion            |
      | SEP-002       | PED-002 | L20260316-B  | 300      | Bodega B - Estante 8 |
    Entonces ambas tareas se crean exitosamente
    Y están asociadas al pedido "PED-002"
    Y cada tarea mantiene su lote distintivo

  Escenario: Editar la ubicación de una tarea de separación
    Dado que existe una tarea de separación "SEP-001" con ubicación "Bodega A - Estante 12"
    Y el usuario navega a la sección "Tareas de Separación"
    Cuando el usuario selecciona editar la tarea "SEP-001"
    Y actualiza el campo "ubicacion" a "Bodega C - Estante 3"
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Tarea de separación actualizada exitosamente"
    Y la tarea "SEP-001" muestra la nueva ubicación "Bodega C - Estante 3"

  Escenario: Listar todas las tareas de separación de un pedido
    Dado que existen las siguientes tareas de separación:
      | identificador | pedido  | lote         | cantidad |
      | SEP-001       | PED-001 | L20260315-A  | 500      |
      | SEP-002       | PED-002 | L20260316-B  | 300      |
      | SEP-003       | PED-001 | L20260317-C  | 200      |
    Y el usuario navega a la sección "Tareas de Separación"
    Cuando el usuario filtra por pedido "PED-001"
    Entonces se muestran 2 tareas en los resultados
    Y los resultados incluyen "SEP-001"
    Y los resultados incluyen "SEP-003"

  Escenario: Buscar tareas de separación por lote
    Dado que existen las siguientes tareas de separación:
      | identificador | lote         | pedido  |
      | SEP-001       | L20260315-A  | PED-001 |
      | SEP-002       | L20260316-B  | PED-002 |
      | SEP-003       | L20260315-A  | PED-003 |
    Y el usuario navega a la sección "Tareas de Separación"
    Cuando el usuario busca por lote "L20260315-A"
    Entonces se muestran 2 tareas en los resultados
    Y los resultados incluyen "SEP-001"
    Y los resultados incluyen "SEP-003"

  # ========== VISUALIZACIÓN INTEGRADA DE TAREAS EN PEDIDOS ==========

  Escenario: Ver todas las tareas logísticas de un pedido desde su detalle
    Dado que existe un pedido "PED-001" con:
      | Tareas de empaque    | EMP-001, EMP-003 |
      | Tareas de separación | SEP-001, SEP-003 |
    Y el usuario navega al detalle del pedido "PED-001"
    Cuando el usuario visualiza la sección de tareas logísticas
    Entonces se muestran 2 tareas de empaque
    Y se muestran 2 tareas de separación
    Y se puede acceder a los detalles de cada tarea

  Escenario: Verificar que un pedido tiene todas sus tareas completadas
    Dado que existe un pedido "PED-001" con estado "En proceso"
    Y el pedido tiene las siguientes tareas de empaque:
      | identificador | fechaRealizacion |
      | EMP-001       | 2026-03-21       |
      | EMP-003       | 2026-03-21       |
    Y el pedido tiene las siguientes tareas de separación:
      | identificador | fechaRealizacion |
      | SEP-001       | 2026-03-20       |
    Y el usuario navega al detalle del pedido "PED-001"
    Cuando el usuario verifica el estado de las tareas
    Entonces se muestra que todas las tareas tienen fecha de realización
    Y se indica que el pedido está listo para despacho

  # ========== VALIDACIONES ==========

  Escenario: Intentar crear una tarea de empaque sin pedido
    Dado que el usuario navega a la sección "Tareas de Empaque"
    Y hace clic en el botón "Crear nueva tarea de empaque"
    Cuando el usuario completa el formulario con:
      | campo            | valor     |
      | identificador    | EMP-004   |
      | tipo             | CAJA      |
      | tamanio          | PEQUENO   |
      | cantidad         | 5         |
      | tiempoMinutos    | 20        |
    Y deja el campo "pedido" vacío
    Y hace clic en "Guardar"
    Entonces se muestra un mensaje de validación indicando que el pedido es obligatorio
    Y el formulario permanece abierto

  Escenario: Intentar crear una tarea de empaque con cantidad negativa
    Dado que el usuario navega a la sección "Tareas de Empaque"
    Y hace clic en el botón "Crear nueva tarea de empaque"
    Cuando el usuario completa el formulario con:
      | campo            | valor     |
      | identificador    | EMP-005   |
      | pedido           | PED-001   |
      | tipo             | BOLSA     |
      | tamanio          | PEQUENO   |
      | cantidad         | -5        |
      | tiempoMinutos    | 15        |
    Y hace clic en "Guardar"
    Entonces se muestra un mensaje de validación indicando que la cantidad debe ser mayor a 0
    Y el formulario permanece abierto

  Escenario: Intentar crear una tarea de separación sin lote
    Dado que el usuario navega a la sección "Tareas de Separación"
    Y hace clic en el botón "Crear nueva tarea de separación"
    Cuando el usuario completa el formulario con:
      | campo            | valor         |
      | identificador    | SEP-004       |
      | pedido           | PED-002       |
      | cantidad         | 100           |
      | ubicacion        | Bodega A      |
    Y deja el campo "lote" vacío
    Y hace clic en "Guardar"
    Entonces se muestra un mensaje de validación indicando que el lote es obligatorio
    Y el formulario permanece abierto

  Escenario: Intentar crear una tarea de empaque con tiempo negativo
    Dado que el usuario navega a la sección "Tareas de Empaque"
    Y hace clic en el botón "Crear nueva tarea de empaque"
    Cuando el usuario completa el formulario con:
      | campo            | valor     |
      | identificador    | EMP-006   |
      | pedido           | PED-001   |
      | tipo             | CAJA      |
      | tamanio          | GRANDE    |
      | cantidad         | 8         |
      | tiempoMinutos    | -30       |
    Y hace clic en "Guardar"
    Entonces se muestra un mensaje de validación indicando que el tiempo debe ser mayor o igual a 0
    Y el formulario permanece abierto

  # ========== ELIMINACIÓN DE TAREAS ==========

  Escenario: Eliminar una tarea de empaque
    Dado que existe una tarea de empaque "EMP-001" asociada al pedido "PED-001"
    Y el usuario navega a la sección "Tareas de Empaque"
    Cuando el usuario selecciona eliminar la tarea "EMP-001"
    Y confirma la eliminación
    Entonces se muestra el mensaje "Tarea de empaque eliminada exitosamente"
    Y la tarea "EMP-001" no aparece en el listado

  Escenario: Eliminar una tarea de separación
    Dado que existe una tarea de separación "SEP-001" asociada al pedido "PED-001"
    Y el usuario navega a la sección "Tareas de Separación"
    Cuando el usuario selecciona eliminar la tarea "SEP-001"
    Y confirma la eliminación
    Entonces se muestra el mensaje "Tarea de separación eliminada exitosamente"
    Y la tarea "SEP-001" no aparece en el listado

  # ========== REPORTES Y ESTADÍSTICAS ==========

  Escenario: Generar reporte de tareas de empaque por tipo
    Dado que existen las siguientes tareas de empaque:
      | identificador | tipo        | cantidad |
      | EMP-001       | CAJA        | 10       |
      | EMP-002       | PALLET      | 5        |
      | EMP-003       | CAJA        | 15       |
      | EMP-004       | CONTENEDOR  | 3        |
      | EMP-005       | CAJA        | 8        |
    Y el usuario navega a la sección "Tareas de Empaque"
    Cuando el usuario solicita agrupar por tipo de empaque
    Entonces se muestra el resumen:
      | tipo        | total_tareas | total_cantidad |
      | CAJA        | 3            | 33             |
      | PALLET      | 1            | 5              |
      | CONTENEDOR  | 1            | 3              |

  Escenario: Calcular tiempo promedio de empaque por tipo de empaque
    Dado que existen las siguientes tareas de empaque:
      | identificador | tipo   | tiempoMinutos |
      | EMP-001       | CAJA   | 30            |
      | EMP-002       | CAJA   | 45            |
      | EMP-003       | PALLET | 120           |
      | EMP-004       | CAJA   | 60            |
    Y el usuario navega a la sección de estadísticas de empaque
    Cuando el usuario solicita el tiempo promedio por tipo
    Entonces se muestra:
      | tipo   | tiempo_promedio_minutos |
      | CAJA   | 45                      |
      | PALLET | 120                     |

