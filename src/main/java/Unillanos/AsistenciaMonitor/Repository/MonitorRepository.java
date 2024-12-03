package Unillanos.AsistenciaMonitor.Repository;

import Unillanos.AsistenciaMonitor.Entity.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MonitorRepository extends JpaRepository<Monitor, Long> {
    List<Monitor> findByDiasAsignadosContaining(String dia);

}
