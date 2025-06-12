package ing.gpps.repository;


import ing.gpps.notificaciones.Notificacion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    /**
     * Encontrar todas las notificaciones de un usuario ordenadas por fecha (más recientes primero)
     */
    List<Notificacion> findByDestinatarioIdOrderByFechaCreacionDesc(Long destinatarioId);

    /**
     * Encontrar notificaciones de un usuario con paginación
     */
    List<Notificacion> findByDestinatarioIdOrderByFechaCreacionDesc(Long destinatarioId, Pageable pageable);

    /**
     * Encontrar solo las notificaciones no leídas de un usuario
     */
    List<Notificacion> findByDestinatarioIdAndLeidaFalseOrderByFechaCreacionDesc(Long destinatarioId);

    /**
     * Contar notificaciones no leídas de un usuario
     */
    long countByDestinatarioIdAndLeidaFalse(Long destinatarioId);

    /**
     * Contar todas las notificaciones de un usuario
     */
    long countByDestinatarioId(Long destinatarioId);

    /**
     * Encontrar notificaciones por tipo
     */
    List<Notificacion> findByDestinatarioIdAndTipoOrderByFechaCreacionDesc(Long destinatarioId, String tipo);

    /**
     * Encontrar notificaciones importantes
     */
    List<Notificacion> findByDestinatarioIdAndImportanteTrueOrderByFechaCreacionDesc(Long destinatarioId);

    /**
     * Encontrar notificaciones anteriores a una fecha (para limpieza)
     */
    List<Notificacion> findByFechaCreacionBefore(LocalDateTime fecha);

    /**
     * Encontrar notificaciones por emisor
     */
    List<Notificacion> findByEmisorIdOrderByFechaCreacionDesc(Long emisorId);

    /**
     * Encontrar notificaciones entre fechas
     */
    List<Notificacion> findByDestinatarioIdAndFechaCreacionBetweenOrderByFechaCreacionDesc(
            Long destinatarioId, LocalDateTime inicio, LocalDateTime fin);

    /**
     * Consulta personalizada para obtener notificaciones recientes no leídas
     */
    @Query("SELECT n FROM Notificacion n WHERE n.destinatario.id = :usuarioId " +
            "AND n.leida = false AND n.fechaCreacion >= :fechaLimite " +
            "ORDER BY n.importante DESC, n.fechaCreacion DESC")
    List<Notificacion> findNotificacionesRecientesNoLeidas(
            @Param("usuarioId") Long usuarioId,
            @Param("fechaLimite") LocalDateTime fechaLimite);

    /**
     * Consulta para obtener resumen de notificaciones por tipo
     */
    @Query("SELECT n.tipo, COUNT(n) FROM Notificacion n WHERE n.destinatario.id = :usuarioId GROUP BY n.tipo")
    List<Object[]> getResumenPorTipo(@Param("usuarioId") Long usuarioId);

    /**
     * Eliminar notificaciones leídas antiguas
     */
    @Query("DELETE FROM Notificacion n WHERE n.leida = true AND n.fechaCreacion < :fechaLimite")
    int deleteNotificacionesLeidasAntiguas(@Param("fechaLimite") LocalDateTime fechaLimite);

    /**
     * Buscar notificaciones por contenido del mensaje
     */
    @Query("SELECT n FROM Notificacion n WHERE n.destinatario.id = :usuarioId " +
            "AND LOWER(n.mensaje) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
            "ORDER BY n.fechaCreacion DESC")
    List<Notificacion> buscarPorMensaje(@Param("usuarioId") Long usuarioId, @Param("busqueda") String busqueda);
}
