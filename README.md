# PRC-CERIF 

[![Maven CI](https://github.com/CSUC/PRC-CSV2XML/actions/workflows/main.yml/badge.svg)](https://github.com/CSUC/PRC-CSV2XML/actions/workflows/main.yml)
[![Docker Build](https://github.com/CSUC/PRC-CSV2XML/actions/workflows/docker.yml/badge.svg)](https://github.com/CSUC/PRC-CSV2XML/actions/workflows/docker.yml)
[![GitHub Packages](https://img.shields.io/badge/ghcr.io-packages-blue?logo=docker)](https://github.com/CSUC/PRC-CSV2XML/pkgs/container/prc-csv2xml)

Conversor de dades de recerca en format Excel/CSV a XML seguint l'estàndard [CERIF](https://eurocris.org/services/main-features-cerif) (Common European Research Information Format).

## Descripció

Aquesta aplicació, desenvolupada pel [CSUC](https://www.csuc.cat/) (Consorci de Serveis Universitaris de Catalunya), processa fitxers Excel amb dades de recerca de les universitats catalanes i genera fitxers XML compatibles amb l'estàndard euroCRIS CERIF 1.6.

### Entitats suportades

| Entitat | Full Excel | Descripció |
|---------|------------|------------|
| Investigadors | `Investigadors` | Dades personals, ORCID, signatures, emails |
| Departaments | `Departaments,Instituts,Escoles` | Departaments, instituts i escoles |
| Relacions Dep-Inv | `Dep,Ins,Escoles-Investigadors` | Vinculació investigadors-departaments |
| Grups de recerca | `Grups_recerca` | Grups de recerca reconeguts |
| Relacions Grups-Inv | `Grups_recerca-Investigadors` | Membres i líders dels grups |
| Projectes | `Projectes_recerca` | Projectes de recerca |
| Relacions Proj-Inv | `Projectes_recerca-Investigadors` | Investigadors principals i co-investigadors |
| Publicacions | `Publicacions` | Articles, llibres, capítols, tesis |
| Relacions Pub-Aut | `Publicacions-Autors` | Autors i directors de publicacions |

## Tech Stack

| Component | Estable | Spark 4 |
|-----------|---------|---------|
| Java | 8 | 21 |
| Apache Spark | 3.5.1 | 4.0.2 |
| Scala | 2.12 | 2.13 |
| Spark Excel | 3.5.1_0.20.4 | 3.5.1_0.20.4 |
| Picocli | 4.7.6 | 4.7.6 |
| Maven | 3.9+ | 3.9+ |
| CERIF XSD | 1.6 | 1.6 |

## Requisits

- Java 8 (JDK) o Java 21 (per Spark 4)
- Apache Spark 3.5.1 o 4.0.2
- Maven 3.9+

## Instal·lació

### Des de font

```bash
git clone https://github.com/CSUC/PRC-CSV2XML.git
cd PRC-CSV2XML

# Spark 3.5 / Java 8 (per defecte)
mvn clean install -DskipTests

# Spark 4 / Java 21
mvn clean install -DskipTests -Pspark4
```

### Amb Docker (recomanat) 🐳

#### Opció 1: Utilitzar Imatge Pre-construïda (més ràpid) ⭐

```bash
# Pull de la imatge des de GitHub Container Registry
docker pull ghcr.io/csuc/prc-csv2xml:latest

# Executar amb el directori data del projecte
# Nota: pot requerir sudo chown després per canviar propietari dels fitxers generats
docker run --rm -v $(pwd)/data:/data ghcr.io/csuc/prc-csv2xml:latest \
  --input /data/entrada.xlsx \
  --output /data/sortida.xml \
  --ruct RUCT_CODE

# Amb paths personalitzats (input i output en directoris diferents)
docker run --rm \
  -v /tmp:/input:ro \
  -v ~/Baixades:/output \
  ghcr.io/csuc/prc-csv2xml:latest \
  --input /input/entrada.xlsx \
  --output /output/sortida.xml \
  --ruct RUCT_CODE

# Amb el mateix directori per input i output
docker run --rm \
  -v ~/Documents:/data \
  ghcr.io/csuc/prc-csv2xml:latest \
  --input /data/entrada.xlsx \
  --output /data/sortida.xml \
  --ruct RUCT_CODE

# Més info: docker/GITHUB_PACKAGES.md
```

#### Opció 2: Build Local

Configuració Docker optimitzada amb multi-stage builds, cache BuildKit, health checks i profiles:

```bash
# Amb Makefile (més senzill)
make build                    # Spark 3.5 / Java 8
make build-spark4             # Spark 4 / Java 21

# Amb Docker Compose
docker compose --profile spark3 -f docker/docker-compose.yml build prc-cerif
docker compose --profile spark4 -f docker/docker-compose.yml build prc-cerif-spark4
```

**Característiques de la configuració Docker:**
- ⚡ Cache BuildKit per builds 3-5x més ràpides
- 🔒 Execució non-root (UID 185) amb permisos segurs
- 💚 Health checks i restart automàtic
- 📊 Logging rotat (10MB x 3 fitxers)
- 🎯 Resource limits configurables
- 🚀 Scripts helper i Makefile per facilitar l'ús
- ✅ Script de validació per verificar configuració

**Documentació Docker:**
- 📖 [QUICKSTART.md](docker/QUICKSTART.md) - Guia ràpida
- 📚 [README.md](docker/README.md) - Documentació completa

**Scripts disponibles:**
```bash
make help               # Veure totes les comandes
./docker/build.sh      # Construir amb validació
./docker/run.sh        # Executar amb auto-detecció de versió
./docker/validate.sh   # Validar configuració
```

## Ús

### Opcions

```
Usage: prc-cerif [-fhV] -i=<PATH> [-o=<PATH>] -r=<STRING>
  -i, --input=<PATH>    fitxer de dades d'entrada (Excel/CSV)
  -o, --output=<PATH>   fitxer de sortida (default: /tmp/<ruct>.xml)
  -r, --ruct=<STRING>   codi RUCT de la institució
  -f, --formatted       XML formatat (default: false)
  -h, --help            mostra l'ajuda
  -V, --version         mostra la versió
```

### Amb Docker + Makefile (més senzill) 🚀

```bash
# Copiar el fitxer d'entrada al directori data (opcional si uses paths personalitzats)
mkdir -p data
cp fitxer.xlsx data/entrada.xlsx

# Executar amb Spark 3.5 (estable) - usa el directori data/ per defecte
make run INPUT=entrada.xlsx OUTPUT=sortida.xml RUCT=RUCT_CODE

# Executar amb Spark 4
make run-spark4 INPUT=entrada.xlsx OUTPUT=sortida.xml RUCT=RUCT_CODE

# XML formatat (indentat)
make run INPUT=entrada.xlsx OUTPUT=sortida.xml RUCT=RUCT_CODE FORMATTED=1

# Amb paths personalitzats (input i output de diferents directoris)
make run INPUT_DIR=/tmp INPUT=entrada.xlsx \
         OUTPUT_DIR=~/Baixades OUTPUT=sortida.xml \
         RUCT=RUCT_CODE

# Amb el mateix directori per input i output
make run INPUT_DIR=~/Documents INPUT=dades.xlsx OUTPUT=resultat.xml RUCT=RUCT_CODE

# Veure totes les comandes disponibles
make help
```

**Notes sobre paths personalitzats:**
- Per defecte, `INPUT_DIR` i `OUTPUT_DIR` són `./data`
- Si especifiques directoris diferents, el Makefile usarà `docker run` directament
- Si input i output són al mateix directori, es munta com a `/data`
- Si són en directoris diferents, es munten com `/input` (read-only) i `/output`
- Els paths suportats inclouen `~` per al home directory

### Amb Docker Compose

```bash
# Copiar el fitxer d'entrada
mkdir -p data
cp fitxer.xlsx data/entrada.xlsx

# Executar amb Spark 3.5 (estable)
docker compose --profile spark3 -f docker/docker-compose.yml run --rm prc-cerif \
  --input /data/entrada.xlsx \
  --output /data/sortida.xml \
  --ruct RUCT_CODE

# Executar amb Spark 4
docker compose --profile spark4 -f docker/docker-compose.yml run --rm prc-cerif-spark4 \
  --input /data/entrada.xlsx \
  --output /data/sortida.xml \
  --ruct RUCT_CODE
```

### Amb Spark (sense Docker)

```bash
# Spark 3.5
spark-submit \
  --master "local[*]" \
  --driver-memory 4g \
  --jars euroCRIS-cerif-definitions/target/euroCRIS-cerif-definitions-2.4.19.jar \
  --class org.csuc.cli.Cerif \
  transformation/target/prc-cerif-2.4.19.jar \
  --input fitxer.xlsx \
  --output sortida.xml \
  --ruct RUCT_CODE
```

## Estructura del projecte

```
PRC-CSV2XML/
├── euroCRIS-cerif-definitions/     # Definicions XSD i classes JAXB per CERIF 1.6
│   └── src/main/resources/
│       └── CERIF_1.6_2.xsd
├── transformation/                  # Codi font de l'aplicació principal
│   └── src/main/java/org/csuc/
│       ├── cli/                 # Punt d'entrada (Cerif.java)
│       ├── marshal/             # Conversió a objectes CERIF
│       ├── typesafe/            # Configuració semàntica CERIF
│       ├── utils/               # Utilitats (fulls Excel, tipus document)
│       └── global/              # Utilitats de dates
├── docker/                          # Configuració Docker
│   ├── Dockerfile               # Spark 3.5.1 / Java 8 (estable)
│   ├── Dockerfile.spark4        # Spark 4.0.2 / Java 21
│   ├── docker-compose.yml       # Orquestració amb dos serveis
│   └── README.md                # Documentació Docker
├── data/                            # Fitxers d'entrada/sortida
├── example/                         # Fitxers d'exemple
└── build.sh                         # Script de compilació
```

## Perfils Maven

| Perfil | Spark | Scala | Java | Ús |
|--------|-------|-------|------|----|
| (default) | 3.5.1 | 2.12 | 8 | `mvn clean install` |
| `spark4` | 4.0.2 | 2.13 | 21 | `mvn clean install -Pspark4` |

## Format d'entrada

El fitxer d'entrada ha de ser un Excel (`.xlsx`) amb els fulls següents:

- **Investigadors**: Nom, ORCID, Signatura, Emails
- **Departaments,Instituts,Escoles**: Nom, Acrònim, Adreça, URL, Email, Codi, Telèfon
- **Dep,Ins,Escoles-Investigadors**: Codi departament, ORCID investigador
- **Grups_recerca**: Nom, Acrònim, URL, Email, Codi intern, Codi regional, Data creació
- **Grups_recerca-Investigadors**: Codi grup, Nom, ORCID, Rol (IP: si/s)
- **Projectes_recerca**: Títol, URL, Codi oficial, Codi intern, Programa, Data inici, Data fi
- **Projectes_recerca-Investigadors**: Codi projecte, Nom, ORCID, Rol (IP: si/s)
- **Publicacions**: Títol, Codi, DOI, Handle, Num, Vol, Pàg inici, Pàg fi, ISBN, ISSN, Data, Revista, Editorial, Tipus, Autors grup
- **Publicacions-Autors**: Codi publicació, Nom, ORCID, Rol (director: si/s)

Consulta el fitxer `example/example.xlsx` com a referència.

## Recursos

- [Release Notes](../../releases)
- [Wiki](../../wiki/Home)
- [RUCT - Registre d'Universitats](https://www.educacion.gob.es/ruct/home)
- [euroCRIS CERIF](https://eurocris.org/services/main-features-cerif)

## Llicència

MIT License - Copyright (c) 2018 Consorci de Serveis Universitaris de Catalunya. Veure [LICENSE](LICENSE).

## Contacte

Albert Martínez - [albert.martinez@csuc.cat](mailto:albert.martinez@csuc.cat) - [CSUC](https://www.csuc.cat/)
