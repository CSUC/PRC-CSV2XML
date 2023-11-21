#!/bin/bash

cd /opt/spark/work-dir

/opt/spark/bin/spark-submit --master "local[*]" --class org.csuc.cli.Cerif --packages info.picocli:picocli:4.7.5,com.crealytics:spark-excel_2.12:3.5.0_0.20.2,com.typesafe:config:1.4.3 --jars euroCRIS-cerif-definitions.jar prc-cerif.jar "$@"