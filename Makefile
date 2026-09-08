.PHONY: help build build-spark4 build-all run run-spark4 test clean shell logs version

# Configuració
COMPOSE_FILE := docker/docker-compose.yml
VERSION := 2.4.19
BUILD_DATE := $(shell date -u +'%Y-%m-%dT%H:%M:%SZ')
VCS_REF := $(shell git rev-parse --short HEAD 2>/dev/null || echo "unknown")

# Directoris per defecte (poden ser sobreescrits)
INPUT_DIR := ./data
OUTPUT_DIR := ./data

# Colors per output
BLUE := \033[0;34m
GREEN := \033[0;32m
YELLOW := \033[0;33m
NC := \033[0m # No Color

help:
	@echo "$(BLUE)PRC-CSV2XML - Comandes disponibles:$(NC)"
	@echo ""
	@echo "$(GREEN)Build:$(NC)"
	@echo "  make build          - Construir imatge Spark 3.5.1 / Java 8"
	@echo "  make build-spark4   - Construir imatge Spark 4.0.2 / Java 21"
	@echo "  make build-all      - Construir ambdues imatges"
	@echo ""
	@echo "$(GREEN)Execució:$(NC)"
	@echo "  make run INPUT=entrada.xlsx OUTPUT=sortida.xml RUCT=CODE"
	@echo "                      - Executar amb Spark 3.5 (estable)"
	@echo "  make run-spark4 INPUT=entrada.xlsx OUTPUT=sortida.xml RUCT=CODE"
	@echo "                      - Executar amb Spark 4"
	@echo ""
	@echo "  $(YELLOW)Amb paths personalitzats:$(NC)"
	@echo "  make run INPUT_DIR=/tmp INPUT=entrada.xlsx \\"
	@echo "           OUTPUT_DIR=~/Baixades OUTPUT=sortida.xml RUCT=CODE"
	@echo ""
	@echo "$(GREEN)Desenvolupament:$(NC)"
	@echo "  make shell          - Shell interactiu al contenidor (Spark 3.5)"
	@echo "  make test           - Executar tests"
	@echo "  make logs           - Veure logs dels contenidors"
	@echo "  make version        - Mostrar versions i info de build"
	@echo ""
	@echo "$(GREEN)Neteja:$(NC)"
	@echo "  make clean          - Netejar imatges i contenidors"
	@echo ""
	@echo "$(YELLOW)Exemples:$(NC)"
	@echo "  make build"
	@echo "  make run INPUT=plantilla_PRC_CREAF.xlsx OUTPUT=resultat.xml RUCT=CODE123"
	@echo "  make run INPUT_DIR=/tmp INPUT=test.xlsx OUTPUT_DIR=~/Baixades OUTPUT=out.xml RUCT=CODE"

build:
	@echo "$(BLUE)🔨 Construint imatge Spark 3.5.1 / Java 8...$(NC)"
	VERSION=$(VERSION) BUILD_DATE=$(BUILD_DATE) VCS_REF=$(VCS_REF) \
		docker compose --profile spark3 -f $(COMPOSE_FILE) build prc-cerif
	@echo "$(GREEN)✓ Imatge construïda: prc-cerif:$(VERSION)$(NC)"

build-spark4:
	@echo "$(BLUE)🔨 Construint imatge Spark 4.0.2 / Java 21...$(NC)"
	VERSION=$(VERSION) BUILD_DATE=$(BUILD_DATE) VCS_REF=$(VCS_REF) \
		docker compose --profile spark4 -f $(COMPOSE_FILE) build prc-cerif-spark4
	@echo "$(GREEN)✓ Imatge construïda: prc-cerif:spark4-$(VERSION)$(NC)"

build-all: build build-spark4
	@echo "$(GREEN)✓ Totes les imatges construïdes$(NC)"

run:
	@test -n "$(INPUT)" || (echo "$(YELLOW)ERROR: INPUT no definit.$(NC)" && \
		echo "Ús: make run INPUT=fitxer.xlsx OUTPUT=sortida.xml RUCT=CODE" && exit 1)
	@test -n "$(OUTPUT)" || (echo "$(YELLOW)ERROR: OUTPUT no definit.$(NC)" && exit 1)
	@test -n "$(RUCT)" || (echo "$(YELLOW)ERROR: RUCT no definit.$(NC)" && exit 1)
	@echo "$(BLUE)🚀 Executant conversió amb Spark 3.5.1...$(NC)"
	@echo "   Input:  $(INPUT_DIR)/$(INPUT)"
	@echo "   Output: $(OUTPUT_DIR)/$(OUTPUT)"
	@echo "   RUCT:   $(RUCT)"
	@echo ""
	@# Expandir ~ i obtenir paths absoluts per als volums
	$(eval INPUT_DIR_EXPANDED := $(shell echo $(INPUT_DIR)))
	$(eval OUTPUT_DIR_EXPANDED := $(shell echo $(OUTPUT_DIR)))
	$(eval INPUT_DIR_ABS := $(abspath $(INPUT_DIR_EXPANDED)))
	$(eval OUTPUT_DIR_ABS := $(abspath $(OUTPUT_DIR_EXPANDED)))
	@# Detectar si s'usen paths personalitzats
	@if [ "$(INPUT_DIR)" != "./data" ] || [ "$(OUTPUT_DIR)" != "./data" ]; then \
		echo "$(YELLOW)📁 Usant paths personalitzats...$(NC)"; \
		VOLUME_ARGS=""; \
		if [ "$(INPUT_DIR_ABS)" = "$(OUTPUT_DIR_ABS)" ]; then \
			VOLUME_ARGS="-v $(INPUT_DIR_ABS):/data"; \
			INPUT_PATH="/data/$(INPUT)"; \
			OUTPUT_PATH="/data/$(OUTPUT)"; \
		else \
			VOLUME_ARGS="-v $(INPUT_DIR_ABS):/input:ro -v $(OUTPUT_DIR_ABS):/output"; \
			INPUT_PATH="/input/$(INPUT)"; \
			OUTPUT_PATH="/output/$(OUTPUT)"; \
		fi; \
		VERSION=$(VERSION) BUILD_DATE=$(BUILD_DATE) VCS_REF=$(VCS_REF) \
			docker compose --profile spark3 -f $(COMPOSE_FILE) run --rm \
				$$VOLUME_ARGS \
				prc-cerif \
				--input $$INPUT_PATH \
				--output $$OUTPUT_PATH \
				--ruct $(RUCT) \
				$(if $(FORMATTED),--formatted); \
	else \
		VERSION=$(VERSION) BUILD_DATE=$(BUILD_DATE) VCS_REF=$(VCS_REF) \
			docker compose --profile spark3 -f $(COMPOSE_FILE) run --rm prc-cerif \
				--input /data/$(INPUT) \
				--output /data/$(OUTPUT) \
				--ruct $(RUCT) \
				$(if $(FORMATTED),--formatted); \
	fi
	@echo ""
	@echo "$(GREEN)✓ Conversió completada!$(NC)"

run-spark4:
	@test -n "$(INPUT)" || (echo "$(YELLOW)ERROR: INPUT no definit.$(NC)" && \
		echo "Ús: make run-spark4 INPUT=fitxer.xlsx OUTPUT=sortida.xml RUCT=CODE" && exit 1)
	@test -n "$(OUTPUT)" || (echo "$(YELLOW)ERROR: OUTPUT no definit.$(NC)" && exit 1)
	@test -n "$(RUCT)" || (echo "$(YELLOW)ERROR: RUCT no definit.$(NC)" && exit 1)
	@echo "$(BLUE)🚀 Executant conversió amb Spark 4.0.2...$(NC)"
	@echo "   Input:  $(INPUT_DIR)/$(INPUT)"
	@echo "   Output: $(OUTPUT_DIR)/$(OUTPUT)"
	@echo "   RUCT:   $(RUCT)"
	@echo ""
	@# Expandir ~ i obtenir paths absoluts per als volums
	$(eval INPUT_DIR_EXPANDED := $(shell echo $(INPUT_DIR)))
	$(eval OUTPUT_DIR_EXPANDED := $(shell echo $(OUTPUT_DIR)))
	$(eval INPUT_DIR_ABS := $(abspath $(INPUT_DIR_EXPANDED)))
	$(eval OUTPUT_DIR_ABS := $(abspath $(OUTPUT_DIR_EXPANDED)))
	@# Detectar si s'usen paths personalitzats
	@if [ "$(INPUT_DIR)" != "./data" ] || [ "$(OUTPUT_DIR)" != "./data" ]; then \
		echo "$(YELLOW)📁 Usant paths personalitzats...$(NC)"; \
		VOLUME_ARGS=""; \
		if [ "$(INPUT_DIR_ABS)" = "$(OUTPUT_DIR_ABS)" ]; then \
			VOLUME_ARGS="-v $(INPUT_DIR_ABS):/data"; \
			INPUT_PATH="/data/$(INPUT)"; \
			OUTPUT_PATH="/data/$(OUTPUT)"; \
		else \
			VOLUME_ARGS="-v $(INPUT_DIR_ABS):/input:ro -v $(OUTPUT_DIR_ABS):/output"; \
			INPUT_PATH="/input/$(INPUT)"; \
			OUTPUT_PATH="/output/$(OUTPUT)"; \
		fi; \
		VERSION=$(VERSION) BUILD_DATE=$(BUILD_DATE) VCS_REF=$(VCS_REF) \
			docker compose --profile spark4 -f $(COMPOSE_FILE) run --rm \
				$$VOLUME_ARGS \
				prc-cerif-spark4 \
				--input $$INPUT_PATH \
				--output $$OUTPUT_PATH \
				--ruct $(RUCT) \
				$(if $(FORMATTED),--formatted); \
	else \
		VERSION=$(VERSION) BUILD_DATE=$(BUILD_DATE) VCS_REF=$(VCS_REF) \
			docker compose --profile spark4 -f $(COMPOSE_FILE) run --rm prc-cerif-spark4 \
				--input /data/$(INPUT) \
				--output /data/$(OUTPUT) \
				--ruct $(RUCT) \
				$(if $(FORMATTED),--formatted); \
	fi
	@echo ""
	@echo "$(GREEN)✓ Conversió completada!$(NC)"

test:
	@echo "$(BLUE)🧪 Executant tests amb Spark 3.5.1...$(NC)"
	VERSION=$(VERSION) BUILD_DATE=$(BUILD_DATE) VCS_REF=$(VCS_REF) \
		docker compose --profile spark3 -f $(COMPOSE_FILE) build --target tester prc-cerif
	@echo "$(GREEN)✓ Tests completats$(NC)"

shell:
	@echo "$(BLUE)🐚 Shell interactiu al contenidor (Spark 3.5.1)...$(NC)"
	VERSION=$(VERSION) BUILD_DATE=$(BUILD_DATE) VCS_REF=$(VCS_REF) \
		docker compose --profile spark3 -f $(COMPOSE_FILE) run --rm --entrypoint /bin/bash prc-cerif

logs:
	@echo "$(BLUE)📋 Logs dels contenidors...$(NC)"
	docker compose -f $(COMPOSE_FILE) logs -f

version:
	@echo "$(BLUE)📦 PRC-CSV2XML - Informació de build:$(NC)"
	@echo ""
	@echo "  Versió:        $(VERSION)"
	@echo "  Build date:    $(BUILD_DATE)"
	@echo "  Git commit:    $(VCS_REF)"
	@echo ""
	@echo "$(BLUE)Imatges disponibles:$(NC)"
	@docker images | grep prc-cerif || echo "  $(YELLOW)Cap imatge construïda$(NC)"

clean:
	@echo "$(YELLOW)🧹 Netejant contenidors i imatges...$(NC)"
	docker compose -f $(COMPOSE_FILE) down -v 2>/dev/null || true
	docker rmi prc-cerif:$(VERSION) 2>/dev/null || true
	docker rmi prc-cerif:spark4-$(VERSION) 2>/dev/null || true
	docker rmi prc-cerif:latest 2>/dev/null || true
	docker rmi prc-cerif:spark4-latest 2>/dev/null || true
	@echo "$(GREEN)✓ Neteja completada$(NC)"
