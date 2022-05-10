# Docker

## Run

manage volumes
```
export SOURCE_FOLDER={source}
export TARGET_FOLDER={target}
```
```
docker-compose -f docker/docker-compose.yml run -d prc-cerif
```

## Build
```
docker-compose -f docker/docker-compose.yml build
```

## Usage

```
docker exec -it {container_id} bash
```

```
sh run.sh --help

Usage: prc-cerif [-fhV] -i=<PATH> [-o=<PATH>] -r=<STRING>
-f, --formatted       formatted output file (default: false)
-h, --help            Show this help message and exit.
-i, --input=<PATH>    data file
-o, --output=<PATH>   output file (default: /tmp/`ruct`.xml)
-r, --ruct=<STRING>   ruct code (https://www.educacion.gob.es/ruct/home)
-V, --version         Print version information and exit.
```
