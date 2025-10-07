# GPPS

GPPS (Gestion de Practicas Profesionales Supervisadas) es una plataforma web para organizar el circuito completo de practicas profesionales dentro de la Licenciatura en Sistemas. El sistema centraliza proyectos, actores academicos y empresariales, permite coordinar actividades y simplifica el seguimiento de estudiantes y tutores.

Creado en junio de 2025 por mi junto a Lautaro Salvo, Tomas Acosta y Cristian Millaqueo, estudiantes de la Licenciatura en Sistemas de la Universidad Nacional de Rio Negro.

## Funcionalidades clave
- Paneles diferenciados por rol: estudiantes, docentes supervisores, direccion de carrera y administradores cuentan con vistas especificas construidas con Thymeleaf.
- Gestion integral de proyectos PPS: alta de proyectos, asignacion de tutores, plan de trabajo y objetivos institucionales.
- Plan de trabajo y actividades: cada proyecto define cronogramas, actividades y horas estimadas con seguimiento de progreso automatico.
- Seguimiento de entregas e informes: registro de entregas, control de estados (pendiente, entregado, aprobado) y descarga de archivos adjuntos.
- Notificaciones y correo: servicio de alertas visuales y envio de mails via SMTP para comunicar hitos, novedades y recordatorios.
- Generacion de convenios en PDF: armado automatico de actas acuerdo y anexos utilizando iTextPDF.
- Gestion documental y almacenamiento: subida de archivos al disco (carpeta `uploads/`) con nombres unicos por usuario y proyecto.

## Roles soportados

| Rol | Ruta principal | Enfoque |
| --- | ------------- | ------- |
| Estudiante | `/estudiante/dashboard` | Visualiza su proyecto, plan de trabajo, actividades, entregas e informes con barra de avance. |
| Docente supervisor | `/docente-supervisor/dashboard` | Supervisa proyectos asignados, accede a planillas, descarga entregas y adjunta devoluciones. |
| Direccion de carrera | `/direccion/dashboard` | Revisa postulaciones, avala planes de trabajo y consulta estado de practicantes. |
| Administracion general | `/admin/dashboard` | Gestiona usuarios, entidades, proyectos y convenios institucionales. |
| Login comun | `/login` | Punto de acceso protegido por Spring Security para todos los perfiles. |

## Stack tecnologico
- Java 21 y Maven 3.9+
- Spring Boot 3.2 (Web, Thymeleaf, Data JPA, Validation, Security)
- PostgreSQL como base de datos principal (H2 opcional para pruebas)
- Hibernate ORM y repositorios Spring Data
- Thymeleaf + CSS propio para la capa de presentacion
- iTextPDF para documentos formales
- Jakarta Mail para correo transaccional

## Estructura principal

```
src/
  main/
    java/ing/gpps/
      controller/
      entity/
      notificaciones/
      repository/
      security/
      service/
    resources/
      templates/
      static/css/
      application.properties
uploads/        # almacenamiento de entregas
convenios/      # generado en runtime para actas
fonts/
```

## Requisitos previos
- JDK 21 instalado y configurado en `JAVA_HOME`
- Maven 3.9 o superior
- PostgreSQL 14+ con una base llamada `basedatosprueba`
- Credenciales de correo (app password de Gmail) si se usara el `EmailService`
- Carpeta local `uploads/` con permisos de escritura (Spring la crea si no existe)

## Configuracion

1. Clonar el repositorio y ubicarte en la raiz `gpps/`.
2. Revisar `src/main/resources/application.properties` y ajustar:
   - Cadena JDBC, usuario y clave de PostgreSQL.
   - Estrategia de `spring.jpa.hibernate.ddl-auto` (por defecto `create`, recomendable `update` en entornos persistentes).
   - Ruta `upload.path`.
   - Credenciales `email.remitente` y `email.claveApp`.

Ejemplo de configuracion basica:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/basedatosprueba
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true

upload.path=uploads/
email.remitente=usuario@gmail.com
email.claveApp=clave_app_generada
```

## Ejecucion en local

1. Instalar dependencias y compilar:
   ```
   mvn clean package
   ```
2. Levantar la aplicacion:
   ```
   mvn spring-boot:run
   ```
   o bien
   ```
   java -jar target/demo-0.0.1-SNAPSHOT.jar
   ```
3. Acceder a http://localhost:8080/login y utilizar alguna de las credenciales de prueba.

## Datos iniciales (perfil `create`)

| Rol | Email | Password | Notas |
| --- | ----- | -------- | ----- |
| Admin general | admin@gmail.com | admin | Acceso completo al panel `/admin/dashboard`. |
| Direccion de carrera | mcambarieri@gmail.com | 1234 | Puede revisar proyectos y postulaciones. |
| Docente supervisor | maria_gonzalez@unrn.edu.ar | tutor123 | Supervisa proyectos asignados. |
| Docente supervisor | carlos_rodriguez@unrn.edu.ar | tutor789 | Segundo supervisor cargado. |
| Estudiante | salvoschaferlautaro@gmail.com | 1234 | Estudiante con proyecto y entregas. |
| Estudiante | porretimaxi@gmail.com | 2345 | Estudiante en practica activa. |
| Estudiante | acostatomas@gmail.com | 3456 | Estudiante con plan de trabajo cargado. |
| Estudiante | cristianmillaqueo.12ok@gmail.com | 9293 | Estudiante con solicitud pendiente. |
| Admin de entidad* | cristianmillaqueo@gmail.com | 1234 | Modulo web en revision, credencial disponible para futuras vistas. |

\*El controlador de administracion de entidad (`AdminEntidadController`) esta en proceso de refactor; la capa de servicios y plantillas ya existen.

## Flujo de archivos y convenios
- Las entregas y adjuntos se almacenan bajo `uploads/` con subcarpetas por usuario o entidad segun lo defina `FileStorageService`.
- `ConvenioService` genera actas en PDF dentro de `convenios/`; si la carpeta no existe, se crea al vuelo.
- Los recursos estaticos (CSS, imagenes) se sirven desde `src/main/resources/static/`.



