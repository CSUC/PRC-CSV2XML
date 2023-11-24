FROM apache/spark:latest

LABEL maintainer="Albert Martínez <albert.martinez@csuc.cat>"

USER root

ADD https://github.com/CSUC/PRC-CSV2XML/releases/download/2.4.19/euroCRIS-cerif-definitions-1.6.2.jar /opt/spark/work-dir/euroCRIS-cerif-definitions.jar
ADD https://github.com/CSUC/PRC-CSV2XML/releases/download/2.4.19/prc-cerif-2.4.19.jar /opt/spark/work-dir/prc-cerif.jar
COPY run.sh /opt/spark/work-dir

ENV PATH "${SPARK_HOME}/bin:${PATH}"

CMD ["/bin/bash"]