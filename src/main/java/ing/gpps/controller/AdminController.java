package ing.gpps.controller;


import ing.gpps.entity.institucional.Entidad;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.entity.institucional.TipoEntidad;
import ing.gpps.entity.users.Admin;
import ing.gpps.entity.users.Usuario;
import ing.gpps.security.CustomUserDetails;
import ing.gpps.service.EntidadService;
import ing.gpps.service.ProyectoService;
import ing.gpps.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EntidadService entidadService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ProyectoService proyectoService;

    public AdminController() {

    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Verificar autenticación y usuario
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new RuntimeException("Usuario no autenticado");
            }
            Object principal = authentication.getPrincipal();

            if (!(principal instanceof CustomUserDetails)) {
                throw new RuntimeException("Usuario no es CustomUserDetails");
            }

            CustomUserDetails userDetails = (CustomUserDetails) principal;
            Usuario usuario = userDetails.getUsuario();

            if (!(usuario instanceof Admin)) {
                throw new RuntimeException("Usuario no es Admin");
            }

            Admin admin = (Admin) usuario;
            model.addAttribute("admin", admin);

            // Obtener entidades desde el servicio y pasarlas al modelo
            List<Entidad> entidades = entidadService.obtenerTodas();
            model.addAttribute("entidades", entidades);

            List<Usuario> usuarios = usuarioService.obtenerTodos();
            model.addAttribute("usuarios", usuarios);

            List<Proyecto> proyectos = proyectoService.obtenerTodos();
            model.addAttribute("proyectos", proyectos);

            return "indexAdmin";
        } catch (Exception e) {
            System.err.println("Error en dashboard: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/login";
    }
    @PostMapping("/entidad/agregar")
    public String agregarEntidad(@RequestParam("nombre") String nombre,
                                 @RequestParam("cuit") String cuit,
                                 @RequestParam("tipo") String tipo,
                                 @RequestParam("ubicacion") String ubicacion,
                                 @RequestParam("contacto") String contacto,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Crear el objeto Entidad con los parámetros recibidos


            Entidad entidad = new Entidad();
            entidad.setNombre(nombre);
            entidad.setCuit(Long.parseLong(cuit));
            entidad.setTipo(TipoEntidad.valueOf(tipo.toUpperCase())); // Asegúrate de que el tipo sea válido
            entidad.setUbicacion(ubicacion);
            entidad.setEmail(contacto);

            // Llamar al servicio
            entidadService.registrarEntidad(entidad);

            redirectAttributes.addFlashAttribute("mensaje", "Entidad agregada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            System.err.println("Error al agregar entidad: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensaje", "Error al agregar la entidad: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        System.out.println("Entidad agregada: " + nombre + ", CUIT: " + cuit + ", Tipo: " + tipo + ", Ubicación: " + ubicacion + ", Contacto: " + contacto);
        return "redirect:/admin/dashboard";
    }

    // Método GET para mostrar el formulario de edición de entidad
    @GetMapping("/entidad/edit/{cuit}")
    public String editarEntidadForm(@PathVariable("cuit") Long cuit, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Verificar autenticación (opcional, puedes reutilizar el código del dashboard)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return "redirect:/login";
            }

            Entidad entidad = entidadService.obtenerPorCuit(cuit);
            if (entidad == null) {
                redirectAttributes.addFlashAttribute("mensaje", "Entidad no encontrada");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/admin/dashboard";
            }

            model.addAttribute("entidad", entidad);
            model.addAttribute("tiposEntidad", TipoEntidad.values()); // Para el select del formulario

            return "editarEntidad"; // Nombre de la vista para editar
        } catch (Exception e) {
            System.err.println("Error al obtener entidad para editar: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensaje", "Error al obtener la entidad");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/admin/dashboard";
        }
    }

    // Método POST para procesar la edición de entidad
    @PostMapping("/entidad/edit")
    public String editarEntidad(@RequestParam("cuit") Long cuit,
                                @RequestParam("nombre") String nombre,
                                @RequestParam("tipo") String tipo,
                                @RequestParam("ubicacion") String ubicacion,
                                @RequestParam("contacto") String contacto,
                                RedirectAttributes redirectAttributes) {
        try {
            Entidad entidad = entidadService.obtenerPorCuit(cuit);
            if (entidad == null) {
                redirectAttributes.addFlashAttribute("mensaje", "Entidad no encontrada");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/admin/dashboard";
            }

            // Actualizar los datos de la entidad
            entidad.setNombre(nombre);
            entidad.setTipo(TipoEntidad.valueOf(tipo.toUpperCase()));
            entidad.setUbicacion(ubicacion);
            entidad.setEmail(contacto);

            entidadService.actualizarEntidad(entidad);

            redirectAttributes.addFlashAttribute("mensaje", "Entidad actualizada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            System.err.println("Error al actualizar entidad: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar la entidad: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/admin/dashboard";
    }

    // Método POST para eliminar entidad
    @PostMapping("/entidad/delete/{cuit}")
    public String eliminarEntidad(@PathVariable("cuit") Long cuit, RedirectAttributes redirectAttributes) {
        try {
            Entidad entidad = entidadService.obtenerPorCuit(cuit);
            if (entidad == null) {
                redirectAttributes.addFlashAttribute("mensaje", "Entidad no encontrada");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/admin/dashboard";
            }

            entidadService.eliminarEntidad(cuit);

            redirectAttributes.addFlashAttribute("mensaje", "Entidad eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            System.err.println("Error al eliminar entidad: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar la entidad: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("usuario/agregar")
    public String agregarUsuario(@RequestParam("nombre") String nombre,
                                 @RequestParam("apellido") String apellido,
                                 @RequestParam("email") String email,
                                 @RequestParam("telefono") String telefono,
                                 @RequestParam("password") String password,
                                 @RequestParam("rol") String rol,
                                 RedirectAttributes redirectAttributes){

        Long numTelefono = Long.parseLong(telefono);

        usuarioService.registrarUsuario(nombre, apellido, email, numTelefono, password, rol);

        System.out.println(" ");

        return "redirect:/admin/dashboard";
    }


}
