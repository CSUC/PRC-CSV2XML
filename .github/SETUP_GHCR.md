# Configuració GitHub Container Registry

Instruccions per configurar la publicació automàtica d'imatges Docker a GitHub Container Registry (ghcr.io).

## ✅ El Workflow ja està creat

El fitxer `.github/workflows/docker.yml` ja està preparat i farà el següent automàticament:

- 🔨 Construir imatges Spark 3.5 i Spark 4
- 🏷️ Etiquetar amb versions semàntiques
- 📦 Pujar a `ghcr.io/csuc/prc-csv2xml`
- 🔄 Cache de GitHub Actions per builds ràpids

## 🚀 Activar la Publicació

### 1. Fer Push del Workflow

```bash
git add .github/workflows/docker.yml
git commit -m "ci: afegir workflow Docker build & push a GHCR"
git push origin main
```

### 2. Fer les Imatges Públiques (Opcional)

Per defecte, les imatges són **privades**. Per fer-les públiques:

1. Ves a: https://github.com/CSUC/PRC-CSV2XML/pkgs/container/prc-csv2xml
2. Clica **"Package settings"**
3. A **"Danger Zone"** → **"Change visibility"**
4. Selecciona **"Public"**
5. Confirma l'acció

Ara qualsevol pot fer `docker pull` sense autenticar-se!

### 3. Verificar que Funciona

Després del primer push a `main`, verifica:

```bash
# 1. Veure el workflow en acció
# https://github.com/CSUC/PRC-CSV2XML/actions

# 2. Un cop completat, pull de la imatge
docker pull ghcr.io/csuc/prc-csv2xml:latest

# 3. Executar
docker run --rm ghcr.io/csuc/prc-csv2xml:latest --version
```

## 🏷️ Tags Creats Automàticament

El workflow crea aquests tags:

| Event | Tags Generats | Exemple |
|-------|---------------|---------|
| **Push a main** | `latest`, `main-SHA` | `latest`, `main-abc1234` |
| **Tag v**** | `version`, `major.minor`, `major` | `v2.4.19`, `2.4`, `2` |
| **Pull Request** | `pr-NUMBER` | `pr-123` |

Totes amb sufixos per Spark 4: `-spark4`

## 📊 Metadata OCI

Cada imatge inclou labels OCI estàndard:

```yaml
org.opencontainers.image.title: PRC-CSV2XML
org.opencontainers.image.description: Conversor CSV/Excel a XML CERIF
org.opencontainers.image.version: 2.4.19
org.opencontainers.image.created: 2026-04-24T12:00:00Z
org.opencontainers.image.revision: abc1234567 (git SHA)
org.opencontainers.image.source: https://github.com/CSUC/PRC-CSV2XML
org.opencontainers.image.licenses: Apache-2.0
```

## 🔐 Permisos i Seguretat

El workflow utilitza `GITHUB_TOKEN` automàtic amb aquests permisos:

```yaml
permissions:
  contents: read    # Llegir codi
  packages: write   # Escriure packages (GHCR)
```

**No cal crear Personal Access Tokens (PAT)!** GitHub proporciona el token automàticament.

## 🔄 Publicar Versions

Per publicar una nova versió:

```bash
# 1. Crear tag
git tag -a v2.4.20 -m "Release v2.4.20"
git push origin v2.4.20

# 2. El workflow automàticament:
#    - Construeix les imatges
#    - Les etiqueta com: v2.4.20, 2.4.20, 2.4, 2, latest
#    - Les publica a ghcr.io
```

## 🌍 Multi-Platform (Opcional)

Si vols imatges per ARM (Apple Silicon, Raspberry Pi):

Edita `.github/workflows/docker.yml`:

```yaml
platforms: linux/amd64,linux/arm64  # Afegir arm64
```

⚠️ **Nota**: Builds multi-platform són més lents (~15 min vs ~5 min)

## 📦 Utilitzar les Imatges

### Des de qualsevol lloc

```bash
docker pull ghcr.io/csuc/prc-csv2xml:latest
docker run --rm -v $(pwd)/data:/data ghcr.io/csuc/prc-csv2xml:latest \
  --input /data/entrada.xlsx --output /data/sortida.xml --ruct CODE
```

### En CI/CD

```yaml
# GitHub Actions
jobs:
  convert:
    runs-on: ubuntu-latest
    container:
      image: ghcr.io/csuc/prc-csv2xml:latest
    steps:
      - run: spark-submit --input data/in.xlsx --output data/out.xml --ruct CODE
```

### En docker-compose.yml

```yaml
services:
  prc-cerif:
    image: ghcr.io/csuc/prc-csv2xml:latest
    volumes:
      - ./data:/data
```

## 🐛 Troubleshooting

### Error: "insufficient_scope"

**Causa**: El workflow no té permisos per escriure packages.

**Solució**: Verifica que `.github/workflows/docker.yml` té:
```yaml
permissions:
  packages: write
```

### Error: "unauthorized"

**Causa**: Intentant pull d'una imatge privada.

**Solució**: 
- Fes la imatge pública (veure pas 2)
- O autentica't: `echo $GITHUB_PAT | docker login ghcr.io -u USERNAME --password-stdin`

### Workflow falla en build

**Causa**: Error en Dockerfile o context.

**Solució**: Testa localment primer:
```bash
DOCKER_BUILDKIT=1 docker build -f docker/Dockerfile .
```

## 📚 Més Informació

- **GitHub Packages Docs**: https://docs.github.com/en/packages
- **GitHub Actions**: https://docs.github.com/en/actions
- **GHCR Docs**: https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry
- **Utilitzar imatges**: [docker/GITHUB_PACKAGES.md](../docker/GITHUB_PACKAGES.md)

---

**Resum**: Fes push del workflow → Espera que completi → Les imatges ja estan a ghcr.io! 🎉
