#!/usr/bin/env bash
# Script helper per executar PRC-CSV2XML amb Docker Compose
# Ús: ./run.sh [opcions spark-submit]
#      SPARK_VERSION=4 ./run.sh [opcions spark-submit]

set -euo pipefail

# Configuració
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
COMPOSE_FILE="${SCRIPT_DIR}/docker-compose.yml"
VERSION="${SPARK_VERSION:-3.5}"
SERVICE="prc-cerif"

# Determinar quin servei utilitzar segons la versió de Spark
if [[ "$VERSION" == "4" || "$VERSION" == "4.0" || "$VERSION" == "spark4" ]]; then
    SERVICE="prc-cerif-spark4"
    PROFILE="spark4"
    echo "🚀 Executant amb Spark 4.0.2 / Java 21"
else
    SERVICE="prc-cerif"
    PROFILE="spark3"
    echo "🚀 Executant amb Spark 3.5.1 / Java 8"
fi

# Generar timestamps de build si no existeixen
export BUILD_DATE="${BUILD_DATE:-$(date -u +'%Y-%m-%dT%H:%M:%SZ')}"
if command -v git &> /dev/null && git rev-parse --git-dir > /dev/null 2>&1; then
    export VCS_REF="${VCS_REF:-$(git rev-parse --short HEAD)}"
else
    export VCS_REF="${VCS_REF:-unknown}"
fi

# Executar amb docker compose
echo "📦 Servei: $SERVICE"
echo "⚙️  Build: $BUILD_DATE (commit: $VCS_REF)"
echo ""

docker compose --profile "$PROFILE" -f "$COMPOSE_FILE" run --rm "$SERVICE" "$@"
