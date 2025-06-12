package ing.gpps.notificaciones;

public enum TipoEvento {
    PROYECTO_CREADO("Proyecto creado"),
    PROYECTO_MODIFICADO("Proyecto modificado"),
    PROYECTO_COMPLETADO("Proyecto completado"),
    DOCUMENTO_SUBIDO("Documento subido"),
    EVALUACION_REALIZADA("Evaluación realizada"),
    COMENTARIO_AGREGADO("Comentario agregado"),
    FECHA_LIMITE_PROXIMA("Fecha límite próxima");

    private final String descripcion;

    TipoEvento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
