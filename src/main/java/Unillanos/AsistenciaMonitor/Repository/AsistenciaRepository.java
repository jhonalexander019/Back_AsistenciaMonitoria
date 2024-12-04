package Unillanos.AsistenciaMonitor.Repository;

import Unillanos.AsistenciaMonitor.Entity.Asistencia;
import Unillanos.AsistenciaMonitor.Entity.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

}
