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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/sin-pps")
    public String sinPPS(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            ing.gpps.entity.users.Usuario usuario = userDetails.getUsuario();

            if (usuario instanceof Estudiante) {
                Estudiante estudiante = (Estudiante) usuario;
                model.addAttribute("estudiante", estudiante);
                return "indexAlumnoSinPPS";
            }
        }
        return "redirect:/login";
    }

//    @GetMapping("/proyecto/{id}")
//    public String verProyecto(@PathVariable int id, Model model, Authentication authentication) {
//        if (authentication != null && authentication.isAuthenticated()) {
//            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//            Usuario usuario = userDetails.getUsuario();
//
//            if (usuario instanceof Estudiante) {
//                Estudiante estudiante = (Estudiante) usuario;
//                Optional<Proyecto> proyecto = proyectoService.buscarPorEstudianteYId(estudiante, id);
//
//                if (proyecto.isPresent()) {
//                    List<Entrega> entregas = entregaService.buscarPorProyecto(proyecto.get());
//                    List<Entrega> entregasAprobadas = entregaService.buscarAprobadasPorProyecto(proyecto.get());
//
//                    model.addAttribute("estudiante", estudiante);
//                    model.addAttribute("proyecto", proyecto.get());
//                    model.addAttribute("entregas", entregas);
//                    model.addAttribute("entregasAprobadas", entregasAprobadas);
//
//                    return "indexAlumno";
//                }
//            }
//        }
//        return "redirect:/login";
//    }
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
