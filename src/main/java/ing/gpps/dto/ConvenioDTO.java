package ing.gpps.dto;

import java.time.LocalDate;

public class ConvenioDTO {
    private Long id;
    private String numeroConvenio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private String observaciones;
    
    // Referencias a otras entidades
    private Long estudianteId;
    private Long proyectoId;
    private Long tutorExternoId;
    private Long entidadId;
    private Long docenteSupervisorId;
    private Long direccionCarreraId;

    // Constructor vacío
    public ConvenioDTO() {
    }

    // Constructor con todos los campos
    public ConvenioDTO(Long id, String numeroConvenio, LocalDate fechaInicio, LocalDate fechaFin,
                      String estado, String observaciones, Long estudianteId, Long proyectoId,
                      Long tutorExternoId, Long entidadId, Long docenteSupervisorId,
                      Long direccionCarreraId) {
        this.id = id;
        this.numeroConvenio = numeroConvenio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.observaciones = observaciones;
        this.estudianteId = estudianteId;
        this.proyectoId = proyectoId;
        this.tutorExternoId = tutorExternoId;
        this.entidadId = entidadId;
        this.docenteSupervisorId = docenteSupervisorId;
        this.direccionCarreraId = direccionCarreraId;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroConvenio() {
        return numeroConvenio;
    }

    public void setNumeroConvenio(String numeroConvenio) {
        this.numeroConvenio = numeroConvenio;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public Long getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Long proyectoId) {
        this.proyectoId = proyectoId;
    }

    public Long getTutorExternoId() {
        return tutorExternoId;
    }

    public void setTutorExternoId(Long tutorExternoId) {
        this.tutorExternoId = tutorExternoId;
    }

    public Long getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(Long entidadId) {
        this.entidadId = entidadId;
    }

    public Long getDocenteSupervisorId() {
        return docenteSupervisorId;
    }

    public void setDocenteSupervisorId(Long docenteSupervisorId) {
        this.docenteSupervisorId = docenteSupervisorId;
    }

    public Long getDireccionCarreraId() {
        return direccionCarreraId;
    }

    public void setDireccionCarreraId(Long direccionCarreraId) {
        this.direccionCarreraId = direccionCarreraId;
    }
} 