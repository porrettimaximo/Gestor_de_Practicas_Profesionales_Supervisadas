//package ing.gpps.service;
//
//import ing.gpps.entity.idClasses.ActividadId;
//import ing.gpps.entity.idClasses.PlanDeTrabajoId;
//import ing.gpps.entity.idClasses.ProyectoId;
//import ing.gpps.entity.institucional.*;
//import ing.gpps.entity.users.AdminEntidad;
//import ing.gpps.entity.users.TutorExterno;
//import ing.gpps.repository.*;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class AdminEntidadService {
//    private AdminEntidadRepository adminEntidadRepository;
//    private TutorRepository tutorRepository;
//    private EntidadRepository entidadRepository;
//    private ProyectoRepository proyectoRepository;
//    private PlanDeTrabajoRepository planDeTrabajoRepository;
//    private ActividadRepository actividadRepository;
//    private EstudianteRepository estuditanteRepository;
//    private List<Actividad> actividades;
//    private AreaRepository areaRepository;
//
//    @Autowired
//    private UsuarioService usuarioService;
//
//
//    public AdminEntidadService(AdminEntidadRepository adminEntidadRepository, TutorRepository tutorRepository, EntidadRepository entidadRepository, ProyectoRepository proyectoRepository, PlanDeTrabajoRepository planDeTrabajoRepository, ActividadRepository actividadRepository, EstudianteRepository estuditanteRepository, UsuarioService usuarioService, AreaRepository areaRepository) {
//        this.adminEntidadRepository = adminEntidadRepository;
//        this.tutorRepository = tutorRepository;
//        this.entidadRepository = entidadRepository;
//        this.proyectoRepository = proyectoRepository;
//        this.planDeTrabajoRepository = planDeTrabajoRepository;
//        this.actividadRepository = actividadRepository;
//        this.estuditanteRepository = estuditanteRepository;
//        this.usuarioService = usuarioService;
//        this.areaRepository = areaRepository;
//    }
//
//    public void registrarAdministrador(AdminEntidad adminEntidad) {
//        adminEntidadRepository.save(adminEntidad);
//    }
//
//    public void altaTutor(String nombre, String apellido, String email, String password, Long numTelefono, Long cuit) {
//        validarCuit(cuit);
//        validarCamposTexto(nombre);
//        validarCamposTexto(apellido);
//        validarCamposTexto(email);
//        TutorExterno tutor = new TutorExterno(nombre, apellido, email, password, numTelefono);
//        tutor.setCuit(cuit);
//        if (tutorRepository.findByEmail(email).isEmpty()) {
//            usuarioService.registrarUsuario(tutor);
//        } else {
//            throw new IllegalArgumentException("El email ya está registrado.");
//        }
//    }
//
/// /    public void bajaTutor(Long id) {
/// /        if (tutorRepository.findByProyectoId(id).isEmpty()) {
/// /            tutorRepository.deleteById(id);
/// /        } else {
/// /            throw new IllegalArgumentException("Tutor no encontrado.");
/// /        }
/// /    }
//
//    public void generarActividad(int numeroProyecto, String tituloProyecto, Long cuitEntidad, String nombre, String descripcion, int cantidadHoras, boolean adjuntaArchivo) {
//        validarCamposTexto(nombre);
//        validarCamposTexto(descripcion);
//        validarCamposTexto(tituloProyecto);
//        validarCuit(cuitEntidad);
//        cantidadDeHorasValidas(cantidadHoras);
//        Proyecto proyecto = proyectoRepository.findByProyectoIdCuitEntidad(cuitEntidad);
//        if (proyecto == null) {
//            throw new EntityNotFoundException("No se encontró un proyecto para la entidad con CUIT: " + cuitEntidad);
//        }
//        if (this.actividades.stream().mapToInt(Actividad::getCantidadHoras).sum() + cantidadHoras > 200) {
//
//            throw new IllegalArgumentException("La cantidad total de horas no puede superar las 200.");
//        }
//        Actividad actividad = new Actividad();
//        actividad.setNombre(nombre);
//        actividad.setDescripcion(descripcion);
//        actividad.setCantidadHoras(cantidadHoras);
//        actividad.setAdjuntaArchivo(adjuntaArchivo);
//        actividad.setProyecto(proyecto);
//        this.actividades.add(actividad);
//    }
//
//    public void solicitudBajaEntidad() {
//        //TODO: Implementar lógica para solicitar baja de entidad a través de mensajes
//    }
//
//    public List<Proyecto> obtenerProyectosSinPlan(Long cuitEntidad) {
//        List<Proyecto> proyectos = proyectoRepository.findWithoutPlanDeTrabajoByEntidad(cuitEntidad);
//        if (proyectos.isEmpty()) {
//            throw new EntityNotFoundException("No se encontraron proyectos sin plan de trabajo para la entidad con CUIT: " + cuitEntidad);
//        }
//        return proyectos;
//    }
//
//    @Transactional
//    public void generarPlanDeTrabajo(List<Actividad> actividadesFormulario, int numeroPlan, LocalDate inicio, LocalDate fin, String tituloProyecto, Long cuitEntidad) {
//        if (actividadesFormulario == null || actividadesFormulario.isEmpty()) {
//            throw new IllegalArgumentException("La lista de actividades no puede ser nula o vacía.");
//        }
//
//        // 1. Obtener el proyecto
//        ProyectoId proyectoId = new ProyectoId(tituloProyecto, cuitEntidad);
//        Proyecto proyecto = proyectoRepository.findByProyectoId(proyectoId)
//                .orElseThrow(() -> new EntityNotFoundException("No se encontró el proyecto con título " + tituloProyecto + " y cuit " + cuitEntidad));
//
//        // 2. Crear el ID del plan
//        PlanDeTrabajoId planId = new PlanDeTrabajoId(numeroPlan, proyectoId);
//
//        // 3. ✅ VERIFICAR SI YA EXISTE EL PLAN
//        Optional<PlanDeTrabajo> planExistente = planDeTrabajoRepository.findByPlanDeTrabajoId(planId);
//        if (planExistente.isPresent()) {
//            throw new IllegalArgumentException("Ya existe un plan de trabajo con el número " + numeroPlan + " para el proyecto " + tituloProyecto);
//        }
//
//        // 4. Crear y configurar el plan
//        PlanDeTrabajo plan = new PlanDeTrabajo();
//        plan.setId(planId);
//        plan.setProyecto(proyecto);
//        plan.setFechaInicio(inicio);
//        plan.setFechaFin(fin);
//
//        // 5. Guardar el plan primero
//        plan = planDeTrabajoRepository.save(plan);
//
//        // 6. Crear y guardar las actividades
//        List<Actividad> actividadesNuevas = new ArrayList<>();
//
//        for (int i = 0; i < actividadesFormulario.size(); i++) {
//            Actividad actividadFormulario = actividadesFormulario.get(i);
//
//            Actividad nuevaActividad = new Actividad();
//            nuevaActividad.setNombre(actividadFormulario.getNombre());
//            nuevaActividad.setDescripcion(actividadFormulario.getDescripcion());
//            nuevaActividad.setCantidadHoras(actividadFormulario.getCantidadHoras());
//            nuevaActividad.setAdjuntaArchivo(actividadFormulario.isAdjuntaArchivo());
//
//            ActividadId actividadId = new ActividadId(i + 1, planId);
//            nuevaActividad.setActividadId(actividadId);
//            nuevaActividad.setPlanDeTrabajo(plan);
//
//            actividadesNuevas.add(nuevaActividad);
//        }
//
//        // 7. Guardar todas las actividades
//        actividadRepository.saveAll(actividadesNuevas);
//
//        System.out.println("Plan de trabajo guardado correctamente con " + actividadesNuevas.size() + " actividades");
//    }
//
//    @Transactional
//    public void proponerProyectos(Proyecto proyecto) {
//        if (proyecto.getEntidad() == null || proyecto.getTitulo() == null) {
//            throw new IllegalArgumentException("El proyecto debe tener entidad y título definidos.");
//        }
//
//        ProyectoId proyectoId = new ProyectoId(proyecto.getTitulo(), proyecto.getEntidad().getCuit());
//        proyecto.setProyectoId(proyectoId);
//
//        proyectoRepository.save(proyecto);
//
//        verificarCarga(proyecto.getProyectoId().getCuitEntidad()); // opcional para debug
//    }
//
//
//    public void modificarActividades(Actividad actividad, Long cuitEntidad, int cantidadHoras, String nombre, String descripcion, boolean adjuntaArchivo) {
//        if (actividad == null) {
//            throw new IllegalArgumentException("La lista de actividades no puede estar vacía.");
//        }
//
//        Proyecto proyecto = proyectoRepository.findByProyectoIdCuitEntidad(cuitEntidad);
//        if (estuditanteRepository.findByProyecto(proyecto).isPresent()) {
//            throw new RuntimeException("El proyecto ya tiene estudiantes asociados.");
//        }
//        if (proyecto == null) {
//            throw new EntityNotFoundException("No se encontró un proyecto para la entidad con CUIT: " + cuitEntidad);
//        }
//
//        cantidadDeHorasValidas(cantidadHoras);
//        validarCamposTexto(nombre);
//        validarCamposTexto(descripcion);
//
//        actividad.setCantidadHoras(cantidadHoras);
//        actividad.setNombre(nombre);
//        actividad.setDescripcion(descripcion);
//        actividad.setAdjuntaArchivo(adjuntaArchivo);
//
//    }
//
/// /    public void modificarDatosEntidad(Long cuit, String nombre, String ubicacion, TipoEntidad tipo, String email, String numeroTelefono) {
/// /        if (!validarCamposTexto(nombre) && !validarCamposTexto(ubicacion) &&
/// /                !validarCamposTexto(email) && !validarCuit(cuit) &&
/// /                entidadRepository.findByCuit(cuit).isPresent()) {
/// /
/// /            Entidad entidad = new Entidad(cuit, nombre, ubicacion, email, tipo, numeroTelefono);
/// /            entidadRepository.save(entidad);
/// /        } else {
/// /            throw new IllegalArgumentException("Entidad no encontrada.");
/// /        }
/// /    }
//
//    public void aceptarProyectoEstudiante() {
//        //REVISAR, CREO QUE DEBERÍA IR EN DIRECCIÓN DE CARRERA
//        //TODO: LÓGICA PARA REVISAR Y ACEPTAR PROYECTO DE ESTUDIANTE
//    }
//
//    public void verEstructuraProyecto() { //TODO: MUESTRA TODA LA INFORMACIÓN DEL PROYECTO
//
//    }
//
//    private void validarCamposTexto(String texto) {
//        if (texto == null || texto.isEmpty()) {
//            throw new IllegalArgumentException("El campo de texto no puede estar vacío o nulo.");
//        }
//    }
//
//    private void validarCuit(Long cuit) {
//        if (cuit == null) {
//            throw new IllegalArgumentException("El CUIT debe ser valido.");
//        }
//    }
//
//    private void validarId(Long id) {
//        if (id == null || id <= 0) {
//            throw new IllegalArgumentException("El ID debe ser un número positivo.");
//        }
//    }
//
//
//    private void cantidadDeHorasValidas(int cantidadHoras) {
//        if (cantidadHoras <= 0 || cantidadHoras > 200) {
//            throw new IllegalArgumentException("La cantidad de horas debe ser un número positivo y no puede superar las 200 horas.");
//        }
//    }
//
//    private void verificarCarga(Long cuitEntidad) {
//        List<Proyecto> proyectos = proyectoRepository.findAllByProyectoIdCuitEntidad(cuitEntidad);
//        for (Proyecto p : proyectos) {
//            System.out.println("Proyecto encontrado: " + p.getTitulo());
//        }
//    }
//
//    public List<Area> obtenerTodasLasAreas() {
//        return areaRepository.findAll();
//    }
//
//    public List<TutorExterno> obtenerTutoresDeEntidad(Long cuit) {
//        return tutorRepository.findAllByCuit(cuit);
//    }
//
//    public List<Proyecto> obtenerProyectosPorCuit(Long cuit) {
//        return proyectoRepository.findAllByProyectoIdCuitEntidad(cuit);
//    }
//
//    public List<Actividad> obtenerActividadesDePrueba(int cantidad) {
//        return actividadRepository.findAll().stream()
//                .limit(cantidad)
//                .toList();
//    }
//
//}
package ing.gpps.service;

