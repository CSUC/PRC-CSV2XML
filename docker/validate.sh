#!/usr/bin/env bash
# Script de validació per verificar la configuració Docker
# Executa checks de sanitat i tests bàsics

set -euo pipefail

# Configuració
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
COMPOSE_FILE="${SCRIPT_DIR}/docker-compose.yml"

# Colors
BLUE='\033[0;34m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
RED='\033[0;31m'
NC='\033[0m'

# Comptadors
PASSED=0
FAILED=0
WARNINGS=0

# Funcions
info() { echo -e "${BLUE}ℹ${NC} $*"; }
success() { echo -e "${GREEN}✓${NC} $*"; ((PASSED++)); }
warning() { echo -e "${YELLOW}⚠${NC} $*"; ((WARNINGS++)); }
error() { echo -e "${RED}✗${NC} $*"; ((FAILED++)); }

check() {
    local description="$1"
    shift
    
    echo -n "  Verificant $description... "
    
    if "$@" &> /dev/null; then
        echo -e "${GREEN}✓${NC}"
        ((PASSED++))
        return 0
    else
        echo -e "${RED}✗${NC}"
        ((FAILED++))
        return 1
    fi
}

# Checks de sistema
check_system() {
    info "1. Sistema i requisits"
    
    check "Docker instal·lat" command -v docker
    check "Docker Compose v2" docker compose version
    check "Git disponible" command -v git
    check "Make disponible" command -v make
    
    # Docker version
    local docker_version=$(docker --version | grep -oP '\d+\.\d+' | head -1 || echo "0")
    if (( $(echo "$docker_version >= 24.0" | bc -l 2>/dev/null || echo 0) )); then
        success "  Docker version >= 24.0 ($docker_version)"
    else
        warning "  Docker version < 24.0 ($docker_version), BuildKit pot no funcionar correctament"
    fi
    
    # BuildKit
    if [ "${DOCKER_BUILDKIT:-0}" = "1" ] || docker buildx version &> /dev/null; then
        success "  BuildKit disponible"
    else
        warning "  BuildKit no habilitat (export DOCKER_BUILDKIT=1)"
    fi
    
    echo ""
}

# Checks de fitxers
check_files() {
    info "2. Fitxers de configuració"
    
    check "Dockerfile" test -f "$SCRIPT_DIR/Dockerfile"
    check "Dockerfile.spark4" test -f "$SCRIPT_DIR/Dockerfile.spark4"
    check "docker-compose.yml" test -f "$SCRIPT_DIR/docker-compose.yml"
    check ".dockerignore" test -f "$SCRIPT_DIR/../.dockerignore"
    check "run.sh" test -x "$SCRIPT_DIR/run.sh"
    check "build.sh" test -x "$SCRIPT_DIR/build.sh"
    check "Makefile" test -f "$SCRIPT_DIR/../Makefile"
    check "README.md" test -f "$SCRIPT_DIR/README.md"
    
    echo ""
}

# Checks de sintaxi
check_syntax() {
    info "3. Validació de sintaxi"
    
    # Docker Compose
    if docker compose -f "$COMPOSE_FILE" config > /dev/null 2>&1; then
        success "  docker-compose.yml sintaxi correcta"
    else
        error "  docker-compose.yml té errors de sintaxi"
    fi
    
    # Dockerfiles
    if docker build -f "$SCRIPT_DIR/Dockerfile" --target deps --dry-run "$SCRIPT_DIR/.." &> /dev/null; then
        success "  Dockerfile sintaxi correcta"
    else
        warning "  Dockerfile pot tenir errors (dry-run no disponible en aquesta versió)"
    fi
    
    # Scripts bash
    for script in run.sh build.sh validate.sh; do
        if [ -f "$SCRIPT_DIR/$script" ]; then
            if bash -n "$SCRIPT_DIR/$script" 2>/dev/null; then
                success "  $script sintaxi correcta"
            else
                error "  $script té errors de sintaxi"
            fi
        fi
    done
    
    echo ""
}

# Checks de profiles
check_profiles() {
    info "4. Profiles Docker Compose"
    
    local config=$(docker compose -f "$COMPOSE_FILE" config 2>/dev/null || echo "")
    
    if echo "$config" | grep -q "profiles:"; then
        success "  Profiles definits"
        
        if echo "$config" | grep -q "spark3"; then
            success "  Profile 'spark3' trobat"
        else
            warning "  Profile 'spark3' no trobat"
        fi
        
        if echo "$config" | grep -q "spark4"; then
            success "  Profile 'spark4' trobat"
        else
            warning "  Profile 'spark4' no trobat"
        fi
    else
        warning "  Profiles no definits al docker-compose.yml"
    fi
    
    echo ""
}

