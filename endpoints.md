# Endpoints

## 1. Obtenir tots els usuaris:

```bash
curl -X GET http://localhost:8080/api/v0/users
```

## 2. Obtenir un usuari per ID

```bash
curl -X GET http://localhost:8080/api/v0/users/1
```

## 3. Crear un usuari nou

```bash
curl -X POST http://localhost:8080/api/v0/users \
  -H "Content-Type: application/json" \
  -d '{
    "id": 4,
    "email": "pere@example.cat",
    "fullName": "Pere Lluch",
    "password": "contrasenyaSegura"
  }'
```

## 4. Eliminar un usuari per ID

```bash
curl -X DELETE http://localhost:8080/api/v0/users/4
```

## 5. Reemplaçar usuari per ID

```bash
curl -X PUT http://localhost:8080/api/v0/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "email": "nouemail@cat.cat",
    "fullName": "Nom Reemplaçat",
    "password": "nova"
  }'
```

## 6. Actualitzar parcialment un usuari (JSON Patch)

```bash
curl -X PATCH http://localhost:8080/api/v0/users/1 \
  -H "Content-Type: application/json-patch+json" \
  -d '[
    { "op": "replace", "path": "/email", "value": "nou@email.cat" }
  ]'
```

### Exemples addicionals de JSON Patch

#### Actualitzar múltiples camps

```bash
curl -X PATCH http://localhost:8080/api/v0/users/1 \
  -H "Content-Type: application/json-patch+json" \
  -d '[
    { "op": "replace", "path": "/email", "value": "nou@email.cat" },
    { "op": "replace", "path": "/fullName", "value": "Nou Nom Complet" }
  ]'
```

#### Actualitzar només la contrasenya

```bash
curl -X PATCH http://localhost:8080/api/v0/users/1 \
  -H "Content-Type: application/json-patch+json" \
  -d '[
    { "op": "replace", "path": "/password", "value": "novaContrasenya" }
  ]'
```

### Notes sobre JSON Patch

- El format ha de ser `application/json-patch+json`
- Les operacions utilitzades:
  - `replace`: Reemplaça el valor d'una propietat
- Els paths disponibles en aquest cas són:
  - `/email`: Per modificar el correu electrònic
  - `/fullName`: Per modificar el nom complet
  - `/password`: Per modificar la contrasenya
