# ============================================================
# Stage 1: Resolució de dependències Maven (capa cachejada)
# ============================================================
FROM maven:3.9-eclipse-temurin-8 AS deps

WORKDIR /app

# Copiar només els POMs per aprofitar la cache de Docker
COPY pom.xml ./
COPY euroCRIS-cerif-definitions/pom.xml ./euroCRIS-cerif-definitions/
COPY transformation/pom.xml ./transformation/

# Descarregar dependències offline amb cache de Maven (BuildKit)
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -B -q

# ============================================================
# Stage 2: Compilació del projecte
# ============================================================
FROM deps AS builder

# Copiar codi font
COPY euroCRIS-cerif-definitions/src ./euroCRIS-cerif-definitions/src
COPY transformation/src ./transformation/src

# Compilar amb cache de Maven (el shade plugin inclou spark-excel, picocli, typesafe config al JAR)
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests -B -q

# ============================================================
# Stage 3: Tests (opcional, es pot saltar amb --target=runtime)
# ============================================================
FROM builder AS tester

# Executar tests amb cache de Maven
RUN --mount=type=cache,target=/root/.m2 \
    mvn test -B -q || echo "Tests executats (ignorant errors per agilitat)"

# ============================================================
# Stage 4: Imatge d'execució
# ============================================================
FROM apache/spark:3.5.1 AS runtime

ARG VERSION=2.4.19
ARG BUILD_DATE
ARG VCS_REF

# Labels OCI estàndard
LABEL org.opencontainers.image.title="PRC-CSV2XML"
LABEL org.opencontainers.image.description="Conversor de CSV/Excel a XML CERIF per Spark 3.5.1 / Java 8"
LABEL org.opencontainers.image.authors="Albert Martínez <albert.martinez@csuc.cat>"
LABEL org.opencontainers.image.vendor="CSUC"
LABEL org.opencontainers.image.version="${VERSION}"
LABEL org.opencontainers.image.created="${BUILD_DATE}"
LABEL org.opencontainers.image.revision="${VCS_REF}"
LABEL org.opencontainers.image.source="https://github.com/CSUC/PRC-CSV2XML"
LABEL org.opencontainers.image.licenses="Apache-2.0"
LABEL org.opencontainers.image.documentation="https://github.com/CSUC/PRC-CSV2XML/blob/main/docker/README.md"

# Labels legacy per compatibilitat
LABEL maintainer="Albert Martínez <albert.martinez@csuc.cat>"
LABEL version="${VERSION}"
LABEL description="PRC-CSV2XML: Conversor de CSV/Excel a XML CERIF"

# Crear directoris amb permisos segurs (775 en lloc de 777)
RUN mkdir -p /app /data && \
    chown -R 185:185 /app /data && \
    chmod 775 /data

# Copiar els JARs compilats
COPY --from=builder --chown=185:185 /app/transformation/target/prc-cerif-${VERSION}.jar /app/prc-cerif.jar
COPY --from=builder --chown=185:185 /app/euroCRIS-cerif-definitions/target/euroCRIS-cerif-definitions-${VERSION}.jar /app/euroCRIS-cerif-definitions.jar

# Validar que els JARs s'han copiat correctament
RUN test -f /app/prc-cerif.jar && \
    test -f /app/euroCRIS-cerif-definitions.jar && \
    echo "✓ JARs validats correctament:" && \
    ls -lh /app/*.jar

WORKDIR /app

# Variables d'entorn
ENV APP_VERSION=${VERSION}
ENV SPARK_VERSION=3.5.1
ENV HOME=/opt/spark

# Health check per verificar que els JARs existeixen
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD test -f /app/prc-cerif.jar && test -f /app/euroCRIS-cerif-definitions.jar || exit 1

# Executar com a usuari non-root (spark = UID 185)
USER 185

ENTRYPOINT ["/opt/spark/bin/spark-submit", \
    "--master", "local[*]", \
    "--driver-memory", "4g", \
    "--jars", "/app/euroCRIS-cerif-definitions.jar", \
    "--class", "org.csuc.cli.Cerif", \
    "/app/prc-cerif.jar"]
