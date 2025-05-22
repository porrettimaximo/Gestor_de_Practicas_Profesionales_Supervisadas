package ing.gpps.controller;

import ing.gpps.entity.users.Usuario;
import ing.gpps.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller  // Usa @Controller en lugar de @RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public String crearUsuario(@ModelAttribute Usuario usuario, Model model) {
        try {
            
            //usuarioService.guardar(usuario);
            model.addAttribute("mensaje", "Usuario creado exitosamente");
            return "redirect:/indexConPPs";  // Redirecciona a la lista de usuarios
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear usuario");
            return "login";  // Vuelve al formulario
        }
    }
/*
    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable int id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "detalle-usuario";  // Muestra vista de detalle
        } else {
            model.addAttribute("error", "Usuario no encontrado");
            return "error";  // Muestra página de error
        }
    }
*/
}



