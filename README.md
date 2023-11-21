# PRC-CERIF   [![Build Status](https://github.com/CSUC/PRC-CSV2XML/actions/workflows/main.yml/badge.svg)](https://github.com/CSUC/PRC-CSV2XML/actions/workflows/main.yml)

## Resources
* [Release Notes](../../releases)
* [Wiki](../../wiki/Home)

## Build

```
sh build.sh
```

## Command line
https://spark.apache.org/docs/latest/submitting-applications.html
```
spark-submit --master "local[*]" --class org.csuc.cli.Cerif --packages info.picocli:picocli:4.7.5,com.crealytics:spark-excel_2.12:3.5.0_0.20.2,com.typesafe:config:1.4.3 --jars euroCRIS-cerif-definitions-1.6.2.jar prc-cerif-${version}.jar args
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
