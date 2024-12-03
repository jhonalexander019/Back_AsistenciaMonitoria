package Unillanos.AsistenciaMonitor.Repository;

import Unillanos.AsistenciaMonitor.Entity.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MonitorRepository extends JpaRepository<Monitor, Long> {
    // Método para obtener todos los monitores
    List<Monitor> findAll();

    // Método para obtener monitores que contienen un día específico en su horario
    List<Monitor> findByDiasAsignadosContaining(String dia);

    // Método para obtener un monitor por su ID
    Optional<Monitor> findById(Long id);

    // Método para eliminar un monitor por su ID
    void delete(Monitor monitor);


}
