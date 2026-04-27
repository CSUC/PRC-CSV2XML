# PRC-CSV2XML Docker

Configuració Docker optimitzada per executar el convertidor de CSV/Excel a XML CERIF amb arquitectura multi-stage, cache BuildKit, health checks i logging configurat.

## Versions Disponibles

| Versió | Spark | Java | Scala | Dockerfile |
|--------|-------|------|-------|------------|
| **Estable** (Recomanada) | 3.5.1 | 8 | 2.12 | `Dockerfile` |
| **Spark 4** (Experimental) | 4.0.2 | 21 | 2.13 | `Dockerfile.spark4` |

## Inici Ràpid

### Opció 1: Imatge Pre-construïda (MÉS RÀPID) ⭐

Utilitza la imatge ja construïda des de GitHub Container Registry:

```bash
# Pull de la imatge
docker pull ghcr.io/csuc/prc-csv2xml:latest

# Executar directament (sense build)
docker run --rm -v $(pwd)/data:/data ghcr.io/csuc/prc-csv2xml:latest \
  --input /data/entrada.xlsx \
  --output /data/sortida.xml \
  --ruct RUCT_CODE

# Amb Spark 4
docker pull ghcr.io/csuc/prc-csv2xml:latest-spark4
docker run --rm -v $(pwd)/data:/data ghcr.io/csuc/prc-csv2xml:latest-spark4 \
  --input /data/entrada.xlsx \
  --output /data/sortida.xml \
  --ruct RUCT_CODE
```

📚 **Més informació**: [GITHUB_PACKAGES.md](GITHUB_PACKAGES.md)

### Opció 2: Makefile (Build Local)

```bash
# 1. Construir la imatge
make build

# 2. Executar la conversió
make run INPUT=entrada.xlsx OUTPUT=sortida.xml RUCT=CODE123

# Amb Spark 4
make build-spark4
make run-spark4 INPUT=entrada.xlsx OUTPUT=sortida.xml RUCT=CODE123

# Veure totes les comandes disponibles
make help
```

### Opció 2: Script Helper

```bash
# 1. Construir la imatge
cd docker
docker compose build prc-cerif

# 2. Executar amb el script
./run.sh --input /data/entrada.xlsx --output /data/sortida.xml --ruct CODE123

# Amb Spark 4
SPARK_VERSION=4 ./run.sh --input /data/entrada.xlsx --output /data/sortida.xml --ruct CODE123
```

### Opció 3: Docker Compose Directe

```bash
# 1. Construir la imatge (Spark 3.5 / Java 8)
docker compose --profile spark3 -f docker/docker-compose.yml build prc-cerif

# O la versió Spark 4 / Java 21
docker compose --profile spark4 -f docker/docker-compose.yml build prc-cerif-spark4

# 2. Copiar el fitxer d'entrada
mkdir -p data
cp fitxer.xlsx data/entrada.xlsx

# 3. Executar la conversió (Spark 3.5)
docker compose --profile spark3 -f docker/docker-compose.yml run --rm prc-cerif \
  --input /data/entrada.xlsx \
  --output /data/sortida.xml \
  --ruct RUCT_CODE

# O amb Spark 4
docker compose --profile spark4 -f docker/docker-compose.yml run --rm prc-cerif-spark4 \
  --input /data/entrada.xlsx \
  --output /data/sortida.xml \
  --ruct RUCT_CODE
```

## Requisits

- Docker 24+ amb **BuildKit habilitat** (per cache de Maven)
- Docker Compose v2+
- Mínim 6 GB de RAM disponible
- Make (opcional, per utilitzar el Makefile)
- Git (opcional, per generar VCS_REF automàticament)

## Estructura

```
PRC-CSV2XML/
├── .dockerignore              # Exclou fitxers innecessaris del context de build
├── Makefile                   # ⭐ Comandes simplificades (recomanat)
└── docker/
    ├── Dockerfile             # Spark 3.5.1 / Java 8 (estable)
    ├── Dockerfile.spark4      # Spark 4.0.2 / Java 21
    ├── docker-compose.yml     # Orquestració amb profiles, logging i health checks
    ├── run.sh                 # ⭐ Script helper per execució ràpida
    ├── .env.example           # Variables d'entorn d'exemple
    └── README.md              # Aquesta documentació
```

