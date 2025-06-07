package ing.gpps.entity.institucional;

public enum TipoEntidad {
    EMPRESA("EMPRESA"),
    ONG("ONG"),
    INSTITUCION_PUBLICA("INSTITUCION_PUBLICA"),
    ORGANISMO_PUBLICO("ORGANISMO_PUBLICO"),
    INSTITUCION_SALUD("INSTITUCION_SALUD");

    private final String descripcion;

    TipoEntidad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
