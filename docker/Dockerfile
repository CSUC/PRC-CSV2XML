FROM apache/spark:v3.2.1

LABEL maintainer="Albert Martínez <albert.martinez@csuc.cat>"

USER root

ADD https://github.com/CSUC/PRC-CSV2XML/releases/download/2.4.18/euroCRIS-cerif-definitions-1.6.2.jar /opt/spark/work-dir/euroCRIS-cerif-definitions.jar
ADD https://github.com/CSUC/PRC-CSV2XML/releases/download/2.4.18/prc-cerif-2.4.18.jar /opt/spark/work-dir/prc-cerif.jar
COPY run.sh /opt/spark/work-dir

CMD ["/bin/bash"]