## Arquitectura dels Dockerfiles

Ambdós Dockerfiles utilitzen un **build multi-stage optimitzat** amb 4 stages:

```
Stage 1 (deps)         Stage 2 (builder)       Stage 3 (tester)        Stage 4 (runtime)
┌─────────────────┐   ┌─────────────────┐   ┌─────────────────┐   ┌─────────────────┐
│ Maven deps      │──>│ Compilació      │──>│ Tests           │──>│ Spark runtime   │
│ (cache BuildKit)│   │ mvn package     │   │ mvn test        │   │ + JARs          │
│                 │   │ [-Pspark4]      │   │ (opcional)      │   │ + health checks │
└─────────────────┘   └─────────────────┘   └─────────────────┘   └─────────────────┘
```

| Component | Dockerfile (estable) | Dockerfile.spark4 |
|---|---|---|
| Maven base | `maven:3.9-eclipse-temurin-8` | `maven:3.9-eclipse-temurin-21` |
| Perfil Maven | (default) | `-Pspark4` |
| Spark runtime | `apache/spark:3.5.1` | `apache/spark:4.0.2` |
| Scala | 2.12 | 2.13 |

### Optimitzacions Implementades

#### 🚀 Performance
- **Cache BuildKit Maven**: `--mount=type=cache,target=/root/.m2` accelera builds repetides
- **Cache de dependències**: els POMs es copien primer per aprofitar cache de Docker
- **Multi-stage build**: separa dependències, compilació, tests i runtime
- **Scope `provided`**: les dependències Spark no s'inclouen al JAR (ja són al runtime)

#### 🔒 Seguretat
- **Usuari non-root**: execució com a UID 185 (spark), no com a root
- **Permisos segurs**: `/data` amb permisos 775 (no 777)
- **Labels OCI estàndard**: metadades completes per seguretat i traçabilitat
- **Health checks**: verificació automàtica que els JARs existeixen
- **Validació de build**: comprovació que els artifacts s'han copiat correctament

#### 📦 Imatge
- **`.dockerignore` complet**: redueix context de build (exclou `.git`, `target/`, `.idea/`, `data/`)
- **Imatge final mínima**: només runtime Spark + JARs necessaris
- **Variables d'entorn**: `APP_VERSION`, `SPARK_VERSION` per introspection

#### 🔍 Observabilitat
- **Logging configurat**: rotació automàtica (max 10MB, 3 fitxers)
- **Health checks**: interval 30s, timeout 10s, 3 retries
- **Restart policy**: reinici automàtic en cas de fallada (max 3 intents)
- **Labels OCI**: build date, VCS ref, version per traçabilitat

## Ús

### 🎯 Amb Makefile (Recomanat)

El Makefile proporciona comandes simplificades amb colors i validació:

```bash
# Veure totes les comandes disponibles
make help

# Construir les imatges
make build                    # Spark 3.5 / Java 8
make build-spark4             # Spark 4 / Java 21
make build-all                # Ambdues versions

# Executar conversions
make run INPUT=entrada.xlsx OUTPUT=sortida.xml RUCT=CODE123
make run-spark4 INPUT=entrada.xlsx OUTPUT=sortida.xml RUCT=CODE123

# XML formatat (indentat)
make run INPUT=entrada.xlsx OUTPUT=sortida.xml RUCT=CODE123 FORMATTED=1

# Desenvolupament
make shell                    # Shell interactiu al contenidor
make test                     # Executar tests
make logs                     # Veure logs en temps real
make version                  # Info de build i versions

# Neteja
make clean                    # Eliminar imatges i contenidors
```

### 🔧 Amb Script Helper

El script `run.sh` detecta automàticament la versió de Spark:

```bash
cd docker

# Spark 3.5 (per defecte)
./run.sh --input /data/entrada.xlsx --output /data/sortida.xml --ruct CODE123

# Spark 4
SPARK_VERSION=4 ./run.sh --input /data/entrada.xlsx --output /data/sortida.xml --ruct CODE123

# Amb sortida formatada
./run.sh --input /data/entrada.xlsx --output /data/sortida.xml --ruct CODE123 --formatted

# Mostrar ajuda
./run.sh --help
```

