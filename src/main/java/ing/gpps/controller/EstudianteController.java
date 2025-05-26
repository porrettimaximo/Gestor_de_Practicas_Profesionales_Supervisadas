package ing.gpps.controller;


import ing.gpps.entity.institucional.Entrega;
import ing.gpps.entity.users.Estudiante;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.users.Usuario;
import ing.gpps.security.CustomUserDetails;
import ing.gpps.service.EntregaService;
import ing.gpps.service.ProyectoService;
import ing.gpps.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {
    private final UsuarioService usuarioService;
    private final ProyectoService proyectoService;
    private final EntregaService entregaService;

    @Autowired
    public EstudianteController(UsuarioService usuarioService, ProyectoService proyectoService, EntregaService entregaService) {
        this.usuarioService = usuarioService;
        this.proyectoService = proyectoService;
        this.entregaService = entregaService;
    }

    // Modificar el método dashboard para asegurar que se están cargando correctamente los datos
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();

                if (principal instanceof CustomUserDetails) {
                    CustomUserDetails userDetails = (CustomUserDetails) principal;
                    Usuario usuario = userDetails.getUsuario();

                    if (usuario instanceof Estudiante) {
                        Estudiante estudiante = (Estudiante) usuario;
                        List<Proyecto> proyectos = proyectoService.buscarPorEstudiante(estudiante);

                        // Agregar el estudiante al modelo siempre
                        model.addAttribute("estudiante", estudiante);

                        if (!proyectos.isEmpty()) {
                            Proyecto proyectoActual = proyectos.get(0); // Tomamos el primer proyecto
                            List<Entrega> entregas = entregaService.buscarPorProyecto(proyectoActual);
                            List<Entrega> entregasAprobadas = entregaService.buscarAprobadasPorProyecto(proyectoActual);

                            // Agregar todos los atributos necesarios al modelo
                            model.addAttribute("proyecto", proyectoActual);
                            model.addAttribute("planDeTrabajo", proyectoActual.getPlanDeTrabajo());
                            model.addAttribute("entregas", entregas);
                            model.addAttribute("entregasAprobadas", entregasAprobadas);

                            System.out.println("Cargando indexAlumno para estudiante: " + estudiante.getNombre() + " " + estudiante.getApellido());
                            System.out.println("Proyecto: " + proyectoActual.getTitulo());
                            System.out.println("Número de entregas: " + entregas.size());

                            return "indexAlumno"; // Usa tu template existente
                        } else {
                            System.out.println("El estudiante no tiene proyectos asignados: " + estudiante.getNombre() + " " + estudiante.getApellido());
                            return "indexAlumnoSinPPS"; // Si no tiene proyectos, muestra la vista sin PPS
                        }
                    } else {
                        System.out.println("El usuario no es un estudiante: " + usuario.getClass().getName());
                    }
                } else {
                    System.out.println("El principal no es un CustomUserDetails: " + principal.getClass().getName());
                }
            } else {
                System.out.println("No hay autenticación o el usuario no está autenticado");
            }
        } catch (Exception e) {
            System.err.println("Error en dashboard: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/login";
    }

    @PostMapping("/entregas/subir")
    public String subirEntrega(@RequestParam("archivo") MultipartFile archivo,
                               @RequestParam("comentarios") String comentarios,
                               @RequestParam("entregaId") int entregaId,
                               RedirectAttributes redirectAttributes,
                               Authentication authentication) {
        try {
            // Verificar que el usuario esté autenticado
            if (authentication == null || !authentication.isAuthenticated()) {
                return "redirect:/login";
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Usuario usuario = userDetails.getUsuario();

            if (!(usuario instanceof Estudiante)) {
                redirectAttributes.addFlashAttribute("error", "Solo los estudiantes pueden subir entregas");
                return "redirect:/estudiante/dashboard";
            }

            // Buscar la entrega
            Optional<Entrega> entregaOpt = entregaService.buscarPorId(entregaId);
            if (!entregaOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Entrega no encontrada");
                return "redirect:/estudiante/dashboard";
            }

            Entrega entrega = entregaOpt.get();

            // Verificar que el archivo no esté vacío
            if (archivo.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Por favor seleccione un archivo");
                return "redirect:/estudiante/dashboard";
            }

            // Crear directorio para almacenar archivos si no existe
            String uploadDir = "uploads/entregas/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generar nombre único para el archivo
            String fileName = entregaId + "_" + System.currentTimeMillis() + "_" + archivo.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // Guardar el archivo
            Files.copy(archivo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Calcular tamaño del archivo
            String tamanoArchivo = formatFileSize(archivo.getSize());

            // Registrar la entrega en la base de datos
            entregaService.registrarEntrega(entrega, filePath.toString(), tamanoArchivo, comentarios);

            redirectAttributes.addFlashAttribute("success", "Entrega subida exitosamente");

        } catch (IOException e) {
            System.err.println("Error al subir archivo: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al subir el archivo");
        } catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al procesar la entrega");
        }

        return "redirect:/estudiante/dashboard";
    }

    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0);
        } else {
            return String.format("%.1f MB", size / (1024.0 * 1024.0));
        }
    }

    @GetMapping("/entregas/descargar/{id}")
    public ResponseEntity<Resource> descargarEntrega(@PathVariable int id, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Usuario usuario = userDetails.getUsuario();

            if (!(usuario instanceof Estudiante)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Optional<Entrega> entregaOpt = entregaService.buscarPorId(id);
            if (!entregaOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Entrega entrega = entregaOpt.get();

            if (entrega.getArchivoUrl() == null || entrega.getArchivoUrl().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get(entrega.getArchivoUrl());
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName().toString() + "\"")
                    .body(resource);

        } catch (Exception e) {
            System.err.println("Error al descargar archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/sin-pps")
    public String sinPPS(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Usuario usuario = userDetails.getUsuario();

            if (usuario instanceof Estudiante) {
                Estudiante estudiante = (Estudiante) usuario;
                model.addAttribute("estudiante", estudiante);
                return "indexAlumnoSinPPS";
            }
        }
        return "redirect:/login";
    }

    // Muestra solo las entregas hechas por el estudiante
    @GetMapping("/entregas/hechas")
    public String mostrarEntregasHechas(Model model, Authentication authentication) {
        Optional<Usuario> estudiante = usuarioService.buscarPorEmail(authentication.getName());
        Optional<Entrega> entregasHechas = entregaService.buscarPorId(estudiante.get().getId());
        model.addAttribute("entregas", entregasHechas);
        return "entregasHechas"; // nombre de la vista (ej: entregasHechas.html)
    }

    // Muestra todas las entregas del estudiante
    @GetMapping("/entregas")
    public String mostrarEntregas(Model model, Authentication authentication) {
        Optional<Usuario> estudiante = usuarioService.buscarPorEmail(authentication.getName());
        Optional<Entrega> todasEntregas = entregaService.buscarPorId(estudiante.get().getId());
        model.addAttribute("entregas", todasEntregas);
        return "entregas"; // nombre de la vista (ej: entregas.html)
    }
}
