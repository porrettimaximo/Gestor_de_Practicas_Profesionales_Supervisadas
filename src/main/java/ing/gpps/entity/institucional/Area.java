package ing.gpps.entity.institucional;

import jakarta.persistence.*;

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

    // Constructor vacío
    public Area() {
    }

    // Getters y setters
    public String nombre() {
        return nombre;
    }
}
