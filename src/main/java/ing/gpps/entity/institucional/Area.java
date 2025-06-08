package ing.gpps.entity.institucional;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Area {
    @Id
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "area")
    private List<Proyecto> proyectos;

    public Area(String nombre, List<Proyecto> proyectos) {
        this.nombre = nombre;
        this.proyectos = proyectos;
    }
}
