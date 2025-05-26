# gpps
el thymeleaf de el indexAlumno

th:text="${proyecto.titulo}":
Muestra el título del proyecto que viene del servidor.

th:each="entrega : ${entregas}":
Repite ese bloque para cada entrega que tenga el estudiante.

th:classappend="' status-' + ${entrega.estado.name().toLowerCase()}":
Agrega una clase CSS según el estado de la entrega (para cambiar el color).

th:href="@{/entregas/descargar/{id}(id=${entrega.id})}":
Crea un enlace para descargar la entrega con el ID correspondiente.
¿Cómo se ve la lógica de las entregas?

Apply to DemoApplicat...
Por cada entrega, se crea un bloque visual con su información.
Se muestra el título, la fecha límite, el estado, y botones para descargar o subir archivos.

¿Qué hace el JavaScript?
Permite mostrar y ocultar el formulario de subida de entregas.
Permite manejar la subida de archivos (aunque en este ejemplo solo simula la subida).
Cambia el color del área de arrastrar y soltar archivos cuando el usuario interactúa.
