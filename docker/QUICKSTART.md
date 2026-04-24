# Quick Start - PRC-CSV2XML Docker

Guia ràpida per començar a utilitzar PRC-CSV2XML amb Docker.

## 🚀 En 3 Passos

### 1. Construir la imatge

```bash
# Opció A: Amb Makefile (més senzill)
make build

# Opció B: Amb Docker Compose
docker compose --profile spark3 -f docker/docker-compose.yml build prc-cerif
```

### 2. Preparar dades

```bash
mkdir -p data
cp /ruta/al/fitxer.xlsx data/entrada.xlsx
```

### 3. Executar

```bash
# Amb Makefile
make run INPUT=entrada.xlsx OUTPUT=sortida.xml RUCT=TU_CODI_RUCT

# Amb Docker Compose
docker compose --profile spark3 -f docker/docker-compose.yml run --rm prc-cerif \
  --input /data/entrada.xlsx \
  --output /data/sortida.xml \
  --ruct TU_CODI_RUCT
```

## 🔧 Comandes Útils

```bash
make help          # Veure totes les comandes
make build-spark4  # Construir versió Spark 4
make shell         # Shell interactiu
make clean         # Netejar tot
```

## ❓ Problemes Comuns

**Permission denied:**
```bash
DOCKER_UID=$(id -u) DOCKER_GID=$(id -g) make run INPUT=entrada.xlsx OUTPUT=sortida.xml RUCT=CODE
```

**OutOfMemoryError:** Augmenta memòria a `docker-compose.yml` (SPARK_OPTS, deploy.resources.limits.memory)

**Build lent:** Habilita BuildKit: `export DOCKER_BUILDKIT=1`

## 📚 Més Info

Consulta [docker/README.md](README.md) per documentació completa.
