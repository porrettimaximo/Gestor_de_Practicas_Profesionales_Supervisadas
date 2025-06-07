package ing.gpps.dto;

import java.time.LocalDate;

public class ActividadRequest {
    private String nombre;
    private String descripcion;
    private int horas;
    private String tituloProyecto;
    private Long cuitEntidad;
    private LocalDate fechaLimite;

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public int getHoras() { return horas; }
    public void setHoras(int horas) { this.horas = horas; }
    
    public String getTituloProyecto() { return tituloProyecto; }
    public void setTituloProyecto(String tituloProyecto) { this.tituloProyecto = tituloProyecto; }
    
    public Long getCuitEntidad() { return cuitEntidad; }
    public void setCuitEntidad(Long cuitEntidad) { this.cuitEntidad = cuitEntidad; }

    public LocalDate getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }
} 