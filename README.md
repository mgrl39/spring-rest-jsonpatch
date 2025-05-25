# 🧩 Spring Rest JSON Patch

Aquest projecte és una petita aplicació de backend construïda amb **Spring Boot**, que implementa operacions **CRUD completes** sobre una entitat `User`, i inclou suport per a **actualitzacions parcials** via **JSON Patch** (`application/json-patch+json`).

Es fa servir l'arquitectura en capes:

- `Resource` (exposa endpoints HTTP)
- `Controller` (gestiona la lògica de presentació i conversió DTO)
- `Service` (conté la lògica de negoci)
- `DAO` (accedeix a la base de dades)
- `Entity` / `DTO` (representació de dades)

---

## 🔁 Flux complet de peticions: Anada i tornada

A continuació es mostren els **diagrames de seqüència** que expliquen com viatgen les peticions HTTP pel sistema: **des del client fins a la base de dades i de tornada**.

---

### 🔍 GET – Obtenir usuaris

```mermaid
sequenceDiagram
    participant Client as Client (Postman/Browser)
    participant Resource as UserResource
    participant Controller as UserController
    participant Service as UserService
    participant DAO as UserDAO
    participant Database as PostgreSQL

    %% GET ALL USERS
    Client->>Resource: HTTP GET /api/v0/users
    Resource->>Controller: getUsers()
    Controller->>Service: getUsers()
    Service->>DAO: userRepository.findAll()
    DAO->>Database: SELECT * FROM users
    Database-->>DAO: Llista d'usuaris (entitats)
    DAO-->>Service: Llista<User>
    Service-->>Controller: Llista<User>
    Controller->>Controller: Converteix cada User a UserDto
    Controller-->>Resource: Llista<UserDto>
    Resource-->>Client: 200 OK + Llista<UserDto> (JSON)

    %% GET USER BY ID
    Client->>Resource: HTTP GET /api/v0/users/1
    Resource->>Controller: getUser(1)
    Controller->>Service: getUserById(1)
    Service->>DAO: userRepository.findById(1)
    DAO->>Database: SELECT * FROM users WHERE id=1
    Database-->>DAO: User (entitat) o null
    alt Usuari trobat
        DAO-->>Service: User
        Service-->>Controller: User
        Controller->>Controller: Converteix a UserDto
        Controller-->>Resource: UserDto
        Resource-->>Client: 200 OK + UserDto (JSON)
    else Usuari no trobat
        DAO-->>Service: null
        Service-->>Controller: null
        Controller-->>Resource: null
        Resource-->>Client: 404 Not Found
    end
```

---

### ➕ POST – Crear, ❌ DELETE – Esborrar, ✂️ PATCH – Actualitzar parcialment

```mermaid
sequenceDiagram
    participant Client as Client (Postman/Browser)
    participant Resource as UserResource
    participant Controller as UserController
    participant Service as UserService
    participant DAO as UserDAO
    participant Database as PostgreSQL

    %% POST - Crear usuari
    Client->>Resource: HTTP POST /api/v0/users
    Resource->>Controller: newUser(UserDto)
    Controller->>Controller: Converteix UserDto a User
    Controller->>Service: addUser(User)
    Service->>DAO: userRepository.save(user)
    DAO->>Database: INSERT INTO users...
    Database-->>DAO: User creat (entitat)
    DAO-->>Service: User (entitat)
    Service-->>Controller: User (entitat)
    Controller->>Controller: Converteix a UserDto
    Controller-->>Resource: UserDto
    Resource-->>Client: 200 OK + UserDto (JSON)

    %% DELETE - Eliminar usuari
    Client->>Resource: HTTP DELETE /api/v0/users/1
    Resource->>Controller: remove(1)
    Controller->>Service: deleteById(1)
    Service->>DAO: userRepository.deleteById(1)
    DAO->>Database: DELETE FROM users...
    alt Usuari existeix
        Database-->>DAO: OK
        DAO-->>Service: OK
        Service-->>Controller: OK
        Controller-->>Resource: OK
        Resource-->>Client: 200 OK
    else Usuari no trobat
        Database-->>DAO: Error
        DAO-->>Service: Error
        Service-->>Controller: Error
        Controller-->>Resource: Error
        Resource-->>Client: 404 Not Found
    end

    %% PATCH - Actualitzar parcialment
    Client->>Resource: HTTP PATCH /api/v0/users/1
    Resource->>Controller: patchUser(1, JsonPatch)
    Controller->>Service: getUserById(1)
    Service->>DAO: userRepository.findById(1)
    DAO->>Database: SELECT * FROM users...
    Database-->>DAO: User (entitat)
    DAO-->>Service: User
    Service-->>Controller: User
    Controller->>Controller: Aplica JSON Patch
    Controller->>Service: updateUser(patchedUser)
    Service->>DAO: userRepository.save(patchedUser)
    DAO->>Database: UPDATE users...
    Database-->>DAO: User actualitzat
    DAO-->>Service: User (entitat)
    Service-->>Controller: User
    Controller->>Controller: Converteix a UserDto
    Controller-->>Resource: UserDto
    Resource-->>Client: 200 OK + UserDto (JSON)
```

---

## 📌 Tecnologies utilitzades

- Java 17+
- Spring Boot 3+
- Spring Web
- Spring Data JPA
- JSON Patch
- Base de dades (PostgreSQL)
