# Variables de configuración
CONTAINER_NAME := postgres-container
PG_USER := usuaris_admin
PG_PASSWORD := secure_password
PG_DATABASE := usuaris

# Color para los mensajes
GREEN := \033[0;32m
NC := \033[0m # No Color

# Regla por defecto (help)
.PHONY: help
help:
	@echo "$(GREEN)Makefile para gestión de PostgreSQL en contenedor LXC$(NC)"
	@echo ""
	@echo "Uso:"
	@echo "  make help          - Muestra este mensaje de ayuda"
	@echo "  make create        - Crea el contenedor LXC"
	@echo "  make install       - Instala PostgreSQL en el contenedor"
	@echo "  make configure     - Configura PostgreSQL para acceso externo"
	@echo "  make create-db     - Crea la base de datos y usuario"
	@echo "  make all           - Ejecuta todo el proceso (create, install, configure, create-db)"
	@echo "  make clean         - Elimina el contenedor"
	@echo "  make shell         - Entra al contenedor"
	@echo "  make psql          - Entra al postgres"
	@echo "  make full          - Fa tota la instal·lació"
	@echo ""

# Crea el contenedor LXC
.PHONY: create
create:
	@echo "$(GREEN)Creando contenedor LXC $(CONTAINER_NAME)...$(NC)"
	@if lxc info $(CONTAINER_NAME) >/dev/null 2>&1; then \
		echo "El contenedor $(CONTAINER_NAME) ya existe."; \
	else \
		lxc launch ubuntu:20.04 $(CONTAINER_NAME); \
		echo "Esperando a que el contenedor esté listo..."; \
		sleep 10; \
	fi

# Instala PostgreSQL en el contenedor
.PHONY: install
install: create
	@echo "$(GREEN)Instalando PostgreSQL en el contenedor...$(NC)"
	@lxc exec $(CONTAINER_NAME) -- bash -c "\
		apt-get update && \
		apt-get install -y postgresql postgresql-contrib && \
		systemctl enable postgresql && \
		systemctl start postgresql"

# Configura PostgreSQL para acceso externo
.PHONY: configure
configure: install
	@echo "$(GREEN)Configurando PostgreSQL para acceso externo...$(NC)"
	@lxc exec $(CONTAINER_NAME) -- bash -c "\
		PG_VERSION=\$$(ls -1 /etc/postgresql/ | head -n 1) && \
		mkdir -p /etc/postgresql/\$${PG_VERSION}/main/conf.d/ && \
		echo \"listen_addresses = '*'\" > /etc/postgresql/\$${PG_VERSION}/main/conf.d/external.conf && \
		echo \"# IPv4 local connections:\" >> /etc/postgresql/\$${PG_VERSION}/main/pg_hba.conf && \
		echo \"host    all             all             0.0.0.0/0               md5\" >> /etc/postgresql/\$${PG_VERSION}/main/pg_hba.conf && \
		systemctl restart postgresql"

# Crea la base de datos y el usuario
.PHONY: create-db
create-db: configure
	@echo "$(GREEN)Creando base de datos y usuario...$(NC)"
	@lxc exec $(CONTAINER_NAME) -- bash -c "\
		su - postgres -c \"psql -c \\\"CREATE USER $(PG_USER) WITH PASSWORD '$(PG_PASSWORD)';\\\"\" && \
		su - postgres -c \"psql -c \\\"CREATE DATABASE $(PG_DATABASE) OWNER $(PG_USER);\\\"\" && \
		su - postgres -c \"psql -c \\\"GRANT ALL PRIVILEGES ON DATABASE $(PG_DATABASE) TO $(PG_USER);\\\"\" \
	"
	@echo "$(GREEN)Obtener IP del contenedor:$(NC)"
	@lxc list $(CONTAINER_NAME) -c 4 | grep -v IPV4 | tr -d "[:blank:]"
	@echo "$(GREEN)Base de datos '$(PG_DATABASE)' y usuario '$(PG_USER)' creados correctamente.$(NC)"
	@echo "Puedes conectarte a la base de datos con:"
	@echo "  psql -h <IP-CONTENEDOR> -U $(PG_USER) -d $(PG_DATABASE)"

# Ejecuta todo el proceso
.PHONY: all
all: create install configure create-db

# Limpia (elimina el contenedor)
.PHONY: clean
clean:
	@echo "$(GREEN)Eliminando contenedor $(CONTAINER_NAME)...$(NC)"
	@if lxc info $(CONTAINER_NAME) >/dev/null 2>&1; then \
		lxc stop $(CONTAINER_NAME) --force; \
		lxc delete $(CONTAINER_NAME); \
		echo "Contenedor eliminado."; \
	else \
		echo "El contenedor no existe."; \
	fi

# Entra directamente al contenedor con una shell interactiva
.PHONY: shell
shell:
	@echo "$(GREEN)Accediendo al contenedor $(CONTAINER_NAME)...$(NC)"
	@lxc exec $(CONTAINER_NAME) -- bash


# Accede directamente a PostgreSQL como usuario postgres (administrador)
.PHONY: psql
psql:
	@echo "$(GREEN)Conectándose a PostgreSQL como administrador...$(NC)"
	@lxc exec $(CONTAINER_NAME) -- su - postgres -c "psql"

# Instala y configura todo el entorno y muestra información detallada
.PHONY: full
full: create install configure create-db
	@echo "$(GREEN)=======================================================$(NC)"
	@echo "$(GREEN)          INSTALACIÓN COMPLETA EXITOSA                 $(NC)"
	@echo "$(GREEN)=======================================================$(NC)"
	@echo ""
	@echo "$(GREEN)INFORMACIÓN DE CONEXIÓN:$(NC)"
	@echo ""
	@IP=$$(lxc list $(CONTAINER_NAME) -c 4 | grep -v IPV4 | tr -d "[:blank:]") && \
	echo "IP del contenedor: $$IP" && \
	echo "" && \
	echo "$(GREEN)Para conectar por línea de comandos:$(NC)" && \
	echo "  psql -h $$IP -U $(PG_USER) -d $(PG_DATABASE)" && \
	echo "" && \
	echo "$(GREEN)Para conectar desde Spring Boot (application.properties):$(NC)" && \
	echo "  spring.datasource.url=jdbc:postgresql://$$IP:5432/$(PG_DATABASE)" && \
	echo "  spring.datasource.username=$(PG_USER)" && \
	echo "  spring.datasource.password=$(PG_PASSWORD)" && \
	echo "" && \
	echo "$(GREEN)Para conectar usando JDBC:$(NC)" && \
	echo "  URL: jdbc:postgresql://$$IP:5432/$(PG_DATABASE)" && \
	echo "  Usuario: $(PG_USER)" && \
	echo "  Contraseña: $(PG_PASSWORD)" && \
	echo "" && \
	echo "$(GREEN)Comandos útiles:$(NC)" && \
	echo "  make shell - Para entrar al contenedor" && \
	echo "  make psql  - Para entrar a PostgreSQL directamente"
