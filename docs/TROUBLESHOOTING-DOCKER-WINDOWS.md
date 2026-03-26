# Troubleshooting: JHipster en Docker desde Windows

Este documento recopila las lecciones aprendidas y soluciones a problemas comunes al ejecutar JHipster dentro de contenedores Docker cuando el host es Windows con Docker Desktop.

## Tabla de contenidos

1. [Error de distributionUrl en Maven Wrapper](#1-error-de-distributionurl-en-maven-wrapper)
2. [Volumen maven-cache no persiste correctamente](#2-volumen-maven-cache-no-persiste-correctamente)
3. [Rendimiento lento con node_modules y Webpack](#3-rendimiento-lento-con-node_modules-y-webpack)
4. [JHipster descarga Node.js aunque ya existe en el contenedor](#4-jhipster-descarga-nodejs-aunque-ya-existe-en-el-contenedor)
5. [Permisos de root en node_modules](#5-permisos-de-root-en-node_modules)
6. [Configuración preventiva recomendada](#6-configuración-preventiva-recomendada)

---

## 1. Error de distributionUrl en Maven Wrapper

### Síntoma

Al ejecutar `./mvnw` aparece un error similar a:

```
distributionUrl is not valid, must match *-bin.zip or maven-mvnd-*.zip,
but found 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-'aven/3.9.9/apache-maven-3.9.9-bin.zip
```

Nótese el texto corrupto `apache-'aven` en lugar de `apache-maven`.

### Causa

Los archivos fueron creados o modificados en Windows con line endings CRLF (`\r\n`). Cuando se ejecutan en Linux (dentro del contenedor), el caracter `\r` (carriage return) se interpreta como parte del texto, corrompiendo las URLs y rutas.

### Solución inmediata

Dentro del contenedor, ejecutar:

```bash
# Corregir archivos del Maven Wrapper
sed -i 's/\r$//' /evergreen/distribucion/.mvn/wrapper/maven-wrapper.properties
sed -i 's/\r$//' /evergreen/distribucion/mvnw
```

### Solución preventiva

Agregar un archivo `.gitattributes` en la raíz del repositorio:

```
# Auto detect text files and normalize line endings to LF
* text=auto eol=lf

# Force LF for critical files
*.properties text eol=lf
*.sh text eol=lf
mvnw text eol=lf
```

Luego normalizar los archivos existentes:

```bash
git add --renormalize .
git commit -m "chore: normalizar line endings"
```

---

## 2. Volumen maven-cache no persiste correctamente

### Síntoma

El volumen `maven-cache` montado en `/home/jhipster/.m2` no preserva las dependencias descargadas, o Maven falla al iniciar porque no encuentra el directorio `repository`.

### Causa

Cuando se monta un volumen vacío sobre un directorio existente, Docker **reemplaza completamente** el contenido del directorio. Si el Dockerfile crea subdirectorios como `.m2/repository` y `.m2/wrapper`, estos se pierden al montar el volumen vacío.

### Solución

Montar el volumen solo en el subdirectorio `repository`:

```yaml
# docker-compose.jhipster.yml
volumes:
  - maven-cache:/home/jhipster/.m2/repository  # Solo el repositorio
  - npm-cache:/home/jhipster/.npm
```

En lugar de:

```yaml
volumes:
  - maven-cache:/home/jhipster/.m2  # Esto sobrescribe toda la estructura
```

---

## 3. Rendimiento lento con node_modules y Webpack

### Síntoma

- `npm install` tarda excesivamente (10-30+ minutos)
- Webpack se queda "compilando" o "descomprimiendo" indefinidamente
- Alto uso de CPU sin progreso aparente

### Causa

Docker Desktop en Windows usa una capa de traducción del sistema de archivos entre NTFS (Windows) y el filesystem del contenedor Linux. Esta traducción es extremadamente lenta para operaciones con muchos archivos pequeños.

`node_modules` es el peor caso posible: miles de archivos pequeños con operaciones intensivas de lectura/escritura.

### Solución

Mover `node_modules` a un volumen nombrado (filesystem nativo de Linux):

```yaml
# docker-compose.jhipster.yml
services:
  jhipster-dev:
    volumes:
      - ../:/evergreen:rw
      - node-modules:/evergreen/distribucion/node_modules  # Volumen nativo
      - maven-cache:/home/jhipster/.m2/repository
      - npm-cache:/home/jhipster/.npm

volumes:
  node-modules:
    driver: local
  maven-cache:
    driver: local
  npm-cache:
    driver: local
```

### Alternativa

Trabajar directamente desde WSL2 en lugar de desde el filesystem de Windows:

```bash
# Clonar el proyecto dentro de WSL2
cd ~
git clone <repo> proyectos/evergreen

# Trabajar desde ahí (NO desde /mnt/c/...)
cd ~/proyectos/evergreen
```

---

## 4. JHipster descarga Node.js aunque ya existe en el contenedor

### Síntoma

Al ejecutar `./mvnw`, Maven descarga e instala Node.js en el directorio `target/node/`, ignorando la versión ya instalada en el contenedor. Esto causa:

- Descarga innecesaria (~50MB)
- Tiempo de descompresión (lento en Windows)
- Duplicación de Node.js

### Causa

JHipster usa el `frontend-maven-plugin` que, por diseño, descarga su propia copia de Node.js para garantizar una versión específica independiente del sistema.

### Solución A: Cachear la descarga

Agregar un volumen para el directorio donde se descarga Node:

```yaml
volumes:
  - node-install:/evergreen/distribucion/target/node
```

Así solo se descarga una vez y queda cacheado en el volumen.

### Solución B: Ejecutar frontend y backend por separado

```bash
# Frontend: usar Node.js del sistema directamente
npm install
npm run build

# Backend: saltar la compilación del frontend
./mvnw -P-webapp
```

### Nota importante

El flag `-Dskip.installnodenpm=true` **no funciona** porque el plugin sigue buscando npm en `target/node/` aunque no lo haya instalado.

---

## 5. Permisos de root en node_modules

### Síntoma

Al ejecutar `npm install` aparece un error de permisos:

```
EACCES: permission denied, mkdir '/evergreen/distribucion/node_modules/...'
```

O al listar el directorio:

```bash
ls -la node_modules/
# drwxr-xr-x root root node_modules
```

### Causa

Cuando Docker crea un volumen nombrado, lo inicializa con `root` como propietario. El usuario `jhipster` (UID 1000) que ejecuta los comandos no tiene permisos de escritura.

### Solución inmediata

Ejecutar una vez como root para corregir permisos:

```bash
docker exec -u root jhipster-dev chown -R jhipster:jhipster /evergreen/distribucion/node_modules
```

### Solución con entrypoint

Modificar `docker-compose.jhipster.yml` para corregir permisos al iniciar:

```yaml
services:
  jhipster-dev:
    user: root
    entrypoint: ["/bin/bash", "-c"]
    command:
      - |
        chown -R jhipster:jhipster /evergreen/distribucion/node_modules 2>/dev/null || true
        exec su jhipster -c "tail -f /dev/null"
```

### Solución en Dockerfile

Crear el directorio con el usuario correcto antes de que se monte el volumen:

```dockerfile
# Como root, antes de USER jhipster
RUN mkdir -p /evergreen/distribucion/node_modules && \
    chown jhipster:jhipster /evergreen/distribucion/node_modules

USER jhipster
```

---

## 6. Configuración preventiva recomendada

### Archivo .gitattributes

Crear en la raíz del repositorio:

```
# Auto detect and normalize to LF
* text=auto eol=lf

# Específicos
*.java text eol=lf
*.xml text eol=lf
*.properties text eol=lf
*.yml text eol=lf
*.json text eol=lf
*.ts text eol=lf
*.tsx text eol=lf
*.sh text eol=lf
mvnw text eol=lf

# Mantener CRLF en batch de Windows
*.bat text eol=crlf
*.cmd text eol=crlf
```

### docker-compose.jhipster.yml optimizado

```yaml
services:
  jhipster-dev:
    build:
      context: .
      dockerfile: Dockerfile.jhipster
    container_name: jhipster-dev
    volumes:
      # Proyecto (bind mount)
      - ../:/evergreen:rw
      # Volúmenes nativos para rendimiento
      - node-modules:/evergreen/distribucion/node_modules
      - node-install:/evergreen/distribucion/target/node
      - maven-cache:/home/jhipster/.m2/repository
      - npm-cache:/home/jhipster/.npm
    user: root
    entrypoint: ["/bin/bash", "-c"]
    command:
      - |
        # Corregir permisos de volúmenes
        chown -R jhipster:jhipster /evergreen/distribucion/node_modules 2>/dev/null || true
        chown -R jhipster:jhipster /evergreen/distribucion/target 2>/dev/null || true
        # Ejecutar como jhipster
        exec su jhipster -c "tail -f /dev/null"
    working_dir: /evergreen
    # ... resto de configuración

volumes:
  node-modules:
    driver: local
  node-install:
    driver: local
  maven-cache:
    driver: local
  npm-cache:
    driver: local
```

### Script de inicialización

Crear `scripts/init-docker.sh`:

```bash
#!/bin/bash
# Inicializar entorno Docker para desarrollo

echo "Corrigiendo line endings..."
find distribucion -name "*.properties" -exec sed -i 's/\r$//' {} \;
sed -i 's/\r$//' distribucion/mvnw 2>/dev/null || true

echo "Iniciando contenedor..."
docker compose -f docker/docker-compose.jhipster.yml up -d

echo "Esperando inicialización..."
sleep 5

echo "Corrigiendo permisos..."
docker exec -u root jhipster-dev chown -R jhipster:jhipster /evergreen/distribucion/node_modules 2>/dev/null || true

echo "Listo. Accede con: docker exec -it jhipster-dev bash"
```

---

## Resumen de comandos útiles

```bash
# Acceder al contenedor
docker exec -it jhipster-dev bash

# Corregir permisos de node_modules
docker exec -u root jhipster-dev chown -R jhipster:jhipster /evergreen/distribucion/node_modules

# Corregir line endings
docker exec jhipster-dev sed -i 's/\r$//' /evergreen/distribucion/mvnw

# Limpiar volúmenes y reiniciar
docker compose -f docker/docker-compose.jhipster.yml down -v
docker compose -f docker/docker-compose.jhipster.yml up -d

# Ver logs del contenedor
docker logs -f jhipster-dev

# Verificar propietario de directorios
docker exec jhipster-dev ls -la /evergreen/distribucion/node_modules
```

---

Proyecto Evergreen - Sistema de Distribución
EAFIT - Industrialización de Software
Documento actualizado: 2026-03-25

