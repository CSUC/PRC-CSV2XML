# PRC-CERIF   [![Build Status](https://github.com/CSUC/PRC-CSV2XML/actions/workflows/main.yml/badge.svg)](https://github.com/CSUC/PRC-CSV2XML/actions/workflows/main.yml)

## Descripció
Aquesta aplicació converteix dades de recerca en format CSV a XML seguint l'estàndard CERIF (Common European Research Information Format). L'aplicació està dissenyada per processar dades de recerca de la Universitat Politècnica de Catalunya (UPC) i generar fitxers XML compatibles amb el sistema euroCRIS.

## Característiques
- Conversió de dades de recerca a format CERIF XML
- Suport per a investigadors, departaments, grups de recerca, projectes i publicacions
- Processament de dades utilitzant Apache Spark
- Generació d'identificadors únics (UUID) per a cada entitat
- Suport per a múltiples idiomes en els metadades

## Requisits
- Java 8 o superior
- Apache Spark
- Maven 3.x

## Instal·lació

### Des de font
```bash
git clone https://github.com/CSUC/PRC-CSV2XML.git
cd PRC-CSV2XML
sh build.sh
```

### Des de Docker
```bash
docker pull csuc/prc-cerif:latest
```

## Ús

### Línia de comandes
```bash
spark-submit --master "local[*]" --class org.csuc.cli.Cerif --packages info.picocli:picocli:4.7.6,com.crealytics:spark-excel_2.12:3.5.1_0.20.4,com.typesafe:config:1.4.3 --jars euroCRIS-cerif-definitions-${version}.jar prc-cerif-${version}.jar args
```

### Opcions
```
Usage: prc-cerif [-fhV] -i=<PATH> [-o=<PATH>] -r=<STRING>
  -f, --formatted       formatted output file (default: false)
  -h, --help            Show this help message and exit.
  -i, --input=<PATH>    data file
  -o, --output=<PATH>   output file (default: /tmp/`ruct`.xml)
  -r, --ruct=<STRING>   ruct code (https://www.educacion.gob.es/ruct/home)
  -V, --version         Print version information and exit.
```

### Docker
```bash
docker run -v /path/to/data:/opt/spark/work-dir csuc/prc-cerif:latest -i input.xlsx -r RUCT_CODE -o output.xml
```

## Estructura del projecte
- `euroCRIS-cerif-definitions`: Definicions XSD i classes generades per al format CERIF
- `transformation`: Codi font de l'aplicació principal
  - `src/main/java/org/csuc/cli`: Classes de línia de comandes
  - `src/main/java/org/csuc/marshal`: Classes per a la conversió de dades
  - `src/main/java/org/csuc/typesafe`: Configuracions i semàntica
  - `src/main/resources`: Fitxers de configuració

## Recursos
* [Release Notes](../../releases)
* [Wiki](../../wiki/Home)

## Llicència
Aquest projecte està llicenciat sota la llicència MIT - veure el fitxer [LICENSE](LICENSE) per més detalls.

## Contacte
Albert Martínez <albert.martinez@csuc.cat>
