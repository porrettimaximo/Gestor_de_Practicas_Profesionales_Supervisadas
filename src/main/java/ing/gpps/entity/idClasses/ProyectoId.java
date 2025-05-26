package ing.gpps.entity.idClasses;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProyectoId implements Serializable {

    @Column(nullable = false)
    private String titulo;

    @Column(name = "fk_cuit_entidad", nullable = false)
    private Long cuitEntidad;

    public ProyectoId() {
    }

    public ProyectoId(String titulo, Long cuitEntidad) {
        this.titulo = titulo;
        this.cuitEntidad = cuitEntidad;
    }

    public String titulo() {
        return titulo;
    }

    public Long cuitEntidad() {
        return cuitEntidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProyectoId that)) return false;
        return titulo.equals(that.titulo) && cuitEntidad.equals(that.cuitEntidad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, cuitEntidad);
    }
}