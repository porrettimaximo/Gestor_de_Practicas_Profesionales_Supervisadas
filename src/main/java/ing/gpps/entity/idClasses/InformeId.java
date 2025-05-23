package ing.gpps.entity.idClasses;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class InformeId implements Serializable {

    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int numero;

    @Column(name = "estudiante_dni", nullable = false)
    private Long estudianteDni;

    public InformeId() {
    }

    public InformeId(int numero, Long estudianteDni) {
        this.numero = numero;
        this.estudianteDni = estudianteDni;
    }

    public int numero() {
        return numero;
    }

    public Long estudianteDni() {
        return estudianteDni;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InformeId that)) return false;
        return numero == that.numero && estudianteDni.equals(that.estudianteDni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, estudianteDni);
    }
}



