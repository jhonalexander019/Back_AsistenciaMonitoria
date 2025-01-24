package Unillanos.AsistenciaMonitor.Repository;

import Unillanos.AsistenciaMonitor.Entity.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface SemestreRepository extends JpaRepository<Semestre, Long> {
    @Query("SELECT s FROM Semestre s WHERE " +
            "(:fechaInicio BETWEEN s.fechaInicio AND s.fechaFin OR " +
            ":fechaFin BETWEEN s.fechaInicio AND s.fechaFin OR " +
            "(s.fechaInicio <= :fechaInicio AND s.fechaFin >= :fechaFin))")
    Optional<Semestre> findSemestreConFechaIncluida(LocalDate fechaInicio, LocalDate fechaFin);
}
