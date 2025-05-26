package ing.gpps.controller;


import ing.gpps.entity.institucional.Entidad;
import ing.gpps.entity.institucional.TipoEntidad;
import ing.gpps.entity.users.Admin;
import ing.gpps.entity.users.Usuario;
import ing.gpps.security.CustomUserDetails;
import ing.gpps.service.EntidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EntidadService entidadService;

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

}
