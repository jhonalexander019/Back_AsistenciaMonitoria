package Unillanos.AsistenciaMonitor.repository;

import Unillanos.AsistenciaMonitor.entity.Asistencia;
import Unillanos.AsistenciaMonitor.entity.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    @Query("SELECT a FROM Asistencia a WHERE a.monitor.id = :monitorId AND a.estado = :estado")
    List<Asistencia> findByMonitorIdAndEstado(Long monitorId, String estado);
    boolean existsByMonitorAndFechaBetweenAndJornada(
            Monitor monitor, LocalDateTime start, LocalDateTime end, String jornada
    );
    @Query("SELECT SUM(a.horasCubiertas) FROM Asistencia a WHERE a.monitor.id IN :monitor_id AND a.estado = :estado")
    Double sumarHorasCubiertasPorMonitores(@Param("monitor_id") List<Long> monitorIds, @Param("estado") String estado);

    List<Asistencia> findByMonitorId(Long monitorId);



}
