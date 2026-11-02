# TricliniUM — Gestión y promoción de restaurantes

Proyecto desarrollado para la asignatura **AADD**. Implementa el servicio `ServicioRestaurantes` para la gestión de restaurantes, su oferta gastronómica y la consulta por parte de los clientes, incluyendo la integración con una API externa para obtener puntos de interés cercanos.

## 👥 Autores
- **Álvaro Lara** (Usuario GitHub: alvarolara2005boop)
- **Víctor Manuel Pérez Castillo (Usuario GitHub: victorpc64)

## 🛠️ Tecnologías utilizadas
- **Java 11**
- **Maven** (gestión de dependencias y ciclo de vida)
- **JPA 2.2 + Hibernate 5.6** (persistencia)
- **MySQL 8** (base de datos relacional)
- **API GeoNames** (`findNearbyWikipedia`) procesada mediante **DOM XML**
- **JUnit** (pruebas, si aplica)

## ⚙️ Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener instalado y configurado:
1. **Java JDK 11** o superior.
2. **Maven 3.6+**.
3. **MySQL Server 8** en ejecución en el puerto `3306`.
4. Una cuenta de **GeoNames** con el webservice gratuito habilitado.

## 🚀 Configuración y Ejecución

### 1. Base de Datos (MySQL 8)
Abre MySQL Workbench o tu cliente de consola y crea la base de datos con el nombre exigido por el enunciado (ajusta los apellidos):

sql
CREATE DATABASE tricliniumLaraLara
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
sql

### 2. Configuración de JPA
Edita el archivo src/main/resources/META-INF/persistence.xml para indicar tus credenciales de MySQL:

xml
<property name="javax.persistence.jdbc.url" 
          value="jdbc:mysql://localhost:3306/tricliniumLaraLara?useSSL=false&amp;serverTimezone=UTC&amp;allowPublicKeyRetrieval=true"/>
<property name="javax.persistence.jdbc.user" value="root"/>
<property name="javax.persistence.jdbc.password" value="FacultadInfo12"/>
```xml

### 3. Cuenta GeoNames
Para que funcione la asignación de puntos de interés, la cuenta debe tener habilitado el webservice gratuito:
java
private static final String GEONAMES_USER = "alvaro.laral";
java

### Estructura del proyecto
text
tricliniumLaraLara/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── modelo/         # Entidades (Restaurante, Plato, PuntoInteres, TipoCocina)
    │   │   ├── repositorio/    # Interfaces y clases AdHoc / JPA
    │   │   ├── fuentes/        # Cliente GeoNames
    │   │   ├── servicio/       # Lógica de negocio (ServicioRestaurantes)
    │   │   └── test/           # Programa de pruebas
    │   └── resources/
    │       └── META-INF/
    │           └── persistence.xml
    └── test/
        └── java/

    
### Decisiones de diseño aplicadas
El proyecto cumple estrictamente con los requisitos de diseño exigidos:

- Inversión de Dependencias (DIP): El servicio (ServicioRestaurantesImpl) no depende de implementaciones concretas, sino de las interfaces (RepositorioRestaurantes, RepositorioPlatos, FuentePuntosInteres), recibidas a través del constructor.

- Patrón Repositorio: Se han definido interfaces para abstraer el acceso a datos, permitiendo cambiar la fuente de persistencia sin afectar a la lógica de negocio.

- Repositorio AdHoc: Implementación en memoria (usando HashMap) para pruebas rápidas y aislamiento de la base de datos.

- Patrón Servicio: La clase ServicioRestaurantes actúa como fachada, orquestando las llamadas a los repositorios y a la fuente externa.

- Integración con API Externa: Se utiliza DocumentBuilderFactory (API DOM de Java) para procesar la respuesta XML de GeoNames, cumpliendo el requisito de no usar librerías de parseo de terceros.

- Resolución del MultipleBagFetchException: Se ha utilizado la anotación @Fetch(FetchMode.SUBSELECT) de Hibernate para evitar productos cartesianos al cargar múltiples colecciones EAGER (puntosInteres y carta) en la entidad Restaurante.

## Pruebas incluidas
El programa principal (test.Programa) ejecuta una batería de pruebas que cubre:

- Alta de restaurantes y platos.

- Asignación de puntos de interés mediante GeoNames.

- Modificación de datos (restaurantes y platos).

- Inclusión/exclusión de platos de la carta.

- Búsqueda de restaurantes con múltiples filtros combinados.

- Filtrado de la carta por precio, categoría y texto.

- Casos de error: IDs inexistentes, parámetros inválidos (max <= 0), etc.


