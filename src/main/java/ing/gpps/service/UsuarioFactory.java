package ing.gpps.service;

import ing.gpps.entity.users.*;

class UsuarioFactory {

    private final String rol;

    UsuarioFactory(String rol) {
        this.rol = rol;
    }

    Usuario crerUsuario(String nombre, String apellido, String email, String password, Long telefono){
        switch (rol) {
            case "ESTUDIANTE":
                return new Estudiante(nombre, apellido, email, password, telefono); // clase hija de Usuario
            case "DOCENTE_SUPERVISOR":
                return new DocenteSupervisor(nombre, apellido, email, password, telefono);
            case "TUTOR_EXTERNO":
                return new TutorExterno(nombre, apellido, email, password, telefono);
            case "ADMIN":
                return new Admin(nombre, apellido, email, password, telefono);
            case "DIRECCION_CARRERA":
                return new DireccionDeCarrera(nombre, apellido, email, password, telefono);
            case "ADMIN_ENTIDAD":
                return new AdminEntidad(nombre, apellido, email, password, telefono);
            default:
                throw new IllegalArgumentException("Rol desconocido: " + rol);
        }
    }

}
