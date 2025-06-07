package ing.gpps.entity.idClasses;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class InformeId implements Serializable {

    @Column(nullable = false)
    private int numero;

    @Column(name = "estudiante_dni", nullable = false)
    private Integer estudianteDni;

    public InformeId() {
    }

    public InformeId(int numero, Integer estudianteDni) {
        this.numero = numero;
        this.estudianteDni = estudianteDni;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InformeId that)) return false;
        return numero == that.numero && Objects.equals(estudianteDni, that.estudianteDni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, estudianteDni);
    }
}



