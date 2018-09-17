# PRCJava2XML   [![Build Status](https://travis-ci.org/CSUC/PRC-CSV2XML.svg?branch=develop)](https://travis-ci.org/CSUC/PRC-CSV2XML)

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
 --deleteOnExit (--deleteOnExit)                                          : deleteOnExit temporal files
  -c (--charset) [UTF-8, ISO_8859_1, US_ASCII, UTF_16, UTF_16BE, UTF_16LE] : charset output file
  -d (--delimiter) <char>                                                  : delimiter char
  -f (--formatted)                                                         : formatted output file
  -i (--input) <Path>                                                      : input file
  -l (--endOfLine) <String>                                                : End Of Line Symbols
  -o (--output) <Path>                                                     : output file
  -ruct (--ruct) https://www.educacion.gob.es/ruct/home                    : ruct code                 : ruct code
```
