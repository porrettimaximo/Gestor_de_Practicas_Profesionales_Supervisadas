package ing.gpps.entity.institucional;

import ing.gpps.entity.idClasses.ActividadId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class Actividad {

    @EmbeddedId
    private ActividadId actividadId;

    @ManyToOne
    @MapsId("planDeTrabajoId")
    @JoinColumns({
            @JoinColumn(name = "fk_numero_planDeTrabajo", referencedColumnName = "numero"),
            @JoinColumn(name = "fk_titulo_proyecto", referencedColumnName = "titulo_proyecto"),
            @JoinColumn(name = "fk_cuit_entidad", referencedColumnName = "cuit_entidad")
    })
    private PlanDeTrabajo planDeTrabajo;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private boolean adjuntaArchivo;

    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Informe> informes = new ArrayList<>();

    // NUEVA RELACIÓN: Actividad puede tener múltiples entregas
    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Entrega> entregas = new ArrayList<>();

    public Actividad(int numero, String nombre, String descripcion, boolean adjuntaArchivo, PlanDeTrabajo planDeTrabajo) {
        this.actividadId = new ActividadId(numero, planDeTrabajo.planDeTrabajoId());
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.adjuntaArchivo = adjuntaArchivo;
        this.planDeTrabajo = planDeTrabajo;
    }

    protected Actividad() {
    }

    public ActividadId actividadId() {
        return actividadId;
    }

    public void setPlanDeTrabajo(PlanDeTrabajo planDeTrabajo) {
        this.planDeTrabajo = planDeTrabajo;
    }

    // Métodos para manejar informes (mantienen la lógica existente)
    public void addInforme(Informe informe) {
        if (!informes.contains(informe)) {
            informes.add(informe);
            informe.setActividad(this);
        }
    }

    public void removeInforme(Informe informe) {
        if (informes.contains(informe)) {
            informes.remove(informe);
            informe.setActividad(null);
        }
    }

    public List<Informe> getInformes() {
        return new ArrayList<>(informes);
    }

    // NUEVOS MÉTODOS para manejar entregas
    public void addEntrega(Entrega entrega) {
        if (!entregas.contains(entrega)) {
            entregas.add(entrega);
            entrega.setActividad(this);
        }
    }

    public void removeEntrega(Entrega entrega) {
        if (entregas.contains(entrega)) {
            entregas.remove(entrega);
            entrega.setActividad(null);
        }
    }

    public List<Entrega> getEntregas() {
        return new ArrayList<>(entregas);
    }

    // Método de conveniencia para obtener todas las entregas pendientes
    public List<Entrega> getEntregasPendientes() {
        return entregas.stream()
                .filter(entrega -> entrega.getEstado() == Entrega.EstadoEntrega.PENDIENTE)
                .collect(Collectors.toList());
    }

    // Método de conveniencia para verificar si la actividad tiene entregas vencidas
    public boolean tieneEntregasVencidas() {
        LocalDate hoy = LocalDate.now();
        return entregas.stream()
                .anyMatch(entrega -> entrega.getFechaLimite() != null &&
                        entrega.getFechaLimite().isBefore(hoy) &&
                        entrega.getEstado() == Entrega.EstadoEntrega.PENDIENTE);
    }
}