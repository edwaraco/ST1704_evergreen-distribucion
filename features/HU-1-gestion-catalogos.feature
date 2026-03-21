# language: es
Característica: Gestión de catálogos base del sistema de distribución
  Como administrador del sistema de distribución
  Quiero gestionar los catálogos fundamentales (clientes, productos, canales y transportes)
  Para tener disponible la información base que usarán los pedidos

  Antecedentes:
    Dado que el usuario está autenticado en el sistema
    Y se encuentra en la página principal

  # ========== GESTIÓN DE CLIENTES ==========

  Escenario: Crear un nuevo cliente exitosamente
    Dado que el usuario navega a la sección "Clientes"
    Y hace clic en el botón "Crear nuevo cliente"
    Cuando el usuario completa el formulario con:
      | campo         | valor                        |
      | identificador | CLI-001                      |
      | nombre        | Distribuidora El Pino S.A.   |
      | email         | contacto@elpino.com          |
      | telefono      | +57 300 1234567              |
      | direccion     | Calle 45 # 12-34, Medellín   |
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Cliente creado exitosamente"
    Y el cliente "Distribuidora El Pino S.A." aparece en el listado

  Escenario: Intentar crear un cliente con identificador duplicado
    Dado que existe un cliente con identificador "CLI-001"
    Y el usuario navega a la sección "Clientes"
    Y hace clic en el botón "Crear nuevo cliente"
    Cuando el usuario completa el formulario con:
      | campo         | valor                        |
      | identificador | CLI-001                      |
      | nombre        | Otro Cliente                 |
      | email         | otro@ejemplo.com             |
    Y hace clic en "Guardar"
    Entonces se muestra un mensaje de error indicando que el identificador ya existe
    Y el formulario permanece abierto

  Escenario: Editar información de un cliente existente
    Dado que existe un cliente con identificador "CLI-001"
    Y el usuario navega a la sección "Clientes"
    Cuando el usuario selecciona editar el cliente "CLI-001"
    Y actualiza el campo "telefono" a "+57 300 9876543"
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Cliente actualizado exitosamente"
    Y el cliente muestra el nuevo teléfono "+57 300 9876543"

  Escenario: Buscar clientes por nombre
    Dado que existen los siguientes clientes:
      | identificador | nombre                       |
      | CLI-001       | Distribuidora El Pino S.A.   |
      | CLI-002       | Comercial Norte LTDA         |
      | CLI-003       | Supermercados El Pino        |
    Y el usuario navega a la sección "Clientes"
    Cuando el usuario busca "Pino"
    Entonces se muestran 2 clientes en los resultados
    Y los resultados incluyen "Distribuidora El Pino S.A."
    Y los resultados incluyen "Supermercados El Pino"

  # ========== GESTIÓN DE PRODUCTOS ==========

  Escenario: Crear un nuevo producto con todos los campos obligatorios
    Dado que el usuario navega a la sección "Productos"
    Y hace clic en el botón "Crear nuevo producto"
    Cuando el usuario completa el formulario con:
      | campo              | valor                      |
      | identificador      | PROD-001                   |
      | nombre             | Leche entera 1L            |
      | descripcion        | Leche pasteurizada entera  |
      | fechaElaboracion   | 2026-03-15                 |
      | lote               | L20260315-A                |
      | cantidad           | 1000                       |
      | unidadMedida       | litros                     |
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Producto creado exitosamente"
    Y el producto "Leche entera 1L" aparece en el listado

  Escenario: Intentar crear un producto sin cantidad
    Dado que el usuario navega a la sección "Productos"
    Y hace clic en el botón "Crear nuevo producto"
    Cuando el usuario completa el formulario con:
      | campo              | valor                      |
      | identificador      | PROD-002                   |
      | nombre             | Yogurt natural 200ml       |
      | fechaElaboracion   | 2026-03-15                 |
    Y deja el campo "cantidad" vacío
    Y hace clic en "Guardar"
    Entonces se muestra un mensaje de validación indicando que la cantidad es obligatoria
    Y el formulario permanece abierto

  Escenario: Filtrar productos por fecha de elaboración
    Dado que existen los siguientes productos:
      | identificador | nombre              | fechaElaboracion |
      | PROD-001      | Leche entera 1L     | 2026-03-15       |
      | PROD-002      | Yogurt natural      | 2026-03-16       |
      | PROD-003      | Queso fresco        | 2026-03-15       |
    Y el usuario navega a la sección "Productos"
    Cuando el usuario filtra por fecha de elaboración "2026-03-15"
    Entonces se muestran 2 productos en los resultados
    Y los resultados incluyen "Leche entera 1L"
    Y los resultados incluyen "Queso fresco"

  # ========== GESTIÓN DE CANALES DE COMERCIALIZACIÓN ==========

  Escenario: Crear un nuevo canal de comercialización activo
    Dado que el usuario navega a la sección "Canales de Comercialización"
    Y hace clic en el botón "Crear nuevo canal"
    Cuando el usuario completa el formulario con:
      | campo         | valor                                    |
      | identificador | CANAL-001                                |
      | nombre        | Tienda Online                            |
      | descripcion   | Pedidos recibidos a través del sitio web |
      | activo        | Sí                                       |
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Canal creado exitosamente"
    Y el canal "Tienda Online" aparece en el listado como activo

  Escenario: Desactivar un canal de comercialización existente
    Dado que existe un canal activo con identificador "CANAL-001"
    Y el usuario navega a la sección "Canales de Comercialización"
    Cuando el usuario selecciona editar el canal "CANAL-001"
    Y cambia el estado "activo" a "No"
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Canal actualizado exitosamente"
    Y el canal "CANAL-001" aparece como inactivo

  # ========== GESTIÓN DE TRANSPORTES ==========

  Escenario: Crear un transporte terrestre con capacidades especificadas
    Dado que el usuario navega a la sección "Transportes"
    Y hace clic en el botón "Crear nuevo transporte"
    Cuando el usuario completa el formulario con:
      | campo            | valor      |
      | identificador    | TRANS-001  |
      | tipoTransporte   | TERRESTRE  |
      | matricula        | ABC-123    |
      | capacidadKg      | 5000       |
      | capacidadM3      | 20         |
      | estado           | Disponible |
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Transporte creado exitosamente"
    Y el transporte "TRANS-001" aparece en el listado con tipo "TERRESTRE"

  Escenario: Crear un transporte aéreo
    Dado que el usuario navega a la sección "Transportes"
    Y hace clic en el botón "Crear nuevo transporte"
    Cuando el usuario completa el formulario con:
      | campo            | valor      |
      | identificador    | TRANS-002  |
      | tipoTransporte   | AEREO      |
      | matricula        | HK-4567    |
      | capacidadKg      | 15000      |
      | capacidadM3      | 50         |
      | estado           | Disponible |
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Transporte creado exitosamente"
    Y el transporte "TRANS-002" aparece en el listado con tipo "AEREO"

  Escenario: Filtrar transportes por tipo y estado
    Dado que existen los siguientes transportes:
      | identificador | tipoTransporte | estado       |
      | TRANS-001     | TERRESTRE      | Disponible   |
      | TRANS-002     | AEREO          | Disponible   |
      | TRANS-003     | TERRESTRE      | En mantenimiento |
    Y el usuario navega a la sección "Transportes"
    Cuando el usuario filtra por tipo "TERRESTRE" y estado "Disponible"
    Entonces se muestra 1 transporte en los resultados
    Y el resultado incluye "TRANS-001"

  Escenario: Asignar fecha de asignación a un transporte
    Dado que existe un transporte con identificador "TRANS-001"
    Y el usuario navega a la sección "Transportes"
    Cuando el usuario selecciona editar el transporte "TRANS-001"
    Y establece la fecha de asignación a "2026-03-20"
    Y cambia el estado a "Asignado"
    Y hace clic en "Guardar"
    Entonces se muestra el mensaje "Transporte actualizado exitosamente"
    Y el transporte "TRANS-001" muestra la fecha de asignación "2026-03-20"

  # ========== ESCENARIOS DE VALIDACIÓN ==========

  Escenario: Intentar crear un producto con cantidad negativa
    Dado que el usuario navega a la sección "Productos"
    Y hace clic en el botón "Crear nuevo producto"
    Cuando el usuario completa el formulario con:
      | campo              | valor                      |
      | identificador      | PROD-004                   |
      | nombre             | Producto inválido          |
      | fechaElaboracion   | 2026-03-15                 |
      | cantidad           | -10                        |
      | unidadMedida       | unidades                   |
    Y hace clic en "Guardar"
    Entonces se muestra un mensaje de validación indicando que la cantidad debe ser mayor a 0
    Y el formulario permanece abierto

  Escenario: Eliminar un cliente que no tiene pedidos asociados
    Dado que existe un cliente con identificador "CLI-004" sin pedidos
    Y el usuario navega a la sección "Clientes"
    Cuando el usuario selecciona eliminar el cliente "CLI-004"
    Y confirma la eliminación
    Entonces se muestra el mensaje "Cliente eliminado exitosamente"
    Y el cliente "CLI-004" no aparece en el listado

