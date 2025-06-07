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

    @Column
    private int horas;

    @Column(name = "fecha_limite")
    private LocalDate fechaLimite;

    @Enumerated(EnumType.STRING)
    private EstadoActividad estado;

    @Column(columnDefinition = "TEXT")
    private String comentarios;

    @Column(name = "ruta_archivo")
    private String rutaArchivo;

    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Informe> informes = new ArrayList<>();

    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Entrega> entregas = new ArrayList<>();

    public Actividad(int numero, String nombre, String descripcion, PlanDeTrabajo planDeTrabajo) {
        this.actividadId = new ActividadId(numero, planDeTrabajo.planDeTrabajoId());
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.planDeTrabajo = planDeTrabajo;
        this.estado = EstadoActividad.EN_REVISION;
    }

    protected Actividad() {
    }

    public ActividadId actividadId() {
        return actividadId;
    }

    public void setPlanDeTrabajo(PlanDeTrabajo planDeTrabajo) {
        this.planDeTrabajo = planDeTrabajo;
    }

    public void addInforme(Informe informe) {
        if (informe != null) {
            informes.add(informe);
            informe.setActividad(this);
        }
    }

    public void removeInforme(Informe informe) {
        if (informe != null) {
            informes.remove(informe);
            informe.setActividad(null);
        }
    }

    public List<Informe> getInformes() {
        return new ArrayList<>(informes);
    }

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

    public List<Entrega> getEntregasPendientes() {
        return entregas.stream()
                .filter(entrega -> entrega.getEstado() == Entrega.EstadoEntrega.PENDIENTE)
                .collect(Collectors.toList());
    }

    public boolean tieneEntregasVencidas() {
        LocalDate hoy = LocalDate.now();
        return entregas.stream()
                .anyMatch(entrega -> entrega.getFechaLimite() != null &&
                        entrega.getFechaLimite().isBefore(hoy) &&
                        entrega.getEstado() == Entrega.EstadoEntrega.PENDIENTE);
    }

    public enum EstadoActividad {
        EN_REVISION,
        EN_CURSO,
        COMPLETADA,
        RECHAZADA
    }
}