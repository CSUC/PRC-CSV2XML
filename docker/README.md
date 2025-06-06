# PRC-CSV2XML Docker

Aquest directori conté la configuració Docker per executar el convertidor de CSV/Excel a XML CERIF.

## Requisits

- Docker
- Docker Compose
- Mínim 4GB de RAM disponible

## Estructura

```
docker/
├── Dockerfile          # Configuració multi-stage per compilar i executar
├── docker-compose.yml  # Configuració del servei
└── data/              # Directori per als fitxers d'entrada/sortida
```

## Configuració

El Dockerfile utilitza un procés multi-stage:
1. Stage de compilació: Utilitza Maven per compilar el projecte
2. Stage d'execució: Utilitza Spark per executar l'aplicació

El docker-compose.yml configura:
- Memòria: 4GB per driver i executor
- Volums: Mapeig del directori `data` per fitxers d'entrada/sortida
- Healthcheck: Verifica que Spark estigui executant-se
- Restart: Automàtic en cas d'error

## Ús

1. Preparar els fitxers:
   ```bash
   # Crear el directori data si no existeix
   mkdir -p docker/data
   
   # Copiar el fitxer d'entrada
   cp ruta/al/fitxer.csv docker/data/entrada.csv
   ```

2. Construir la imatge:
   ```bash
   sudo docker compose -f docker/docker-compose.yml build
   ```

3. Executar el convertidor:
   ```bash
   # Forma bàsica
   sudo docker compose -f docker/docker-compose.yml run --rm prc-cerif \
     --input /data/entrada.csv \
     --output /data/sortida.xml \
     --ruct RUCT_CODE

   # Amb sortida formatada
   sudo docker compose -f docker/docker-compose.yml run --rm prc-cerif \
     --input /data/entrada.csv \
     --output /data/sortida.xml \
     --ruct RUCT_CODE \
     --formatted

   # Especificant un fitxer Excel
   sudo docker compose -f docker/docker-compose.yml run --rm prc-cerif \
     --input /data/entrada.xlsx \
     --output /data/sortida.xml \
     --ruct RUCT_CODE
   ```

4. El fitxer XML generat es trobarà a `docker/data/sortida.xml`

## Opcions disponibles

- `--input`: Ruta al fitxer d'entrada (CSV/Excel)
- `--output`: Ruta al fitxer de sortida (XML)
- `--ruct`: Codi RUCT de la institució
- `--formatted`: Genera un fitxer XML formatat (més llegible)
- `--help`: Mostra l'ajuda

## Solució de problemes

1. Error de memòria:
   - Augmentar la memòria a `docker-compose.yml`:
     ```yaml
     environment:
       - SPARK_OPTS=--driver-java-options=-Xmx8g
       - JAVA_OPTS=-Xmx8g
     ```

2. Error de dependències:
   - Verificar la connexió a Internet
   - Netejar la cache de Docker:
     ```bash
     sudo docker system prune -a
     ```

3. Error d'accés als fitxers:
   - Verificar els permisos del directori `data`
   - Assegurar que el fitxer d'entrada existeix
   - Comprovar que el format del fitxer és correcte (CSV o Excel)

4. Error de codi RUCT:
   - Verificar que el codi RUCT és vàlid
   - Consultar la llista de codis RUCT a: https://www.educacion.gob.es/ruct/home

## Notes

- La imatge utilitza Spark 3.5.1
- Les dependències es compilen durant la construcció de la imatge
- Els fitxers d'entrada/sortida es gestionen a través del directori `data`
- La versió actual és 2.4.19
- Els fitxers JAR s'instal·len amb noms genèrics:
  - `prc-cerif.jar` (versió ${VERSION})
  - `euroCRIS-cerif-definitions.jar` (versió ${VERSION})

## Contacte

Albert Martínez <albert.martinez@csuc.cat> 