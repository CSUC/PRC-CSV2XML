#!/bin/bash

cd /opt/spark/work-dir

/opt/spark/bin/spark-submit --master "local[*]" --class org.csuc.cli.Cerif --packages info.picocli:picocli:4.6.3,com.crealytics:spark-excel_2.12:3.2.1_0.17.0,com.typesafe:config:1.4.2 --jars euroCRIS-cerif-definitions.jar prc-cerif.jar "$@"