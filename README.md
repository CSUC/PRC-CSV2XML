# PRCJava2XML   [![Build Status](https://travis-ci.org/CSUC/PRC-CSV2XML.svg?branch=master)](https://travis-ci.org/CSUC/PRC-CSV2XML)

## Resources
* [Release Notes](../../releases)
* [Wiki](../../wiki/Home)

## Installing

```
mvn clean install -DskipTests
```

## Command line

```
Usage: 
 -c (--charset) [UTF-8, ISO_8859_1, US_ASCII, UTF_16, UTF_16BE, UTF_16LE] : charset output file
 -d (--department) <Path>                                                 : Department file
 -f (--formatted)                                                         : formatted output file
 -o (--output) <Path>                                                     : output file
 -p (--publication) <Path>                                                : Publication file
 -pj (--project) <Path>                                                   : Project file
 -r (--researcher) <Path>                                                 : Researcher file
 -rd (--relationDepartment) <Path>                                        : Relation Department  file
 -rg (--researchgroup) <Path>                                             : Research Group file
 -rp (--relationPublication) <Path>                                       : Relation Publication file
 -rpj (--relationProject) <Path>                                          : Relation Project file
 -rrg (--relationResearchGroup) <Path>                                    : Relation Research Group file
 -ruct (--ruct) https://www.educacion.gob.es/ruct/home                    : ruct code
```