### ⚙️ Amb Docker Compose

Utilitzant profiles per gestionar versions:

```bash
# Construir les imatges
docker compose --profile spark3 -f docker/docker-compose.yml build prc-cerif
docker compose --profile spark4 -f docker/docker-compose.yml build prc-cerif-spark4

# Executar conversions
docker compose --profile spark3 -f docker/docker-compose.yml run --rm prc-cerif \
  --input /data/entrada.xlsx \
  --output /data/sortida.xml \
  --ruct RUCT_CODE

docker compose --profile spark4 -f docker/docker-compose.yml run --rm prc-cerif-spark4 \
  --input /data/entrada.xlsx \
  --output /data/sortida.xml \
  --ruct RUCT_CODE

# Sense cache (rebuild complet)
docker compose --profile spark3 -f docker/docker-compose.yml build --no-cache prc-cerif
```

### 🐳 Amb Docker directe

```bash
# Construir amb BuildKit i arguments
DOCKER_BUILDKIT=1 docker build \
  --build-arg VERSION=2.4.19 \
  --build-arg BUILD_DATE=$(date -u +'%Y-%m-%dT%H:%M:%SZ') \
  --build-arg VCS_REF=$(git rev-parse --short HEAD) \
  -f docker/Dockerfile \
  -t prc-cerif:latest .

# Executar
docker run --rm -v $(pwd)/data:/data prc-cerif:latest \
  --input /data/entrada.xlsx \
  --output /data/sortida.xml \
  --ruct RUCT_CODE

# Mostrar ajuda
docker run --rm prc-cerif:latest --help
```

### Opcions disponibles