# Checks d'imatges
check_images() {
    info "5. Imatges Docker"
    
    local has_spark3=0
    local has_spark4=0
    
    if docker image inspect prc-cerif:latest &> /dev/null || docker image inspect prc-cerif:2.4.19 &> /dev/null; then
        success "  Imatge Spark 3.5 disponible"
        has_spark3=1
        
        # Verificar labels
        local labels=$(docker image inspect prc-cerif:latest --format='{{json .Config.Labels}}' 2>/dev/null || echo "{}")
        if echo "$labels" | grep -q "org.opencontainers.image.version"; then
            success "    Labels OCI presents"
        else
            warning "    Labels OCI no trobades"
        fi
        
        # Verificar health check
        local healthcheck=$(docker image inspect prc-cerif:latest --format='{{json .Config.Healthcheck}}' 2>/dev/null || echo "null")
        if [ "$healthcheck" != "null" ]; then
            success "    Health check configurat"
        else
            warning "    Health check no configurat"
        fi
    else
        warning "  Imatge Spark 3.5 no construïda (executa: make build)"
    fi
    
    if docker image inspect prc-cerif:spark4-latest &> /dev/null || docker image inspect prc-cerif:spark4-2.4.19 &> /dev/null; then
        success "  Imatge Spark 4 disponible"
        has_spark4=1
    else
        warning "  Imatge Spark 4 no construïda (executa: make build-spark4)"
    fi
    
    if [ $has_spark3 -eq 0 ] && [ $has_spark4 -eq 0 ]; then
        warning "  Cap imatge construïda. Executa 'make build' primer"
    fi
    
    echo ""
}

# Checks de configuració
check_config() {
    info "6. Configuració runtime"
    
    local config=$(docker compose -f "$COMPOSE_FILE" config 2>/dev/null || echo "")
    
    # Resources
    if echo "$config" | grep -q "cpus:"; then
        success "  Límits de CPU configurats"
    else
        warning "  Límits de CPU no configurats"
    fi
    
    if echo "$config" | grep -q "memory:"; then
        success "  Límits de memòria configurats"
    else
        warning "  Límits de memòria no configurats"
    fi
    
    # Logging
    if echo "$config" | grep -q "max-size:"; then
        success "  Rotació de logs configurada"
    else
        warning "  Rotació de logs no configurada"
    fi
    
    # Restart policy
    if echo "$config" | grep -q "restart_policy:"; then
        success "  Restart policy configurat"
    else
        warning "  Restart policy no configurat"
    fi
    
    # Health check
    if echo "$config" | grep -q "healthcheck:"; then
        success "  Health checks configurats"
    else
        warning "  Health checks no configurats"
    fi
    
    echo ""
}

# Test bàsic
test_basic() {
    info "7. Test bàsic de funcionament"
    
    local image=""
    if docker image inspect prc-cerif:latest &> /dev/null; then
        image="prc-cerif:latest"
    elif docker image inspect prc-cerif:2.4.19 &> /dev/null; then
        image="prc-cerif:2.4.19"
    else
        warning "  No es pot executar test: cap imatge disponible"
        echo ""
        return
    fi
    
    # Test --help
    if docker run --rm "$image" --help &> /dev/null; then
        success "  Contenidor executa correctament (--help)"
    else
        error "  Error executant contenidor"
    fi
    
    # Test --version
    if docker run --rm "$image" --version 2>&1 | grep -q "2.4"; then
        success "  Versió reportada correctament"
    else
        warning "  Versió no es pot verificar"
    fi
    
    echo ""
}

# Resum final
show_summary() {
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    info "RESUM DE VALIDACIÓ"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    
    echo -e "${GREEN}✓ Passat:${NC}    $PASSED checks"
    echo -e "${YELLOW}⚠ Warnings:${NC}  $WARNINGS checks"
    echo -e "${RED}✗ Fallat:${NC}    $FAILED checks"
    echo ""
    
    local total=$((PASSED + WARNINGS + FAILED))
    local success_rate=$((PASSED * 100 / total))
    
    if [ $FAILED -eq 0 ]; then
        if [ $WARNINGS -eq 0 ]; then
            success "🎉 Configuració perfecta! Tots els checks passat ($success_rate%)"
        else
            success "✓ Configuració correcta amb alguns warnings ($success_rate%)"
        fi
        echo ""
        info "Pots construir les imatges amb:"
        echo "  make build"
        echo "  make build-spark4"
        return 0
    else
        error "❌ Hi ha errors en la configuració ($success_rate% èxit)"
        echo ""
        info "Revisa els errors anteriors i torna a executar aquest script"
        return 1
    fi
}

# Main
main() {
    echo ""
    info "🔍 Validació de Configuració Docker - PRC-CSV2XML"
    echo ""
    
    check_system
    check_files
    check_syntax
    check_profiles
    check_images
    check_config
    test_basic
    
    show_summary
}

# Executar
main "$@"
