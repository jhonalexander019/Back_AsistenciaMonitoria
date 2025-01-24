package Unillanos.AsistenciaMonitor.Repository;

import Unillanos.AsistenciaMonitor.Entity.Asistencia;
import Unillanos.AsistenciaMonitor.Entity.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    @Query("SELECT a FROM Asistencia a WHERE a.monitor.id = :monitorId AND a.estado = :estado")
    List<Asistencia> findByMonitorIdAndEstado(Long monitorId, String estado);
    boolean existsByMonitorAndFechaBetweenAndJornada(
            Monitor monitor, LocalDateTime start, LocalDateTime end, String jornada
    );

    List<Asistencia> findByMonitorId(Long monitorId);



}
