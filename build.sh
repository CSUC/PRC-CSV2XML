#!/bin/bash

## build
mvn clean install -DskipTests

## clean
rm -rf ~/.m2/repository/ ~/.ivy2/