import ing.gpps.entity.idClasses.ActividadId;
import ing.gpps.entity.idClasses.PlanDeTrabajoId;
import ing.gpps.entity.idClasses.ProyectoId;
import ing.gpps.entity.institucional.*;
import ing.gpps.entity.users.AdminEntidad;
import ing.gpps.entity.users.TutorExterno;
import ing.gpps.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdminEntidadService {
    private AdminEntidadRepository adminEntidadRepository;
    private TutorRepository tutorRepository;
    private EntidadRepository entidadRepository;
    private ProyectoRepository proyectoRepository;
    private PlanDeTrabajoRepository planDeTrabajoRepository;
    private ActividadRepository actividadRepository;
    private EstudianteRepository estuditanteRepository;
    private List<Actividad> actividades;
    private AreaRepository areaRepository;

    @Autowired
    private UsuarioService usuarioService;

    // ✅ AGREGAR ENTITYMANAGER
    @Autowired
    private EntityManager entityManager;


    public AdminEntidadService(AdminEntidadRepository adminEntidadRepository, TutorRepository tutorRepository, EntidadRepository entidadRepository, ProyectoRepository proyectoRepository, PlanDeTrabajoRepository planDeTrabajoRepository, ActividadRepository actividadRepository, EstudianteRepository estuditanteRepository, UsuarioService usuarioService, AreaRepository areaRepository) {
        this.adminEntidadRepository = adminEntidadRepository;
        this.tutorRepository = tutorRepository;
        this.entidadRepository = entidadRepository;
        this.proyectoRepository = proyectoRepository;
        this.planDeTrabajoRepository = planDeTrabajoRepository;
        this.actividadRepository = actividadRepository;
        this.estuditanteRepository = estuditanteRepository;
        this.usuarioService = usuarioService;
        this.areaRepository = areaRepository;
    }

    public void registrarAdministrador(AdminEntidad adminEntidad) {
        adminEntidadRepository.save(adminEntidad);
    }

    public void altaTutor(String nombre, String apellido, String email, String password, Long numTelefono, Long cuit) {
        validarCuit(cuit);
        validarCamposTexto(nombre);
        validarCamposTexto(apellido);
        validarCamposTexto(email);
        TutorExterno tutor = new TutorExterno(nombre, apellido, email, password, numTelefono);
        tutor.setCuit(cuit);
        if (tutorRepository.findByEmail(email).isEmpty()) {
            usuarioService.registrarUsuario(tutor);
        } else {
            throw new IllegalArgumentException("El email ya está registrado.");
        }
    }

//    public void bajaTutor(Long id) {
//        if (tutorRepository.findByProyectoId(id).isEmpty()) {
//            tutorRepository.deleteById(id);
//        } else {
//            throw new IllegalArgumentException("Tutor no encontrado.");
//        }
//    }

    public void generarActividad(int numeroProyecto, String tituloProyecto, Long cuitEntidad, String nombre, String descripcion, int cantidadHoras, boolean adjuntaArchivo) {
        validarCamposTexto(nombre);
        validarCamposTexto(descripcion);
        validarCamposTexto(tituloProyecto);
        validarCuit(cuitEntidad);
        cantidadDeHorasValidas(cantidadHoras);
        Proyecto proyecto = proyectoRepository.findByProyectoIdCuitEntidad(cuitEntidad);
        if (proyecto == null) {
            throw new EntityNotFoundException("No se encontró un proyecto para la entidad con CUIT: " + cuitEntidad);
        }
        if (this.actividades.stream().mapToInt(Actividad::getCantidadHoras).sum() + cantidadHoras > 200) {

            throw new IllegalArgumentException("La cantidad total de horas no puede superar las 200.");
        }
        Actividad actividad = new Actividad();
        actividad.setNombre(nombre);
        actividad.setDescripcion(descripcion);
        actividad.setCantidadHoras(cantidadHoras);
        actividad.setAdjuntaArchivo(adjuntaArchivo);
        actividad.setProyecto(proyecto);
        this.actividades.add(actividad);
    }

    public void solicitudBajaEntidad() {
        //TODO: Implementar lógica para solicitar baja de entidad a través de mensajes
    }

    public List<Proyecto> obtenerProyectosSinPlan(Long cuitEntidad) {
        List<Proyecto> proyectos = proyectoRepository.findWithoutPlanDeTrabajoByEntidad(cuitEntidad);
        if (proyectos.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron proyectos sin plan de trabajo para la entidad con CUIT: " + cuitEntidad);
        }
        return proyectos;
    }

    @Transactional
    public void generarPlanDeTrabajo(List<Actividad> actividadesFormulario, int numeroPlan, LocalDate inicio, LocalDate fin, String tituloProyecto, Long cuitEntidad) {
        System.out.println("🚀 Iniciando generación de plan de trabajo...");

        if (actividadesFormulario == null || actividadesFormulario.isEmpty()) {
            throw new IllegalArgumentException("La lista de actividades no puede ser nula o vacía.");
        }

        // 1. Crear el ID del proyecto y plan
        ProyectoId proyectoId = new ProyectoId(tituloProyecto, cuitEntidad);
        PlanDeTrabajoId planId = new PlanDeTrabajoId(numeroPlan, proyectoId);

        // 2. ✅ LIMPIAR LA SESIÓN COMPLETAMENTE
        System.out.println("🧹 Limpiando sesión de Hibernate...");
        entityManager.clear();

        // 3. Verificar si ya existe el plan ANTES de obtener el proyecto
        System.out.println("🔍 Verificando si ya existe el plan...");
        if (planDeTrabajoRepository.findById(planId).isPresent()) {
            throw new IllegalArgumentException("Ya existe un plan de trabajo con el número " + numeroPlan + " para el proyecto " + tituloProyecto);
        }

        // 4. Obtener el proyecto de forma simple (sin relaciones complejas)
        System.out.println("📋 Obteniendo proyecto...");
        Proyecto proyecto = proyectoRepository.findByProyectoId(proyectoId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el proyecto con título " + tituloProyecto + " y cuit " + cuitEntidad));

        // 5. ✅ DETACH del proyecto para evitar conflictos
        entityManager.detach(proyecto);

        // 6. Crear el plan con datos básicos
        System.out.println("📝 Creando nuevo plan de trabajo...");
        PlanDeTrabajo plan = new PlanDeTrabajo();
        plan.setId(planId);
        plan.setProyecto(proyecto);
        plan.setFechaInicio(inicio);
        plan.setFechaFin(fin);

        // 7. ✅ USAR MERGE EN LUGAR DE SAVE
        System.out.println("💾 Guardando plan con merge...");
        plan = entityManager.merge(plan);
        entityManager.flush();
        System.out.println("✅ Plan guardado exitosamente");

        // 8. Crear las actividades de forma simple
        System.out.println("🎯 Creando " + actividadesFormulario.size() + " actividades...");

        for (int i = 0; i < actividadesFormulario.size(); i++) {
            Actividad actividadFormulario = actividadesFormulario.get(i);

            // Crear actividad con datos básicos
            Actividad nuevaActividad = new Actividad();
            nuevaActividad.setNombre(actividadFormulario.getNombre());
            nuevaActividad.setDescripcion(actividadFormulario.getDescripcion());
            nuevaActividad.setCantidadHoras(actividadFormulario.getCantidadHoras());
            nuevaActividad.setAdjuntaArchivo(actividadFormulario.isAdjuntaArchivo());

            // Asignar IDs manualmente
            ActividadId actividadId = new ActividadId(i + 1, planId);
            nuevaActividad.setActividadId(actividadId);
            nuevaActividad.setPlanDeTrabajo(plan);

            // ✅ USAR MERGE PARA CADA ACTIVIDAD
            entityManager.merge(nuevaActividad);
            System.out.println("📌 Actividad " + (i + 1) + " guardada: " + nuevaActividad.getNombre());
        }

        // 9. Flush final
        entityManager.flush();
        System.out.println("🎉 Plan de trabajo guardado correctamente con " + actividadesFormulario.size() + " actividades");
    }

    @Transactional
    public void proponerProyectos(Proyecto proyecto) {
        if (proyecto.getEntidad() == null || proyecto.getTitulo() == null) {
            throw new IllegalArgumentException("El proyecto debe tener entidad y título definidos.");
        }

        ProyectoId proyectoId = new ProyectoId(proyecto.getTitulo(), proyecto.getEntidad().getCuit());
        proyecto.setProyectoId(proyectoId);

        proyectoRepository.save(proyecto);

        verificarCarga(proyecto.getProyectoId().getCuitEntidad()); // opcional para debug
    }


    public void modificarActividades(Actividad actividad, Long cuitEntidad, int cantidadHoras, String nombre, String descripcion, boolean adjuntaArchivo) {
        if (actividad == null) {
            throw new IllegalArgumentException("La lista de actividades no puede estar vacía.");
        }

        Proyecto proyecto = proyectoRepository.findByProyectoIdCuitEntidad(cuitEntidad);
        if (estuditanteRepository.findByProyecto(proyecto).isPresent()) {
            throw new RuntimeException("El proyecto ya tiene estudiantes asociados.");
        }
        if (proyecto == null) {
            throw new EntityNotFoundException("No se encontró un proyecto para la entidad con CUIT: " + cuitEntidad);
        }

        cantidadDeHorasValidas(cantidadHoras);
        validarCamposTexto(nombre);
        validarCamposTexto(descripcion);

        actividad.setCantidadHoras(cantidadHoras);
        actividad.setNombre(nombre);
        actividad.setDescripcion(descripcion);
        actividad.setAdjuntaArchivo(adjuntaArchivo);

    }

//    public void modificarDatosEntidad(Long cuit, String nombre, String ubicacion, TipoEntidad tipo, String email, String numeroTelefono) {
//        if (!validarCamposTexto(nombre) && !validarCamposTexto(ubicacion) &&
//                !validarCamposTexto(email) && !validarCuit(cuit) &&
//                entidadRepository.findByCuit(cuit).isPresent()) {
//
//            Entidad entidad = new Entidad(cuit, nombre, ubicacion, email, tipo, numeroTelefono);
//            entidadRepository.save(entidad);
//        } else {
//            throw new IllegalArgumentException("Entidad no encontrada.");
//        }
//    }

    public void aceptarProyectoEstudiante() {
        //REVISAR, CREO QUE DEBERÍA IR EN DIRECCIÓN DE CARRERA
        //TODO: LÓGICA PARA REVISAR Y ACEPTAR PROYECTO DE ESTUDIANTE
    }

    public void verEstructuraProyecto() { //TODO: MUESTRA TODA LA INFORMACIÓN DEL PROYECTO

    }

    private void validarCamposTexto(String texto) {
        if (texto == null || texto.isEmpty()) {
            throw new IllegalArgumentException("El campo de texto no puede estar vacío o nulo.");
        }
    }

    private void validarCuit(Long cuit) {
        if (cuit == null) {
            throw new IllegalArgumentException("El CUIT debe ser valido.");
        }
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo.");
        }
    }


    private void cantidadDeHorasValidas(int cantidadHoras) {
        if (cantidadHoras <= 0 || cantidadHoras > 200) {
            throw new IllegalArgumentException("La cantidad de horas debe ser un número positivo y no puede superar las 200 horas.");
        }
    }

    private void verificarCarga(Long cuitEntidad) {
        List<Proyecto> proyectos = proyectoRepository.findAllByProyectoIdCuitEntidad(cuitEntidad);
        for (Proyecto p : proyectos) {
            System.out.println("Proyecto encontrado: " + p.getTitulo());
        }
    }

    public List<Area> obtenerTodasLasAreas() {
        return areaRepository.findAll();
    }

    public List<TutorExterno> obtenerTutoresDeEntidad(Long cuit) {
        return tutorRepository.findAllByCuit(cuit);
    }

    public List<Proyecto> obtenerProyectosPorCuit(Long cuit) {
        return proyectoRepository.findAllByProyectoIdCuitEntidad(cuit);
    }

    public List<Actividad> obtenerActividadesDePrueba(int cantidad) {
        return actividadRepository.findAll().stream()
                .limit(cantidad)
                .toList();
    }

    public List<PlanDeTrabajo> ObtenerPlanPorEntidad(Long cuit) {
        return planDeTrabajoRepository.findByCuitEntidad(cuit);
    }
}


