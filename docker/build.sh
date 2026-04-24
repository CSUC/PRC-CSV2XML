#!/usr/bin/env bash
# Script per construir les imatges Docker amb BuildKit i metadades completes
# Ús: ./build.sh [spark3|spark4|all]

set -euo pipefail

# Configuració
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
COMPOSE_FILE="${SCRIPT_DIR}/docker-compose.yml"
VERSION="${VERSION:-2.4.19}"

# Colors per output
BLUE='\033[0;34m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Funcions helpers
info() {
    echo -e "${BLUE}ℹ${NC} $*"
}

success() {
    echo -e "${GREEN}✓${NC} $*"
}

warning() {
    echo -e "${YELLOW}⚠${NC} $*"
}

error() {
    echo -e "${RED}✗${NC} $*"
    exit 1
}

# Generar metadades de build
generate_metadata() {
    export BUILD_DATE=$(date -u +'%Y-%m-%dT%H:%M:%SZ')
    
    if command -v git &> /dev/null && git rev-parse --git-dir > /dev/null 2>&1; then
        export VCS_REF=$(git rev-parse --short HEAD)
        local branch=$(git rev-parse --abbrev-ref HEAD)
        local status=$(git status --porcelain | wc -l)
        
        info "Build metadata generada:"
        echo "  • Versió:      $VERSION"
        echo "  • Build date:  $BUILD_DATE"
        echo "  • Git commit:  $VCS_REF"
        echo "  • Branch:      $branch"
        
        if [ "$status" -gt 0 ]; then
            warning "Hi ha canvis sense commit ($status fitxers modificats)"
        fi
    else
        export VCS_REF="unknown"
        warning "Git no disponible, VCS_REF = unknown"
    fi
    
    echo ""
}

# Verificar requisits
check_requirements() {
    info "Verificant requisits..."
    
    # Docker
    if ! command -v docker &> /dev/null; then
        error "Docker no trobat. Instal·la Docker primer: https://docs.docker.com/get-docker/"
    fi
    
    local docker_version=$(docker --version | grep -oP '\d+\.\d+' | head -1)
    info "Docker version: $docker_version"
    
    # Docker Compose
    if ! docker compose version &> /dev/null; then
        error "Docker Compose v2 no trobat. Actualitza Docker a la versió 24+"
    fi
    
    # BuildKit
    if [ -z "${DOCKER_BUILDKIT:-}" ]; then
        export DOCKER_BUILDKIT=1
        info "BuildKit habilitat automàticament"
    fi
    
    success "Tots els requisits complerts"
    echo ""
}

# Construir imatge Spark 3.5
build_spark3() {
    info "🔨 Construint imatge Spark 3.5.1 / Java 8..."
    
    DOCKER_BUILDKIT=1 VERSION=$VERSION BUILD_DATE=$BUILD_DATE VCS_REF=$VCS_REF \
        docker compose --profile spark3 -f "$COMPOSE_FILE" build prc-cerif
    
    if [ $? -eq 0 ]; then
        success "Imatge Spark 3.5 construïda correctament"
        docker images | grep prc-cerif | grep -v spark4 | head -1
    else
        error "Error construint imatge Spark 3.5"
    fi
    
    echo ""
}

# Construir imatge Spark 4
build_spark4() {
    info "🔨 Construint imatge Spark 4.0.2 / Java 21..."
    
    DOCKER_BUILDKIT=1 VERSION=$VERSION BUILD_DATE=$BUILD_DATE VCS_REF=$VCS_REF \
        docker compose --profile spark4 -f "$COMPOSE_FILE" build prc-cerif-spark4
    
    if [ $? -eq 0 ]; then
        success "Imatge Spark 4 construïda correctament"
        docker images | grep prc-cerif | grep spark4 | head -1
    else
        error "Error construint imatge Spark 4"
    fi
    
    echo ""
}

# Validar imatges
validate_images() {
    info "🔍 Validant imatges construïdes..."
    
    local errors=0
    
    # Validar Spark 3.5
    if docker image inspect prc-cerif:${VERSION} &> /dev/null || docker image inspect prc-cerif:latest &> /dev/null; then
        success "Imatge Spark 3.5 trobada"
        
        # Verificar labels
        local version_label=$(docker image inspect prc-cerif:latest --format='{{index .Config.Labels "org.opencontainers.image.version"}}' 2>/dev/null || echo "")
        if [ -n "$version_label" ]; then
            info "  Labels OCI: ✓"
        else
            warning "  Labels OCI no trobades"
        fi
    else
        warning "Imatge Spark 3.5 no trobada"
        ((errors++))
    fi
    
    # Validar Spark 4 si s'ha construït
    if docker image inspect prc-cerif:spark4-${VERSION} &> /dev/null || docker image inspect prc-cerif:spark4-latest &> /dev/null; then
        success "Imatge Spark 4 trobada"
    fi
    
    echo ""
    
    if [ $errors -eq 0 ]; then
        success "Totes les imatges validades correctament"
    else
        warning "$errors error(s) de validació"
    fi
    
    echo ""
}

# Mostrar informació final
show_info() {
    info "📦 Imatges disponibles:"
    docker images | grep -E "REPOSITORY|prc-cerif" || echo "  Cap imatge trobada"
    
    echo ""
    info "🚀 Ús:"
    echo "  make run INPUT=entrada.xlsx OUTPUT=sortida.xml RUCT=CODE123"
    echo "  ./docker/run.sh --input /data/entrada.xlsx --output /data/sortida.xml --ruct CODE123"
    echo ""
}

# Main
main() {
    local target="${1:-spark3}"
    
    echo ""
    info "🐳 PRC-CSV2XML Docker Build Script"
    echo ""
    
    check_requirements
    generate_metadata
    
    case "$target" in
        spark3|3|3.5)
            build_spark3
            validate_images
            ;;
        spark4|4|4.0)
            build_spark4
            validate_images
            ;;
        all|both)
            build_spark3
            build_spark4
            validate_images
            ;;
        *)
            error "Target desconegut: $target. Ús: ./build.sh [spark3|spark4|all]"
            ;;
    esac
    
    show_info
    success "Build completada amb èxit! 🎉"
}

# Executar
main "$@"
