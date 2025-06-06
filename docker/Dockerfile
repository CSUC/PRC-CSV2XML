# Stage 1: Compilació
FROM maven:3.8-openjdk-8-slim AS builder

ENV VERSION="2.4.19"

LABEL maintainer="Albert Martínez <albert.martinez@csuc.cat>"
LABEL version="$VERSION"
LABEL description="PRC-CSV2XML: Conversor de CSV/Excel a XML CERIF"

# Configurar repositori Maven central
RUN mkdir -p /root/.m2 && \
    echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd"><mirrors><mirror><id>central</id><name>Maven Central</name><url>https://repo.maven.apache.org/maven2</url><mirrorOf>central</mirrorOf></mirror></mirrors></settings>' > /root/.m2/settings.xml

# Copiar el codi font
WORKDIR /app
COPY . .

# Compilar el projecte
RUN mvn clean install -DskipTests

# Stage 2: Execució
FROM bitnami/spark:3.5.1

ENV VERSION="2.4.19"

LABEL maintainer="Albert Martínez <albert.martinez@csuc.cat>"
LABEL version="$VERSION"
LABEL description="PRC-CSV2XML: Conversor de CSV/Excel a XML CERIF"

# Crear directoris necessaris
USER root
RUN mkdir -p /app /data

# Copiar els JARs compilats
COPY --from=builder /app/transformation/target/prc-cerif-$VERSION.jar /app/prc-cerif.jar
COPY --from=builder /app/euroCRIS-cerif-definitions/target/euroCRIS-cerif-definitions-$VERSION.jar /app/euroCRIS-cerif-definitions.jar

# Configurar el directori de treball
WORKDIR /app

# Comanda per defecte
ENTRYPOINT ["spark-submit", \
    "--master", "local[*]", \
    "--driver-memory", "4g", \
    "--executor-memory", "4g", \
    "--packages", "info.picocli:picocli:4.7.6,com.crealytics:spark-excel_2.12:3.5.1_0.20.4,com.typesafe:config:1.4.3,commons-io:commons-io:2.15.0,commons-codec:commons-codec:1.16.0,org.apache.commons:commons-lang3:3.13.0,org.apache.commons:commons-text:1.10.0,org.slf4j:slf4j-api:2.0.11,org.apache.commons:commons-compress:1.25.0", \
    "--jars", "/app/euroCRIS-cerif-definitions.jar", \
    "--class", "org.csuc.cli.Cerif", \
    "/app/prc-cerif.jar"] 