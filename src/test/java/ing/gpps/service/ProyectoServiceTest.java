package ing.gpps.service;

import ing.gpps.entity.institucional.Actividad;
import ing.gpps.entity.institucional.PlanDeTrabajo;
import ing.gpps.entity.institucional.Proyecto;
import ing.gpps.repository.ProyectoRepository;
import ing.gpps.repository.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ProyectoServiceTest {

    @Mock
    private ProyectoRepository proyectoRepository;

    @Mock
    private SolicitudRepository solicitudRepository;

    @InjectMocks
    private ProyectoService proyectoService;

    private Proyecto proyecto;
    private PlanDeTrabajo planDeTrabajo;
    private List<Actividad> actividades;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Crear proyecto de prueba
        proyecto = new Proyecto();
        planDeTrabajo = new PlanDeTrabajo();
        actividades = new ArrayList<>();
        
        // Configurar actividades de prueba
        Actividad actividad1 = new Actividad();
        actividad1.setCantidadHoras(50);
        actividad1.setEstado(Actividad.EstadoActividad.COMPLETADA);
        
        Actividad actividad2 = new Actividad();
        actividad2.setCantidadHoras(75);
        actividad2.setEstado(Actividad.EstadoActividad.COMPLETADA);
        
        Actividad actividad3 = new Actividad();
        actividad3.setCantidadHoras(75);
        actividad3.setEstado(Actividad.EstadoActividad.EN_REVISION);
        
        actividades.add(actividad1);
        actividades.add(actividad2);
        actividades.add(actividad3);
        
        planDeTrabajo.setActividades(actividades);
        proyecto.setPlanDeTrabajo(planDeTrabajo);
    }

    @Test
    void calcularProgreso_ConActividadesCompletadas_DeberiaCalcularCorrectamente() {
        // Arrange
        // Las actividades ya están configuradas en setUp()
        // Total de horas: 200 (50 + 75 + 75)
        // Horas completadas: 125 (50 + 75)
        // Progreso esperado: (125/200) * 100 = 62.5%

        // Act
        double progreso = proyectoService.calcularProgreso(proyecto);

        // Assert
        assertEquals(62.5, progreso, 0.01, "El progreso debería ser 62.5%");
    }

    @Test
    void calcularProgreso_ConProyectoNulo_DeberiaRetornarCero() {
        // Act
        double progreso = proyectoService.calcularProgreso(null);

        // Assert
        assertEquals(0.0, progreso, 0.01, "El progreso debería ser 0% para un proyecto nulo");
    }

    @Test
    void calcularProgreso_ConPlanDeTrabajoNulo_DeberiaRetornarCero() {
        // Arrange
        proyecto.setPlanDeTrabajo(null);

        // Act
        double progreso = proyectoService.calcularProgreso(proyecto);

        // Assert
        assertEquals(0.0, progreso, 0.01, "El progreso debería ser 0% para un plan de trabajo nulo");
    }

    @Test
    void calcularProgreso_ConListaActividadesVacia_DeberiaRetornarCero() {
        // Arrange
        planDeTrabajo.setActividades(new ArrayList<>());

        // Act
        double progreso = proyectoService.calcularProgreso(proyecto);

        // Assert
        assertEquals(0.0, progreso, 0.01, "El progreso debería ser 0% para una lista de actividades vacía");
    }

    @Test
    void calcularProgreso_ConTodasActividadesCompletadas_DeberiaRetornarCien() {
        // Arrange
        actividades.forEach(actividad -> actividad.setEstado(Actividad.EstadoActividad.COMPLETADA));

        // Act
        double progreso = proyectoService.calcularProgreso(proyecto);

        // Assert
        assertEquals(100.0, progreso, 0.01, "El progreso debería ser 100% cuando todas las actividades están completadas");
    }
} 