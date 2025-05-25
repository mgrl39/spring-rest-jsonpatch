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
  -d '{"email": "nouemail@cat.cat", "fullName": "Nom Reemplaçat", "password": "nova"}'
```