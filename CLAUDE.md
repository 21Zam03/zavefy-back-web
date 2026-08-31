# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Descripción general

Backend de **Zavefy** (nombre de artefacto Maven: `ventas-bodega`), una API REST en Spring Boot para gestión de bodegas/tiendas: ventas, inventario, clientes, oportunidades (leads), reportes y un catálogo público. Es **multi-tenant**: casi todos los datos están particionados por `CompanyEntity` (identificada por `ruc`), y cada usuario autenticado pertenece a una compañía.

## Comandos

Todos se ejecutan desde la raíz del repo, usando el wrapper de Maven (`mvnw`/`mvnw.cmd`).

```bash
# Compilar
./mvnw compile

# Ejecutar en local (usa src/main/resources/application.yaml)
./mvnw spring-boot:run

# Ejecutar toda la suite de tests
./mvnw test

# Ejecutar una sola clase de test
./mvnw test -Dtest=VentasBodegaApplicationTests

# Empaquetar el jar (usado por el Dockerfile: target/ventas-bodega-0.0.1.jar)
./mvnw clean package
```

En Windows usar `mvnw.cmd` en vez de `./mvnw`.

## Arquitectura

Paquete base: `com.example.ventas_bodega`. Flujo estándar por feature:

```
Controller (@RestController, /api/...) 
  -> Service (interfaz) -> impl/ServiceImpl (@Service, lógica de negocio) 
    -> Repository (Spring Data JPA) -> Entity
```

- **DTO vs Entity vs Request/Response**: `dto/` son los objetos de transferencia usados entre service y mapper; `request/` son los payloads de entrada de los controllers (`SignInRequest`, `SaleRequest`, etc.); `response/` son las salidas específicas de un endpoint (`SignInResponse`, `ErrorResponse`, etc.). `entity/` son las entidades JPA.
- **Mappers son clases estáticas** (`mapper/ProductMapper`, `mapper/SaleMapper`, ...) con métodos `dtoToEntity`, `mapToInternal`, `buildXFromController`, etc. No se usa MapStruct ni un mapper por instancia/bean.
- **Multi-tenencia**: casi todo se filtra por `user.getCompany().getRuc()`. Al añadir un endpoint o query nuevo, propagar el filtro por compañía salvo que el recurso sea explícitamente público (ver `SecurityConfig`).
- **Usuario autenticado**: se inyecta en los controllers con `@CurrentUser UserEntity user` (`security/annotation/CurrentUser.java`), que es un alias de `@AuthenticationPrincipal(expression = "user")` sobre `CustomUserDetails` (`security/custom/CustomUserDetails.java`), la cual envuelve el `UserEntity` real.
- **Autenticación**: JWT (com.auth0:java-jwt) guardado en una cookie httpOnly `auth_token` (no en `Authorization` header desde el cliente). `security/filter/JwtCookieTokenFilter` decodifica la cookie en cada request, carga el usuario vía `CustomUserDetailService` y arma las `GrantedAuthority` a partir de los claims `roles` (`ROLE_*`) y `permissions` (`PERMISSION_*`) del token. Login/registro están en `AuthController`/`AuthServiceImpl`.
- **Autorización por endpoint**: se define en `config/SecurityConfig.java` (allowlist explícita: `/api/auth/**`, `/api/catalog/**`, `/api/print-jobs/**`, `/api/option/**` son públicos; el resto requiere autenticación; algunos requieren rol/permiso específico). Al agregar un controller nuevo, registrar su regla ahí.
- **Manejo de errores**: centralizado en `config/GlobalExceptionConfig` (`@RestControllerAdvice`) más las excepciones custom `exceptions/NotFoundException` y `exceptions/DuplicateException`.
- **Catálogo público**: `CatalogController`/`CatalogService` exponen productos de una compañía sin autenticación (usado por el frontend en `/catalogo/:id`), a diferencia del resto de la API que es privada.

### Integraciones externas

- **Firebase Storage** (`config/FirebaseStorageConfig`, `service/FirebaseStorageService`) para imágenes de productos, con credenciales en `src/main/resources/*firebase-adminsdk*.json`.
- **RENIEC** (`rest/ReniecRestTemplate` + `ReniecController`) para autocompletar datos de clientes peruanos por DNI.
- **Open Food Facts** (`rest/FoodRestTemplate`) para autocompletar datos de producto por código de barras (`response/OpenFoodFactsResponse`).
- **JasperReports** (`resources/templates/report/sale-ticket.jrxml`) para generar tickets/comprobantes de venta en PDF; también se usa OpenPDF/Apache POI para exportes.
- **ZXing** para generación de códigos QR (`util/QrUtil`).
- **Scheduler**: `scheduler/CronJobs` para tareas periódicas (p.ej. estados de oportunidades).

### Configuración

`src/main/resources/application.yaml` contiene la config activa (puerto 8080, datasource MySQL, JPA con `ddl-auto: update`, clave JWT, y parámetros de cookie bajo `parameters:`). Es un único perfil, sin `application-{profile}.yaml` separados — los valores de entorno (URLs de BD, credenciales) se editan directamente ahí.