| Opció | Requerit | Descripció |
|------|----------|------------|
| `--input`, `-i` | Sí | Ruta al fitxer d'entrada (Excel/CSV) |
| `--output`, `-o` | No | Ruta al fitxer de sortida XML (default: `/tmp/<ruct>.xml`) |
| `--ruct`, `-r` | Sí | Codi [RUCT](https://www.educacion.gob.es/ruct/home) de la institució |
| `--formatted`, `-f` | No | Genera XML formatat/indentat (default: `false`) |
| `--help`, `-h` | No | Mostra l'ajuda |
| `--version`, `-V` | No | Mostra la versió |

## Configuració

### Variables d'Entorn

Crea un fitxer `.env` al directori `docker/` (pots copiar de `.env.example`):

```bash
# docker/.env
VERSION=2.4.19
BUILD_DATE=2026-04-24T10:30:00Z
VCS_REF=abc1234

# Executar com a usuari actual (recomanat per permisos)
DOCKER_UID=1000
DOCKER_GID=1000
```

O passa-les directament:

```bash
VERSION=2.4.19 BUILD_DATE=$(date -u +'%Y-%m-%dT%H:%M:%SZ') make build
```

### Recursos del Contenidor

El `docker-compose.yml` defineix els límits de recursos (iguals per ambdós serveis):

| Recurs | Límit | Reserva | Propòsit |
|--------|-------|---------|----------|
| CPU | 2 cores | 1 core | Processament Spark |
| Memòria | 6 GB | 4 GB | Driver Spark (4GB) + overhead (2GB) |

Per modificar els límits, edita `docker-compose.yml`:

```yaml
deploy:
  resources:
    limits:
      cpus: '4.0'
      memory: 8G
    reservations:
      cpus: '2.0'
      memory: 6G
```

### Restart Policy

Configuració de reinici automàtic en cas de fallada:

```yaml
restart_policy:
  condition: on-failure    # Només reinicia si falla (exit code != 0)
  delay: 5s               # Espera 5 segons abans de reiniciar
  max_attempts: 3         # Màxim 3 intents
  window: 120s            # Finestra de 2 minuts per comptabilitzar intents
```

### Logging

Rotació automàtica de logs per evitar omplir el disc:

```yaml
logging:
  driver: "json-file"
  options:
    max-size: "10m"       # Màxim 10 MB per fitxer
    max-file: "3"         # Mantenir 3 fitxers (30 MB total)
    compress: "true"      # Comprimir logs antics
```

Veure logs:

```bash
# Temps real
make logs

# O amb docker compose
docker compose -f docker/docker-compose.yml logs -f prc-cerif
```

## Solució de Problemes

### 🔴 Error de memòria (OutOfMemoryError)

**Símptomes**: `java.lang.OutOfMemoryError: Java heap space`

**Solució**: Augmenta la memòria al `docker-compose.yml`:

```yaml
environment:
  - SPARK_OPTS=--driver-java-options=-Xmx8g
  - JAVA_OPTS=-Xmx8g

deploy:
  resources:
    limits:
      memory: 10G
    reservations:
      memory: 8G
```

O directament amb Makefile:

```bash
# Edita Makefile i canvia SPARK_DRIVER_MEMORY
make run INPUT=entrada.xlsx OUTPUT=sortida.xml RUCT=CODE123
```

### 🔴 Error de dependències o build

**Símptomes**: Build falla, errors de Maven, dependencies no trobades

**Solució**:

```bash
# Opció 1: Rebuild sense cache
make clean
make build

# Opció 2: Netejar sistema Docker complet
docker system prune -a -f
DOCKER_BUILDKIT=1 docker compose --profile spark3 -f docker/docker-compose.yml build --no-cache prc-cerif

# Opció 3: Verificar BuildKit està habilitat
docker buildx version
export DOCKER_BUILDKIT=1
```

### 🔴 Error d'accés als fitxers

**Símptomes**: `Permission denied`, `No such file or directory`

**Solució**:

```bash
# 1. Verifica que el directori data/ existeix
mkdir -p data

# 2. Ajusta permisos
chmod 755 data

# 3. Executa amb el teu usuari (recomanat)
DOCKER_UID=$(id -u) DOCKER_GID=$(id -g) make run INPUT=entrada.xlsx OUTPUT=sortida.xml RUCT=CODE123

# O configura .env
echo "DOCKER_UID=$(id -u)" >> docker/.env
echo "DOCKER_GID=$(id -g)" >> docker/.env
```

### 🔴 Health Check falla

**Símptomes**: Contenidor en estat `unhealthy`

**Solució**:

```bash
# Verificar health status
docker inspect prc-cerif | grep Health -A 10

# Verificar que els JARs existeixen
docker compose --profile spark3 -f docker/docker-compose.yml run --rm --entrypoint /bin/bash prc-cerif -c "ls -lh /app/*.jar"

# Rebuild amb validació
make clean
make build
```

### 🔴 Build lent (més de 5 minuts)

**Símptomes**: Build triga molt, descarrega dependències cada vegada

**Solució**:

```bash
# Verifica que BuildKit està habilitat (per cache Maven)
export DOCKER_BUILDKIT=1

# Rebuild amb cache
docker compose --profile spark3 -f docker/docker-compose.yml build prc-cerif

# Verifica que el cache funciona
docker system df -v | grep buildkit
```

### 🔴 Error de codi RUCT

**Símptomes**: `Invalid RUCT code`, validació falla

**Solució**:

- Verifica que el codi RUCT és vàlid a: https://www.educacion.gob.es/ruct/home
- Format correcte: números i/o lletres, sense espais
- Exemple vàlid: `CODE123`, `RUCT_2024_001`

### 🔴 Profiles no funcionen

**Símptomes**: `service "prc-cerif" not found`

**Solució**:

```bash
# Especifica el profile correcte
docker compose --profile spark3 -f docker/docker-compose.yml run --rm prc-cerif --help
docker compose --profile spark4 -f docker/docker-compose.yml run --rm prc-cerif-spark4 --help

# O usa el Makefile que ja ho gestiona
make run INPUT=entrada.xlsx OUTPUT=sortida.xml RUCT=CODE123
```

### 📋 Veure logs detallats

```bash
# Opció 1: Amb Makefile
make logs

# Opció 2: Logs en temps real
docker compose -f docker/docker-compose.yml logs -f

# Opció 3: Guardar logs a fitxer
docker compose --profile spark3 -f docker/docker-compose.yml run --rm prc-cerif \
  --input /data/entrada.xlsx \
  --output /data/sortida.xml \
  --ruct RUCT_CODE 2>&1 | tee conversion.log

# Opció 4: Logs d'un contenidor específic
docker logs prc-cerif

# Opció 5: Inspeccionar health status
docker inspect prc-cerif --format='{{json .State.Health}}' | jq
```

### 🐚 Debug Interactiu

```bash
# Shell interactiu al contenidor
make shell

# O amb docker compose
docker compose --profile spark3 -f docker/docker-compose.yml run --rm --entrypoint /bin/bash prc-cerif

# Dins del contenidor:
ls -lh /app/
java -version
spark-submit --version
env | grep SPARK
```

## Versions i Compatibilitat

| Component | Estable (Recomanada) | Spark 4 (Experimental) |
|-----------|---------|---------|
| **Spark** | 3.5.1 | 4.0.2 |
| **Java** | 8 (Temurin) | 21 (Temurin) |
| **Scala** | 2.12 | 2.13 |
| **Spark Excel** | 3.5.1_0.20.4 | 3.5.1_0.20.4 |
| **Maven** | 3.9 | 3.9 |
| **PRC-CERIF** | 2.4.19 | 2.4.19 |
| **Docker** | 24+ (BuildKit) | 24+ (BuildKit) |

## Millores Implementades

Aquesta configuració Docker inclou optimitzacions avançades respecte a configuracions estàndard:

### ✅ Performance
- ⚡ **Cache BuildKit Maven**: builds 3-5x més ràpides en canvis incrementals
- 📦 **Multi-stage optimitzat**: imatges finals mínimes (només runtime + JARs)
- 🔄 **Stage de tests opcional**: executa tests sense ralentir builds de producció
- 🎯 **Layer caching intel·ligent**: POMs separats per màxima reutilització de cache

### ✅ Seguretat
- 🔒 **Execució non-root**: UID 185 (usuari spark), no root
- 🛡️ **Permisos restrictius**: 775 en lloc de 777 per directoris compartits
- 🏷️ **Labels OCI completes**: traçabilitat amb build date, VCS ref, version
- ✔️ **Validació de build**: verificació automàtica que els artifacts són correctes
- 💚 **Health checks**: detecció automàtica de problemes al contenidor

### ✅ Observabilitat
- 📊 **Logging configurat**: rotació automàtica (10MB x 3 fitxers, comprimit)
- 🔁 **Restart policy**: recuperació automàtica de fallades (max 3 intents)
- 🩺 **Health monitoring**: checks cada 30s amb timeout i retries
- 📝 **Variables d'entorn**: APP_VERSION i SPARK_VERSION per introspection

### ✅ Developer Experience
- 🎨 **Makefile complet**: comandes colorejades amb validació i help
- 🚀 **Script helper**: detecció automàtica de versió Spark
- 📋 **Profiles Docker Compose**: gestió clara de múltiples versions
- 🔧 **Variables d'entorn**: configuració flexible via .env
- 📚 **Documentació exhaustiva**: exemples per tots els casos d'ús

### ✅ Producció Ready
- 🎯 **Resource limits**: CPU i memòria controlats per estabilitat
- 🔄 **Restart automàtic**: resiliència davant fallades temporals
- 📦 **Imatges tagejades**: versions controlades amb BUILD_DATE i VCS_REF
- 🌐 **OCI compliant**: compatible amb Kubernetes, podman, altres runtimes

## Comparació amb Configuració Estàndard

| Característica | Estàndard | Aquesta Implementació |
|----------------|-----------|----------------------|
| Temps build inicial | ~5 min | ~5 min |
| Temps rebuild (canvis codi) | ~5 min | ~1 min (cache BuildKit) |
| Seguretat | Root user | Non-root (UID 185) |
| Permisos | 777 | 775 |
| Observabilitat | Logs bàsics | Health checks + logging rotat |
| Resiliència | Manual restart | Auto-restart (3 intents) |
| Developer UX | Docker commands llargs | Makefile + scripts |
| Profiles | No | Spark3 / Spark4 |
| Documentació | Bàsica | Exhaustiva amb troubleshooting |

## Contacte i Suport

**Mantenidor**: Albert Martínez  
📧 Email: [albert.martinez@csuc.cat](mailto:albert.martinez@csuc.cat)  
🏢 Organització: [CSUC](https://www.csuc.cat/)  
📦 Repositori: [https://github.com/CSUC/PRC-CSV2XML](https://github.com/CSUC/PRC-CSV2XML)

Per reportar problemes o suggerir millores, obre un issue al repositori.
