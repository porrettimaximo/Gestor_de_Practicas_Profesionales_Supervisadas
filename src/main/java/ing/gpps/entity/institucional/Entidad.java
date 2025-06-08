package ing.gpps.entity.institucional;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Entidad {
    @Id
    @Column(name = "cuit", nullable = false)
    private Long cuit;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "ubicacion", nullable = false)
    private String ubicacion; //TODO: MODELAR

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "tipo", nullable = false)
    private TipoEntidad tipo;

    @Column(name = "telefono")
    private String telefono;

    @OneToMany
    @JoinColumn(name = "fk_cuit_entidad")
    private List<Proyecto> proyectos;

//    @OneToMany
//    @JoinColumn(name = "cuit_entidad")
//    private List<AdminEntidad> administradores;

    // Constructor completo
    public Entidad(Long cuit, String nombre, String ubicacion, String email, TipoEntidad empresa, String telefono) {
        this.cuit = cuit;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.email = email;
        this.tipo = empresa;
        this.telefono = telefono;
    }

    // Getters y setters
    public String nombre() {
        return nombre;
    }

    public Long cuit() {
        return cuit;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCuit(Long cuit) {
        this.cuit = cuit;
    }

    public String ubicacion() {
        return ubicacion;
    }


    public void setTipo(TipoEntidad tipoEntidad) {
        this.tipo = tipoEntidad;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String email() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoEntidad tipo() {
        return tipo;
    }

    public Long getCuit() {
        return this.cuit;
    }
}

