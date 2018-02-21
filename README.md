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
 -d (--department) <Path>              : Department file
 -p (--publication) <Path>             : Publication file
 -pj (--project) <Path>                : Project file
 -r (--researcher) <Path>              : Researcher file
 -rd (--relationDepartment) <Path>     : Relation Department  file
 -rg (--researchgroup) <Path>          : Research Group file
 -rp (--relationPublication) <Path>    : Relation Publication file
 -rpj (--relationProject) <Path>       : Relation Project file
 -rrg (--relationResearchGroup) <Path> : Relation Research Group file
 -ruct (--ruct) VAL                    : ruct code
```