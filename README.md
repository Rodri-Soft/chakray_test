# API Chakray_test 
## José Rodrigo Sánchez Méndez

API Spring con Java 17 - prueba técnica

## Tecnologías utilizadas
- Java 17
- Spring Boot
- Maven
- Base de datos: MySQL
- Otras librerías: Lombok, Hibernate 

## Requisitos
- Java 17 instalado
- Maven
- MySQL

## Configuración
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Rodri-Soft/chakray_test
   cd chakray_test
   
2. Crear un cliente de MySQL o bien crear un contenedor de docker:
    - docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=mi_password -p 3306:3306 -d mysql:latest

3. Crear esquema:
   - CREATE DATABASE users_chakray;

4. Dado que se tiene habilitada la opción de actualización en cascada de nuestras entidades JPA, no será necesario crear las tablas, no obstante, dejo un ejemplo de algunos datos
   para poblarlas:
   - INSERT INTO users (id, email, name, phone, password, tax_id, created_at)
VALUES 
('6d6c0e64-995e-11f0-b8d1-0242ac110002', 'user1@mail.com', 'User One', '+15555555555', AES_ENCRYPT('password123', '4f2b9c8d7e6f1a0b3c5d8e9f0a1b2c3d'), 'AARR990101XXX', NOW()),
('6d6dea30-995e-11f0-b8d1-0242ac110002', 'user2@mail.com', 'User Two', '+15555555556', AES_ENCRYPT('password456', '4f2b9c8d7e6f1a0b3c5d8e9f0a1b2c3d'), 'BBOB990202YYY', NOW()),
('6d6df921-995e-11f0-b8d1-0242ac110002', 'user3@mail.com', 'User Three', '+15555555557', AES_ENCRYPT('password789', '4f2b9c8d7e6f1a0b3c5d8e9f0a1b2c3d'), 'CCDD990303ZZZ', NOW());


  - INSERT INTO addresses (user_id, name, street, country_code)
VALUES
('6d6c0e64-995e-11f0-b8d1-0242ac110002', 'Work Address', 'Street 1', 'US'),
('6d6c0e64-995e-11f0-b8d1-0242ac110002', 'Home Address', 'Street 2', 'US'),
('6d6dea30-995e-11f0-b8d1-0242ac110002', 'Work Address', 'Street 3', 'UK'),
('6d6dea30-995e-11f0-b8d1-0242ac110002', 'Home Address', 'Street 4', 'UK'),
('6d6df921-995e-11f0-b8d1-0242ac110002', 'Work Address', 'Street 5', 'AU'),
('6d6df921-995e-11f0-b8d1-0242ac110002', 'Home Address', 'Street 6', 'AU');

5. Para ejecutar los endpoints se puede acceder a la ruta:
  - http://localhost:8080/swagger-ui/index.html

  - También se tiene acceso al workspace dentro de postman con el siguiente link:
     - https://rodrisoftteam.postman.co/workspace/ChakRayWorkspace~570a1ab4-2f28-4cf8-9a20-2090d5bde37f/collection/19446461-3740caf2-3916-4f46-a032-20efa3a79f57?action=share&creator=19446461
   





