package ing.gpps.entity.institucional;

import ing.gpps.entity.idClasses.ProyectoId;
import ing.gpps.entity.users.DocenteSupervisor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanDeTrabajoTest {

    private PlanDeTrabajo planDeTrabajo;
    private Proyecto proyecto;
    private DocenteSupervisor docenteSupervisor;
    private Entidad entidad;

    @BeforeEach
    void setUp() {
        // Crear entidad
        entidad = new Entidad();
        entidad.setCuit(30123456789L);
        entidad.setNombre("Empresa Test");
        entidad.setUbicacion("Buenos Aires");
        entidad.setEmail("empresa@test.com");
        entidad.setTipo(TipoEntidad.EMPRESA);

        // Crear docente supervisor
        docenteSupervisor = new DocenteSupervisor();
        docenteSupervisor.setId(1L);
        docenteSupervisor.setNombre("Juan");
        docenteSupervisor.setApellido("Pérez");

        // Crear proyecto
        proyecto = new Proyecto();
        proyecto.setProyectoId(new ProyectoId("Proyecto Test", entidad.getCuit()));
        proyecto.setEntidad(entidad);

        // Crear plan de trabajo
        planDeTrabajo = new PlanDeTrabajo(1L, LocalDate.now(), LocalDate.now().plusMonths(6), proyecto);
    }

    @Test
    void testCrearPlanDeTrabajo() {
        assertNotNull(planDeTrabajo);
        assertEquals(1L, planDeTrabajo.getPlanDeTrabajoId().numero());
        assertEquals(proyecto.getProyectoId(), planDeTrabajo.getPlanDeTrabajoId().proyectoId());
        assertEquals(proyecto, planDeTrabajo.getProyecto());
        assertTrue(planDeTrabajo.getActividades().isEmpty());
    }

    @Test
    void testAgregarActividad() {
        // Crear una nueva actividad
        Actividad actividad = new Actividad();
        actividad.setId(1L);
        actividad.setPlanDeTrabajo(planDeTrabajo);
        actividad.setTitulo("Actividad de prueba");
        actividad.setDescripcion("Descripción de prueba");
        actividad.setHoras(10);
        actividad.setDocenteSupervisor(docenteSupervisor);

        // Agregar la actividad al plan de trabajo
        planDeTrabajo.agregarActividad(actividad);

        // Verificar que la actividad fue agregada correctamente
        assertFalse(planDeTrabajo.getActividades().isEmpty());
        assertEquals(1, planDeTrabajo.getActividades().size());
        assertTrue(planDeTrabajo.getActividades().contains(actividad));
        assertEquals(planDeTrabajo, actividad.getPlanDeTrabajo());
    }

    @Test
    void testRemoverActividad() {
        Actividad actividad = new Actividad();
        actividad.setTitulo("Actividad Test");
        actividad.setDescripcion("Descripción de prueba");
        actividad.setHoras(10);
        actividad.setEstado(Actividad.EstadoActividad.EN_REVISION);
        actividad.setDocenteSupervisor(docenteSupervisor);

        planDeTrabajo.addActividad(actividad);
        assertEquals(1, planDeTrabajo.getActividades().size());

        planDeTrabajo.removeActividad(actividad);
        assertTrue(planDeTrabajo.getActividades().isEmpty());
        assertNull(actividad.getPlanDeTrabajo());
    }

    @Test
    void testSetProyecto() {
        Proyecto nuevoProyecto = new Proyecto();
        nuevoProyecto.setProyectoId(new ProyectoId("Nuevo Proyecto", entidad.getCuit()));
        nuevoProyecto.setEntidad(entidad);

        planDeTrabajo.setProyecto(nuevoProyecto);

        assertEquals(nuevoProyecto, planDeTrabajo.getProyecto());
        assertEquals(planDeTrabajo, nuevoProyecto.getPlanDeTrabajo());
    }
}
