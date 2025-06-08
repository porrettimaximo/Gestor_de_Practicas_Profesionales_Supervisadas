package ing.gpps.entity.institucional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

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

    public Area(String nombre) {
        this.nombre = nombre;
    }

    // Constructor vacío
    public Area() {
    }

    // Getters y setters
    public String nombre() {
        return nombre;
    }
}
