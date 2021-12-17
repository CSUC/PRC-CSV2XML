# PRC-CERIF   [![Build Status](https://travis-ci.org/CSUC/PRC-CSV2XML.svg?branch=develop)](https://travis-ci.org/CSUC/PRC-CSV2XML)

## Resources
* [Release Notes](../../releases)
* [Wiki](../../wiki/Home)

## Installing

```
mvn clean install -DskipTests
```

## Command line
```
spark-submit --master "local[*]" prc-cerif-{version.jar} args
```
```
Usage: prc-cerif [-fhV] -i=<PATH> [-o=<PATH>] -r=<STRING>
  -f, --formatted       formatted output file (default: false)
  -h, --help            Show this help message and exit.
  -i, --input=<PATH>    data file
  -o, --output=<PATH>   output file (default: /tmp/`ruct`.xml)
  -r, --ruct=<STRING>   ruct code (https://www.educacion.gob.es/ruct/home)
  -V, --version         Print version information and exit.
```
