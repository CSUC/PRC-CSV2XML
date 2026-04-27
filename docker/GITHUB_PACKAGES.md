# Utilitzar Imatges des de GitHub Container Registry

Les imatges Docker de PRC-CSV2XML es publiquen automàticament a GitHub Container Registry (ghcr.io).

## 📦 Imatges Disponibles

```
ghcr.io/csuc/prc-csv2xml:latest          # Spark 3.5.1 / Java 8 (última versió)
ghcr.io/csuc/prc-csv2xml:latest-spark4   # Spark 4.0.2 / Java 21 (última versió)
ghcr.io/csuc/prc-csv2xml:2.4.19          # Versió específica
ghcr.io/csuc/prc-csv2xml:2.4.19-spark4   # Versió específica Spark 4
```

## 🚀 Ús Ràpid

### Amb Docker

```bash
# Pull de la imatge
docker pull ghcr.io/csuc/prc-csv2xml:latest

# Executar
docker run --rm -v $(pwd)/data:/data ghcr.io/csuc/prc-csv2xml:latest \
  --input /data/entrada.xlsx \
  --output /data/sortida.xml \
  --ruct RUCT_CODE
```

### Amb Docker Compose

Crea un `docker-compose.yml`:

```yaml
services:
  prc-cerif:
    image: ghcr.io/csuc/prc-csv2xml:latest
    volumes:
      - ./data:/data
    command: >
      --input /data/entrada.xlsx
      --output /data/sortida.xml
      --ruct RUCT_CODE
```

Executa:
```bash
docker compose run --rm prc-cerif
```

## 🔐 Autenticació

Les imatges públiques no requereixen autenticació. Si són privades:

```bash
# Crear un Personal Access Token (PAT) a GitHub amb scope 'read:packages'
echo $GITHUB_PAT | docker login ghcr.io -u USERNAME --password-stdin

# Després ja pots fer pull
docker pull ghcr.io/csuc/prc-csv2xml:latest
```

## 🏷️ Tags Disponibles

| Tag | Descripció |
|-----|------------|
| `latest` | Última versió Spark 3.5 de la branca main |
| `latest-spark4` | Última versió Spark 4 de la branca main |
| `2.4.19` | Versió específica (Spark 3.5) |
| `2.4.19-spark4` | Versió específica (Spark 4) |
| `main-abc1234` | Commit específic de main |
| `v2.4.19` | Tag de release |

## 📊 Metadata de la Imatge

Cada imatge conté labels OCI amb informació:

```bash
# Veure metadata
docker inspect ghcr.io/csuc/prc-csv2xml:latest | jq '.[0].Config.Labels'

# Labels disponibles:
# - org.opencontainers.image.version
# - org.opencontainers.image.created
# - org.opencontainers.image.revision (git SHA)
# - org.opencontainers.image.source (URL repositori)
# - org.opencontainers.image.licenses
```

## 🔄 Actualitzar a l'Última Versió

```bash
# Pull de l'última versió
docker pull ghcr.io/csuc/prc-csv2xml:latest

# Verificar versió
docker run --rm ghcr.io/csuc/prc-csv2xml:latest --version
```

## 🛠️ CI/CD Integration

### GitHub Actions

```yaml
jobs:
  convert:
    runs-on: ubuntu-latest
    container:
      image: ghcr.io/csuc/prc-csv2xml:latest
    steps:
      - name: Convert CSV to XML
        run: |
          /app/prc-cerif.sh \
            --input /github/workspace/data/input.xlsx \
            --output /github/workspace/data/output.xml \
            --ruct ${{ secrets.RUCT_CODE }}
```

### GitLab CI

```yaml
convert:
  image: ghcr.io/csuc/prc-csv2xml:latest
  script:
    - |
      spark-submit \
        --input data/input.xlsx \
        --output data/output.xml \
        --ruct $RUCT_CODE
```

## 📝 Comparació Build Local vs Registry

| Aspecte | Build Local | GitHub Registry |
|---------|-------------|-----------------|
| **Temps** | ~5 min | ~30 segons (pull) |
| **Espai disc** | Build layers (~2GB) | Només imatge (~1.2GB) |
| **Actualitzacions** | Manual rebuild | `docker pull` automàtic |
| **Consistència** | Pot variar | Sempre igual per tag |
| **CI/CD** | Requereix build step | Pull directe |

## 🔍 Verificar Signatura i Seguretat

```bash
# Verificar checksums
docker inspect --format='{{.Id}}' ghcr.io/csuc/prc-csv2xml:latest

# Scan de vulnerabilitats (si tens Docker Scout)
docker scout cves ghcr.io/csuc/prc-csv2xml:latest

# Veure historial de capes
docker history ghcr.io/csuc/prc-csv2xml:latest
```

## 🐛 Troubleshooting

### Error: "unauthorized: unauthenticated"

**Solució**: La imatge és privada, autentica't primer:
```bash
echo $GITHUB_PAT | docker login ghcr.io -u USERNAME --password-stdin
```

### Error: "manifest unknown"

**Solució**: El tag no existeix, verifica tags disponibles a:
https://github.com/CSUC/PRC-CSV2XML/pkgs/container/prc-csv2xml

### Imatge antiga

**Solució**: Força pull de la última versió:
```bash
docker pull --no-cache ghcr.io/csuc/prc-csv2xml:latest
```

## 📚 Més Informació

- **Repositori**: https://github.com/CSUC/PRC-CSV2XML
- **Packages**: https://github.com/CSUC/PRC-CSV2XML/pkgs/container/prc-csv2xml
- **Issues**: https://github.com/CSUC/PRC-CSV2XML/issues
- **Documentació**: [docker/README.md](README.md)

---

**Nota**: Les imatges es construeixen automàticament a cada push a `main` i per cada tag de versió.
