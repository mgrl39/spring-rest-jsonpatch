# Variables de configuració
CONTAINER_NAME := postgres-container
PG_USER := usuaris_admin
PG_PASSWORD := secure_password
PG_DATABASE := usuaris

# Color pels missatges
GREEN := \033[0;32m
NC := \033[0m # No Color

# Regla per defecte (help)
.PHONY: help
help:
	@echo "$(GREEN)Makefile per a la gestió de PostgreSQL en contenidor LXC$(NC)"
	@echo ""
	@echo "Ús:"
	@echo "  make help          - Mostra aquest missatge d'ajuda"
	@echo "  make create        - Crea el contenidor LXC"
	@echo "  make install       - Instal·la PostgreSQL al contenidor"
	@echo "  make configure     - Configura PostgreSQL per a accés extern"
	@echo "  make create-db     - Crea la base de dades i l'usuari"
	@echo "  make all           - Executa tot el procés (create, install, configure, create-db)"
	@echo "  make clean         - Elimina el contenidor"
	@echo "  make shell         - Entra al contenidor"
	@echo "  make psql          - Entra al PostgreSQL"
	@echo "  make full          - Fa tota la instal·lació"
	@echo ""

# Crea el contenidor LXC
.PHONY: create
create:
	@echo "$(GREEN)Creant contenidor LXC $(CONTAINER_NAME)...$(NC)"
	@if lxc info $(CONTAINER_NAME) >/dev/null 2>&1; then \
		echo "El contenidor $(CONTAINER_NAME) ja existeix."; \
	else \
		lxc launch ubuntu:20.04 $(CONTAINER_NAME); \
		echo "Esperant que el contenidor estigui llest..."; \
		sleep 10; \
	fi

# Instal·la PostgreSQL al contenidor
.PHONY: install
install: create
	@echo "$(GREEN)Instal·lant PostgreSQL al contenidor...$(NC)"
	@lxc exec $(CONTAINER_NAME) -- bash -c "\
		apt-get update && \
		apt-get install -y postgresql postgresql-contrib && \
		systemctl enable postgresql && \
		systemctl start postgresql"

# Configura PostgreSQL per a accés extern
.PHONY: configure
configure: install
	@echo "$(GREEN)Configurant PostgreSQL per a accés extern...$(NC)"
	@lxc exec $(CONTAINER_NAME) -- bash -c "\
		PG_VERSION=\$$(ls -1 /etc/postgresql/ | head -n 1) && \
		mkdir -p /etc/postgresql/\$${PG_VERSION}/main/conf.d/ && \
		echo \"listen_addresses = '*'\" > /etc/postgresql/\$${PG_VERSION}/main/conf.d/external.conf && \
		echo \"# IPv4 local connections:\" >> /etc/postgresql/\$${PG_VERSION}/main/pg_hba.conf && \
		echo \"host    all             all             0.0.0.0/0               md5\" >> /etc/postgresql/\$${PG_VERSION}/main/pg_hba.conf && \
		systemctl restart postgresql"

# Crea la base de dades i l'usuari
.PHONY: create-db
create-db: configure
	@echo "$(GREEN)Creant base de dades i usuari...$(NC)"
	@lxc exec $(CONTAINER_NAME) -- bash -c "\
		su - postgres -c \"psql -c \\\"CREATE USER $(PG_USER) WITH PASSWORD '$(PG_PASSWORD)';\\\"\" && \
		su - postgres -c \"psql -c \\\"CREATE DATABASE $(PG_DATABASE) OWNER $(PG_USER);\\\"\" && \
		su - postgres -c \"psql -c \\\"GRANT ALL PRIVILEGES ON DATABASE $(PG_DATABASE) TO $(PG_USER);\\\"\" \
	"
	@echo "$(GREEN)Obtenint IP del contenidor:$(NC)"
	@IP=$$(lxc exec $(CONTAINER_NAME) -- ip -4 addr show eth0 | grep -oP '(?<=inet\s)\d+(\.\d+){3}') && echo "$$IP"
	@echo "$(GREEN)Base de dades '$(PG_DATABASE)' i usuari '$(PG_USER)' creats correctament.$(NC)"
	@echo "Pots connectar-te a la base de dades amb:"
	@echo "  psql -h <IP-CONTENIDOR> -U $(PG_USER) -d $(PG_DATABASE)"


# Executa tot el procés
.PHONY: all
all: create install configure create-db

# Neteja (elimina el contenidor)
.PHONY: clean
clean:
	@echo "$(GREEN)Eliminant contenidor $(CONTAINER_NAME)...$(NC)"
	@if lxc info $(CONTAINER_NAME) >/dev/null 2>&1; then \
		lxc stop $(CONTAINER_NAME) --force; \
		lxc delete $(CONTAINER_NAME); \
		echo "Contenidor eliminat."; \
	else \
		echo "El contenidor no existeix."; \
	fi

# Entra directament al contenidor amb una shell interactiva
.PHONY: shell
shell:
	@echo "$(GREEN)Accedint al contenidor $(CONTAINER_NAME)...$(NC)"
	@lxc exec $(CONTAINER_NAME) -- bash


# Accedeix directament a PostgreSQL com a usuari postgres (administrador)
.PHONY: psql
psql:
	@echo "$(GREEN)Connectant-se a PostgreSQL com a administrador...$(NC)"
	@lxc exec $(CONTAINER_NAME) -- su - postgres -c "psql"

# Instal·la i configura tot l'entorn i mostra informació detallada
.PHONY: full
full: create install configure create-db
	@echo "$(GREEN)=======================================================$(NC)"
	@echo "$(GREEN)          INSTAL·LACIÓ COMPLETA EXITOSA                $(NC)"
	@echo "$(GREEN)=======================================================$(NC)"
	@echo ""
	@echo "$(GREEN)INFORMACIÓ DE CONNEXIÓ:$(NC)"
	@echo ""
	@IP=$$(lxc exec $(CONTAINER_NAME) -- ip -4 addr show eth0 | grep -oP '(?<=inet\s)\d+(\.\d+){3}') && \
	echo "IP del contenidor: $$IP" && \
	echo "" && \
	echo "$(GREEN)Per connectar per línia de comandes:$(NC)" && \
	echo "  psql -h $$IP -U $(PG_USER) -d $(PG_DATABASE)" && \
	echo "" && \
	echo "$(GREEN)Per connectar des de Spring Boot (application.properties):$(NC)" && \
	echo "  spring.datasource.url=jdbc:postgresql://$$IP:5432/$(PG_DATABASE)" && \
	echo "  spring.datasource.username=$(PG_USER)" && \
	echo "  spring.datasource.password=$(PG_PASSWORD)" && \
	echo "" && \
	echo "$(GREEN)Per connectar utilitzant JDBC:$(NC)" && \
	echo "  URL: jdbc:postgresql://$$IP:5432/$(PG_DATABASE)" && \
	echo "  Usuari: $(PG_USER)" && \
	echo "  Contrasenya: $(PG_PASSWORD)" && \
	echo "" && \
	echo "$(GREEN)Comandes útils:$(NC)" && \
	echo "  make shell - Per entrar al contenidor" && \
	echo "  make psql  - Per entrar a PostgreSQL directament"
