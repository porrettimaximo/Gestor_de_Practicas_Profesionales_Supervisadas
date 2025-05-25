# gpps
## 🔧 **1. CAMBIOS EN EL CONTROLADOR (`EstudianteController.java`)**

### **ANTES**:

- Solo tenía el método `dashboard()` básico
- No había funcionalidad de archivos


### **DESPUÉS**: Agregamos 3 métodos nuevos:

#### **A) Método `subirEntrega()`**

```java
@PostMapping("/entregas/subir")
public String subirEntrega(@RequestParam("archivo") MultipartFile archivo,
                      @RequestParam("comentarios") String comentarios,
                      @RequestParam("entregaId") int entregaId,
                      RedirectAttributes redirectAttributes,
                      Authentication authentication)
```

**¿Qué hace?**

- Recibe archivos desde el formulario web
- Valida que el usuario sea estudiante
- Crea directorio `uploads/entregas/` si no existe
- Genera nombre único: `{entregaId}_{timestamp}_{nombreOriginal}`
- Guarda archivo físicamente en el servidor
- Actualiza la base de datos con la información del archivo
- Redirige de vuelta al dashboard


#### **B) Método `descargarEntrega()`**

```java
@GetMapping("/entregas/descargar/{id}")
public ResponseEntity<Resource> descargarEntrega(@PathVariable int id, Authentication authentication)
```

**¿Qué hace?**

- Permite descargar archivos previamente subidos
- Valida permisos del usuario
- Busca el archivo en el sistema de archivos
- Retorna el archivo como descarga


#### **C) Método auxiliar `formatFileSize()`**

```java
private String formatFileSize(long size)
```

**¿Qué hace?**

- Convierte bytes a formato legible (B, KB, MB)


---

## 🎨 **2. CAMBIOS EN LA INTERFAZ (`indexAlumno.html`)**

### **ANTES**:

- Template básico sin funcionalidad de archivos
- Solo mostraba información estática


### **DESPUÉS**: Agregamos funcionalidad completa:

#### **A) Estructura HTML nueva:**

```html
<!-- Formulario de subida de entrega -->
<div class="upload-section" id="upload-form" style="display: none;">
    <form th:action="@{/estudiante/entregas/subir}" method="post" enctype="multipart/form-data">
        <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
        <!-- Área de drag & drop -->
        <div class="file-input-container">
            <input type="file" id="file-upload" name="archivo" class="file-input" required>
            <label for="file-upload" class="file-input-label">
                <span class="file-input-icon"><i class="fas fa-cloud-upload-alt"></i></span>
                <span>Arrastra y suelta archivos aquí o haz clic para seleccionar</span>
            </label>
        </div>
        <!-- Campo de comentarios -->
        <textarea name="comentarios" placeholder="Añade comentarios..."></textarea>
        <!-- ID oculto de la entrega -->
        <input type="hidden" id="entrega-id" name="entregaId" value="">
        <button type="submit">Enviar Entrega</button>
    </form>
</div>
```

#### **B) Botones de acción:**

```html
<div class="document-actions">
    <!-- Botón de descarga -->
    <a th:href="@{/estudiante/entregas/descargar/{id}(id=${entrega.id})}" class="btn small primary">
        <i class="fas fa-download"></i>
    </a>
    <!-- Botón de subida -->
    <button class="btn small secondary upload-btn"
            th:data-entrega-id="${entrega.id}"
            th:data-entrega-titulo="${entrega.titulo}">
        <i class="fas fa-upload"></i>
    </button>
</div>
```

#### **C) JavaScript completo:**

```javascript
// 1. Event listeners para botones de subida
uploadButtons.forEach((button, index) => {
    button.addEventListener('click', function(e) {
        // Mostrar formulario con datos de la entrega seleccionada
        entregaTitle.textContent = entregaTitulo;
        entregaIdInput.value = entregaId;
        uploadForm.style.display = 'block';
    });
});

// 2. Funcionalidad drag & drop
dropArea.addEventListener('drop', function(e) {
    const files = e.dataTransfer.files;
    if (files.length > 0) {
        fileInput.files = files;
        updateFileInputLabel();
    }
});

// 3. Validación de formulario
form.addEventListener('submit', function(e) {
    if (!fileInput.files.length) {
        e.preventDefault();
        alert('Por favor, selecciona un archivo para subir.');
    }
});

// 4. Indicador de carga
submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Subiendo...';
```

---

## 🎯 **3. CORRECCIONES DE ERRORES**

### **Error 1: URLs incorrectas**

- **ANTES**: `@{/entregas/subir}` y `@{/entregas/descargar/{id}}`
- **DESPUÉS**: `@{/estudiante/entregas/subir}` y `@{/estudiante/entregas/descargar/{id}}`


### **Error 2: JavaScript conflictivo**

- **ANTES**: Múltiples event listeners duplicados
- **DESPUÉS**: JavaScript limpio y organizado


### **Error 3: Falta de token CSRF**

- **ANTES**: Sin protección CSRF
- **DESPUÉS**: `<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>`


---

## 🔄 **4. FLUJO COMPLETO IMPLEMENTADO**

### **Proceso de subida:**

1. Usuario hace clic en botón rojo de "subir" ⬆️
2. Se abre formulario con drag & drop
3. Usuario selecciona archivo y añade comentarios
4. JavaScript valida que hay archivo
5. Formulario se envía a `/estudiante/entregas/subir`
6. Controlador procesa archivo:

1. Crea directorio si no existe
2. Guarda archivo con nombre único
3. Actualiza base de datos



7. Redirige al dashboard con mensaje de éxito


### **Proceso de descarga:**

1. Usuario hace clic en botón azul de "descargar" ⬇️
2. Navegador hace GET a `/estudiante/entregas/descargar/{id}`
3. Controlador valida permisos
4. Busca archivo en sistema
5. Retorna archivo como descarga


---

## 📊 **5. CARACTERÍSTICAS IMPLEMENTADAS**

✅ **Subida de archivos** con drag & drop✅ **Descarga de archivos** con validación de permisos✅ **Validación de formularios** en frontend y backend✅ **Indicadores de carga** durante la subida✅ **Mensajes de éxito/error** con notificaciones visuales✅ **Almacenamiento seguro** con nombres únicos✅ **Integración con base de datos** para tracking✅ **Interfaz responsive** y moderna✅ **Protección CSRF** para seguridad

---

## 🗂️ **6. ESTRUCTURA DE ARCHIVOS CREADA**

```plaintext
tu-proyecto/
├── uploads/                    ← NUEVO directorio
│   └── entregas/              ← NUEVO subdirectorio
│       ├── 1_1737847582324_documento.pdf
│       ├── 2_1737847619440_informe.docx
│       └── ...
├── src/main/java/ing/gpps/controller/
│   └── EstudianteController.java    ← MODIFICADO (3 métodos nuevos)
└── src/main/resources/templates/
    └── indexAlumno.html            ← COMPLETAMENTE RENOVADO
```

---

## 🎯 **RESULTADO FINAL**

Pasamos de un **dashboard básico** a un **sistema completo de gestión de entregas** con:

- Interfaz moderna y funcional
- Subida de archivos con drag & drop
- Descarga segura de archivos
- Validaciones completas
- Integración total con la base de datos
- Experiencia de usuario profesional


**¡Todo funcionando perfectamente según los logs que me mostraste!** 🚀
