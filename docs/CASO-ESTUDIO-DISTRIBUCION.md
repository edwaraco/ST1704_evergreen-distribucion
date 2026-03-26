# Caso de estudio: macroproceso de distribución

El presente documento describe el desarrollo del caso de estudio correspondiente al macroproceso de distribución, implementado mediante JHipster como herramienta de industrialización de software. A lo largo de las siguientes secciones se detallan las fases de implementación, los lenguajes de dominio específico utilizados, los hallazgos técnicos identificados durante el proceso y los resultados obtenidos.

## Tabla de contenidos

1. [Contexto del caso de estudio](#contexto-del-caso-de-estudio)
2. [Configuración del entorno de desarrollo](#configuración-del-entorno-de-desarrollo)
3. [Aplicación en el caso de estudio](#aplicación-en-el-caso-de-estudio)
4. [Uso de DSLs en la herramienta](#uso-de-dsls-en-la-herramienta)
5. [Hallazgos técnicos](#hallazgos-técnicos)
6. [Resultados obtenidos](#resultados-obtenidos)
7. [Conclusiones](#conclusiones)

---

## Contexto del caso de estudio

El macroproceso de distribución abarcó la gestión del flujo de productos desde su recepción hasta la entrega al cliente final. Para el sistema Evergreen, este proceso se dividió en tres historias de usuario que permitieron estructurar el desarrollo de manera incremental.

En la Tabla 1 se presentan las historias de usuario definidas junto con las entidades correspondientes a cada una.

**Tabla 1.** Historias de usuario del macroproceso de distribución.

| Historia | Descripción | Entidades |
|----------|-------------|-----------|
| HU-1 | Gestión de catálogos base | Cliente, Producto, CanalComercializacion, Transporte |
| HU-2 | Gestión de pedidos | Pedido (con relaciones a catálogos) |
| HU-3 | Gestión de tareas logísticas | Empaque, Separacion |

La primera historia contempló los catálogos que sirvieron como base para el resto del sistema. La segunda historia introdujo la entidad Pedido, la cual estableció relaciones con todas las entidades de la primera historia. Finalmente, la tercera historia agregó las entidades de empaque y separación, relacionadas directamente con los pedidos.

---

## Configuración del entorno de desarrollo

Antes de iniciar el desarrollo de las historias de usuario, el equipo definió la infraestructura y las herramientas que se utilizarían durante todo el proyecto. Esta configuración previa tuvo como objetivo garantizar que todos los miembros del equipo trabajaran en un entorno homogéneo y reproducible.

### Decisión de utilizar Docker

Se optó por utilizar Docker como plataforma de contenedorización para el entorno de desarrollo. Esta decisión se fundamentó en la necesidad de evitar problemas de compatibilidad entre las diferentes máquinas de los desarrolladores, así como en la facilidad para replicar el ambiente de desarrollo de manera consistente.

El equipo configuró un entorno de desarrollo basado en Docker Compose mediante el archivo `docker/docker-compose.jhipster.yml`. Este archivo definió un contenedor de desarrollo con todas las dependencias necesarias: Java 21, Node.js 22 y JHipster CLI. Adicionalmente, se configuraron volúmenes persistentes para las cachés de Maven y npm, lo cual redujo los tiempos de compilación en ejecuciones posteriores.

Los comandos utilizados para trabajar con el entorno Docker fueron los siguientes:

```bash
# Construir e iniciar el contenedor de desarrollo
docker compose -f docker/docker-compose.jhipster.yml up -d

# Acceder al contenedor para ejecutar comandos
docker exec -it jhipster-dev bash

# Dentro del contenedor, ejecutar JHipster
cd distribucion
jhipster jdl ../jdl/modelo-completo.jdl --force --skip-install

# Detener el contenedor (preserva el estado)
docker compose -f docker/docker-compose.jhipster.yml stop

# Reiniciar el contenedor después de haberlo detenido
docker compose -f docker/docker-compose.jhipster.yml start

# Destruir el contenedor (elimina el contenedor pero preserva volúmenes)
docker compose -f docker/docker-compose.jhipster.yml down
```

El contenedor se configuró con límites de recursos (8 CPU y 8 GB de memoria) y opciones de seguridad que prevenían la escalación de privilegios. El directorio del proyecto se montó como volumen, lo cual permitió editar los archivos desde el sistema anfitrión mientras se ejecutaban los comandos de JHipster dentro del contenedor.

<!-- TODO: Agregar captura de pantalla del contenedor Docker en ejecución -->

### Repositorio del proyecto

El código fuente del proyecto se alojó en un repositorio de GitHub, el cual sirvió como punto central para la colaboración del equipo. La estructura del repositorio se organizó de manera que separara los artefactos de configuración de los artefactos generados.

El repositorio se encuentra disponible en la siguiente ubicación: https://github.com/[usuario]/evergreen

<!-- TODO: Reemplazar con la URL real del repositorio -->

En la Figura 1 se presenta la estructura de directorios definida para el proyecto.

**Figura 1.** Estructura de directorios del proyecto Evergreen.

```
evergreen/
├── jdl/                    # Archivos JDL para generación
│   ├── 00-aplicacion.jdl
│   ├── 01-hu1-catalogos.jdl
│   ├── 02-hu2-pedidos.jdl
│   ├── 03-hu3-logistica.jdl
│   └── modelo-completo.jdl
├── features/               # Especificaciones BDD en Gherkin
│   ├── HU-1-gestion-catalogos.feature
│   ├── HU-2-gestion-pedidos.feature
│   └── HU-3-gestion-tareas-logisticas.feature
├── docs/                   # Documentación del proyecto
│   ├── CONFIGURACION-PLAYWRIGHT.md
│   ├── GUIA-IMPLEMENTACION-INCREMENTAL.md
│   └── CASO-ESTUDIO-DISTRIBUCION.md
├── distribucion/           # Proyecto JHipster generado
│   ├── src/
│   ├── e2e/
│   └── package.json
└── README.md
```

Esta estructura permitió mantener los archivos de definición del dominio (JDL) separados del código generado, lo cual facilitó el control de versiones y la regeneración del proyecto cuando fue necesario.

### Guía de implementación para el equipo

Con el propósito de estandarizar el proceso de desarrollo, se elaboró una guía de implementación incremental que describía los pasos a seguir para cada historia de usuario. Esta guía incluyó los comandos de generación, las verificaciones requeridas y las convenciones de commits.

La guía se documentó en el archivo `GUIA-IMPLEMENTACION-INCREMENTAL.md` dentro del directorio `docs/`. Entre los aspectos cubiertos se encontraban la configuración de Playwright para pruebas E2E, los comandos de JHipster para cada fase y la solución a problemas comunes identificados durante el desarrollo.

<!-- TODO: Agregar captura de pantalla de la guía de implementación -->

---

## Aplicación en el caso de estudio

### Fases de implementación

El desarrollo del caso de estudio siguió un enfoque por fases, donde cada fase correspondió a una historia de usuario. A continuación se describe el proceso llevado a cabo en cada una de ellas.

#### Fase 0: configuración inicial

Durante esta fase se generó el proyecto base de JHipster con la configuración requerida para el sistema. Se optó por una arquitectura monolítica con H2 como base de datos de desarrollo y PostgreSQL para producción. El frontend se implementó con React y la autenticación se configuró mediante JWT. Adicionalmente, se habilitó el soporte para los idiomas español e inglés.

El comando utilizado para la generación fue el siguiente:

```bash
jhipster jdl ../jdl/00-aplicacion.jdl
```

<!-- TODO: Agregar captura de pantalla de la generación del proyecto base -->

#### Fase 1: catálogos base (HU-1)

En esta fase se generaron las cuatro entidades que conformaron los catálogos del sistema. La entidad Cliente permitió gestionar la información de contacto con validación de correo electrónico. La entidad Producto manejó el catálogo de productos disponibles, incluyendo control de lotes y fechas de elaboración. Por su parte, CanalComercializacion registró los canales de venta con su estado de activación, mientras que Transporte almacenó la información de los vehículos disponibles categorizados por tipo.

```bash
jhipster jdl ../jdl/01-hu1-catalogos.jdl --skip-install
```

<!-- TODO: Agregar captura de pantalla del menú de entidades generadas -->
<!-- TODO: Agregar captura de pantalla del CRUD de Cliente -->

#### Fase 2: pedidos (HU-2)

La segunda fase introdujo la entidad Pedido, la cual requirió establecer relaciones con las entidades existentes. Se configuró una relación de muchos a uno con Cliente, dado que un pedido pertenece a un único cliente. Con Producto se estableció una relación de muchos a muchos bidireccional, permitiendo que un pedido contenga múltiples productos y que desde un producto se puedan consultar los pedidos asociados. Las relaciones con CanalComercializacion y Transporte se definieron como uno a muchos desde estas entidades hacia Pedido.

Debido a los hallazgos descritos en la sección correspondiente, se optó por utilizar un archivo JDL acumulativo con la bandera de forzado:

```bash
jhipster jdl ../jdl/modelo-completo.jdl --force --skip-install
```

<!-- TODO: Agregar captura de pantalla del formulario de Pedido -->
<!-- TODO: Agregar captura de pantalla mostrando las relaciones -->

#### Fase 3: tareas logísticas (HU-3)

Durante la tercera fase se agregaron las entidades correspondientes al proceso de preparación de pedidos. La entidad Empaque registró las tareas de empacado con información sobre el tipo de empaque, tamaño, cantidad y tiempo requerido. La entidad Separacion manejó las tareas de separación de productos por lotes, incluyendo la ubicación y el responsable asignado.

<!-- TODO: Agregar captura de pantalla de las entidades logísticas -->

---

## Uso de DSLs en la herramienta

JHipster empleó varios lenguajes de dominio específico (DSL, por sus siglas en inglés) para la definición y generación del sistema. En las siguientes subsecciones se describen los tres DSLs identificados durante el desarrollo del caso de estudio.

### JHipster Domain Language (JDL)

El JDL constituyó el lenguaje principal de JHipster para la definición del modelo de dominio. Mediante este lenguaje se especificaron la configuración de la aplicación, las entidades con sus atributos, las relaciones entre entidades y las opciones de generación como DTOs, servicios y paginación.

Para la definición de entidades, el JDL permitió especificar el nombre de cada campo, su tipo de dato y las validaciones requeridas. En el siguiente fragmento se muestra la definición de las entidades Cliente y Producto tal como fueron configuradas para el sistema:

```jdl
entity Cliente {
    identificador String required unique
    nombre String required maxlength(200)
    email String pattern(/^[^@\s]+@[^@\s]+\.[^@\s]+$/)
    telefono String maxlength(20)
    direccion String maxlength(500)
}

entity Producto {
    identificador String required unique
    nombre String required maxlength(200)
    descripcion String maxlength(1000)
    fechaElaboracion LocalDate required
    lote String maxlength(50)
    cantidad Integer required min(1)
    unidadMedida String maxlength(20)
}
```

El JDL también permitió definir enumeraciones que restringieron los valores permitidos para ciertos campos. En el caso de estudio se utilizaron enumeraciones para los tipos de transporte y los tipos de empaque:

```jdl
enum TipoTransporte {
    ACUATICO,
    AEREO,
    TERRESTRE
}

enum TipoEmpaque {
    CAJA,
    BOLSA,
    PALLET,
    CONTENEDOR,
    OTRO
}
```

Las relaciones entre entidades se especifican indicando el tipo de relación y los campos de referencia. El siguiente fragmento ilustra las tres formas de relación utilizadas en el sistema:

```jdl
relationship ManyToOne {
    Pedido{cliente(nombre) required} to Cliente
}

relationship ManyToMany {
    Pedido{producto(nombre)} to Producto{pedido(identificador)}
}

relationship OneToMany {
    CanalComercializacion{pedido} to Pedido{canalComercializacion(nombre)}
}
```

Finalmente, las opciones de generación permitieron configurar aspectos como la paginación, la generación de DTOs con MapStruct, la implementación de servicios y los filtros de búsqueda:

```jdl
paginate Cliente, Producto, Pedido with pagination
paginate CanalComercializacion, Transporte with infinite-scroll

dto * with mapstruct
service * with serviceImpl
filter Cliente, Producto, Pedido, Transporte
```

<!-- TODO: Agregar captura de pantalla del archivo JDL en el editor -->
<!-- TODO: Agregar captura de pantalla del diagrama generado por JDL-Studio -->

### Gherkin para especificaciones BDD

Para las especificaciones de comportamiento se empleó Gherkin, un DSL diseñado para pruebas de desarrollo guiado por comportamiento (BDD). Este lenguaje permitió describir escenarios de prueba en un formato legible tanto para desarrolladores como para personas no técnicas.

El siguiente ejemplo muestra un escenario de creación de cliente redactado en español:

```gherkin
# language: es

Característica: Gestión de Clientes
  Como administrador del sistema
  Quiero gestionar los clientes
  Para mantener actualizada la información de contacto

  Escenario: Crear un nuevo cliente
    Dado que el usuario está autenticado como administrador
    Y navega a la sección "Clientes"
    Cuando hace clic en "Crear nuevo Cliente"
    Y completa el formulario con:
      | campo         | valor                    |
      | identificador | CLI-001                  |
      | nombre        | Empresa ABC              |
      | email         | contacto@empresa.com     |
    Y guarda el registro
    Entonces el sistema muestra el mensaje "Cliente creado"
    Y el cliente aparece en la lista
```

<!-- TODO: Agregar captura de pantalla del archivo .feature -->

### Liquibase para gestión de esquemas

JHipster generó automáticamente changelogs de Liquibase para la gestión de los esquemas de base de datos. Estos archivos XML definieron las operaciones de creación y modificación de tablas, lo cual permitió mantener un historial de cambios y facilitar las migraciones entre ambientes.

En el siguiente fragmento se observa un changelog generado para la entidad Cliente:

```xml
<changeSet id="20260322213629-1" author="jhipster">
    <createTable tableName="cliente">
        <column name="id" type="bigint">
            <constraints primaryKey="true" nullable="false"/>
        </column>
        <column name="identificador" type="varchar(255)">
            <constraints nullable="false" unique="true"/>
        </column>
        <column name="nombre" type="varchar(200)">
            <constraints nullable="false"/>
        </column>
        <column name="email" type="varchar(255)"/>
        <column name="telefono" type="varchar(20)"/>
        <column name="direccion" type="varchar(500)"/>
    </createTable>
</changeSet>
```

<!-- TODO: Agregar captura de pantalla del changelog generado -->

---

## Hallazgos técnicos

### Investigación sobre generación incremental

Durante el desarrollo se investigó la viabilidad de utilizar un enfoque de generación incremental, donde cada historia de usuario contara con su propio archivo JDL. El objetivo era ejecutar cada archivo de forma secuencial y que JHipster agregara las nuevas entidades sin afectar las existentes. Los resultados de esta investigación se describen a continuación.

#### Problema inicial identificado

Al ejecutar el archivo JDL de la segunda historia de usuario después de haber generado la primera, se presentó el siguiente error de compilación:

```
[ERROR] ProductoMapper.java: cannot find symbol
  symbol:   class PedidoDTO
```

Tras analizar el problema, se determinó que la causa raíz estaba relacionada con las relaciones bidireccionales. Cuando se definió una relación bidireccional entre dos entidades, JHipster generó código en ambos lados de la relación. En este caso, aunque Pedido no estaba incluido en la opción de generación de DTOs, ProductoDTO sí fue modificado para incluir una referencia a PedidoDTO, el cual nunca se generó.

#### Primera prueba: archivo JDL con la entidad nueva únicamente

Se intentó crear un archivo JDL que definiera solamente la entidad Pedido con sus relaciones:

```jdl
entity Pedido { ... }
relationship ManyToOne {
    Pedido{cliente(nombre)} to Cliente
}
dto Pedido with mapstruct
```

El resultado fue el siguiente error:

```
ERROR! CanalComercializacion is not declared.
```

Este comportamiento indicó que JHipster requería que todas las entidades mencionadas en las relaciones estuvieran declaradas dentro del mismo archivo JDL. La herramienta no consultó automáticamente las entidades existentes almacenadas en el directorio `.jhipster/`.

#### Segunda prueba: archivo JDL con declaraciones vacías

Se realizó una segunda prueba declarando las entidades existentes como estructuras vacías, con la expectativa de que JHipster las fusionara con la configuración almacenada:

```jdl
entity Cliente
entity Producto
entity Pedido { ... }
dto Pedido with mapstruct
```

El resultado fue diferente pero igualmente fallido:

```
ERROR! could not find the related field nombre for the relationship cliente
```

Las declaraciones vacías sobrescribieron la configuración existente en los archivos `.jhipster/`, eliminando los campos previamente definidos.

#### Conclusión de la investigación

A partir de las pruebas realizadas se concluyó que JHipster no soportaba un modo de generación incremental puro mediante archivos JDL separados. Las razones identificadas fueron tres: primero, las declaraciones de entidades en el JDL reemplazaron completamente la configuración almacenada en `.jhipster/`; segundo, la herramienta no realizó una fusión inteligente con la configuración existente; tercero, las relaciones bidireccionales requirieron que ambas entidades estuvieran completamente definidas en el mismo archivo.

En la Tabla 2 se presentan los enfoques alternativos evaluados junto con sus implicaciones para Liquibase.

**Tabla 2.** Enfoques alternativos para la generación de entidades.

| Enfoque | Descripción | Comportamiento de Liquibase |
|---------|-------------|-----------|
| JDL completo por historia | Cada archivo incluye todas las entidades | Requiere bandera `--force` y limpieza de H2 |
| JDL único acumulativo | Un solo archivo que crece con cada historia | Changelogs incrementales naturales |
| Comando jhipster entity | Generación interactiva sin JDL | Changelogs incrementales naturales |

Con base en el análisis anterior, se optó por utilizar el enfoque de JDL único acumulativo mediante el archivo `modelo-completo.jdl`.

### Incompatibilidad de versiones de Java en imágenes Docker

Durante la configuración del entorno de desarrollo con Docker se identificó un problema relacionado con las versiones de Java incluidas en las imágenes oficiales de JHipster. Las imágenes disponibles en Docker Hub para JHipster contenían Java 17 como versión predeterminada. Sin embargo, las versiones más recientes de JHipster (8.x) requerían Java 21 como versión mínima para la compilación y ejecución del proyecto generado.

Este desajuste provocó errores durante la compilación del proyecto cuando se intentaba ejecutar Maven dentro del contenedor o cuando se copiaba el proyecto generado a un ambiente local con Java 21. El error típico observado fue el siguiente:

```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:
Fatal error compiling: error: release version 21 not supported
```

La causa del problema radicó en que el equipo de JHipster actualizó los requisitos de versión de Java en el generador, pero las imágenes de Docker publicadas en Docker Hub no habían sido actualizadas con la misma frecuencia. En la Tabla 3 se presenta la comparación entre las versiones esperadas y las encontradas.

**Tabla 3.** Comparación de versiones de Java en JHipster.

| Componente | Versión esperada | Versión en imagen Docker |
|------------|------------------|--------------------------|
| Java (JDK) | 21 | 17 |
| JHipster Generator | 8.x | 8.x |
| Node.js | 20.x | 20.x |

Para resolver esta situación se evaluaron dos alternativas. La primera consistió en construir una imagen Docker personalizada basada en Eclipse Temurin con Java 21 instalado. La segunda alternativa fue instalar JHipster de forma local en las máquinas de desarrollo, asegurando la versión correcta de Java. El equipo optó por la primera alternativa, dado que mantenía los beneficios de portabilidad y reproducibilidad que motivaron la elección inicial de Docker.

Se creó el archivo `docker/Dockerfile.jhipster` con una imagen personalizada basada en Eclipse Temurin JDK 21, a la cual se le agregó Node.js 22 y JHipster CLI. Esta imagen se integró con el archivo `docker-compose.jhipster.yml` descrito anteriormente, lo cual permitió que todos los miembros del equipo trabajaran con las versiones correctas de las dependencias.

Este hallazgo evidenció la importancia de verificar las versiones de las dependencias al utilizar imágenes de contenedores para herramientas de desarrollo, especialmente cuando dichas herramientas presentan ciclos de actualización frecuentes.

---

## Resultados obtenidos

### Artefactos generados

JHipster generó de forma automática los componentes descritos en las siguientes tablas. En la Tabla 4 se detallan los artefactos correspondientes al backend desarrollado con Spring Boot.

**Tabla 4.** Artefactos generados para el backend.

| Componente | Cantidad | Descripción |
|------------|----------|-------------|
| Entidades JPA | 7 | Clases de dominio con anotaciones de persistencia |
| Repositorios | 7 | Interfaces que extienden Spring Data JPA |
| Servicios | 7 | Clases con la lógica de negocio y uso de DTOs |
| Controladores REST | 7 | Endpoints con operaciones CRUD completas |
| DTOs | 7 | Objetos de transferencia de datos |
| Mappers | 7 | Clases de conversión entre entidades y DTOs |
| Pruebas | 21+ | Pruebas unitarias y de integración |

En la Tabla 5 se presentan los artefactos generados para el frontend implementado con React.

**Tabla 5.** Artefactos generados para el frontend.

| Componente | Cantidad | Descripción |
|------------|----------|-------------|
| Páginas CRUD | 7 | Vistas de listado, detalle, creación y edición |
| Modelos TypeScript | 7 | Interfaces de tipos para las entidades |
| Reducers Redux | 7 | Gestores de estado por entidad |
| Traducciones | 14 | Archivos de idioma (español e inglés) por entidad |

La Tabla 6 muestra los artefactos relacionados con la base de datos.

**Tabla 6.** Artefactos generados para la base de datos.

| Componente | Cantidad | Descripción |
|------------|----------|-------------|
| Changelogs Liquibase | 7+ | Archivos de migración de esquema |
| Datos de prueba | 7 | Archivos con datos ficticios para desarrollo |

<!-- TODO: Agregar captura de pantalla de la estructura de archivos generada -->

### Métricas de generación

<!-- TODO: Completar con las métricas reales obtenidas durante la generación -->

En la Tabla 7 se presentan las métricas obtenidas durante el proceso de generación.

**Tabla 7.** Métricas del proceso de generación.

| Métrica | Valor |
|---------|-------|
| Líneas de código generadas | Por determinar |
| Tiempo de generación | Por determinar |
| Archivos generados | Por determinar |

### Ejecución de la aplicación

<!-- TODO: Agregar capturas de pantalla de la aplicación en ejecución -->

A continuación se describen las principales pantallas del sistema implementado.

#### Pantalla de inicio de sesión

<!-- TODO: Agregar captura y descripción -->

#### Panel principal

<!-- TODO: Agregar captura y descripción -->

#### Gestión de clientes

<!-- TODO: Agregar capturas de listado, creación, edición y visualización de detalle -->

#### Gestión de pedidos con relaciones

<!-- TODO: Agregar capturas mostrando la selección de cliente, productos y demás relaciones -->

#### Consola de base de datos H2

<!-- TODO: Agregar captura de la consola H2 mostrando las tablas generadas -->

---

## Conclusiones

### Aspectos positivos de JHipster

El uso de JHipster permitió generar una aplicación completa con operaciones CRUD en un tiempo reducido. El código generado siguió una estructura estandarizada que facilitó su mantenimiento y comprensión. De igual forma, la herramienta implementó patrones de diseño recomendados como el uso de DTOs, servicios con interfaces y mappers. El stack tecnológico resultante combinó Spring Boot en el backend con React en el frontend, incluyendo seguridad mediante JWT. Adicionalmente, el soporte de internacionalización quedó integrado desde la generación inicial.

### Limitaciones identificadas

Durante el desarrollo se identificaron algunas limitaciones. La más relevante fue la imposibilidad de utilizar un esquema de generación incremental puro con archivos JDL separados. El código generado, si bien funcional, pudo requerir ajustes para adaptarse a requerimientos específicos del negocio. Por otra parte, la curva de aprendizaje inicial para dominar el JDL y las opciones de configuración representó una inversión de tiempo considerable.

### Aprendizajes del proceso

El proceso de desarrollo permitió obtener varios aprendizajes. El uso de un archivo JDL único y acumulativo evitó los problemas de regeneración asociados a archivos separados. Cuando existieron relaciones bidireccionales, todas las entidades involucradas debieron incluirse en las opciones de generación de DTOs. Al regenerar con la bandera `--force`, resultó necesario limpiar la base de datos H2 para evitar conflictos de checksums en Liquibase. Finalmente, el versionamiento con Git permitió rastrear los cambios realizados en cada historia de usuario.

---

## Referencias

- JHipster. (s.f.). JHipster Documentation. Recuperado de https://www.jhipster.tech/
- JHipster. (s.f.). JDL Reference. Recuperado de https://www.jhipster.tech/jdl/
- JHipster. (s.f.). JDL Studio. Recuperado de https://start.jhipster.tech/jdl-studio/
- Liquibase. (s.f.). Liquibase Documentation. Recuperado de https://www.liquibase.org/
- Cucumber. (s.f.). Gherkin Reference. Recuperado de https://cucumber.io/docs/gherkin/

---

Proyecto Evergreen - Sistema de Distribución
EAFIT - Industrialización de Software
Semestre 2, 2025

