package Unillanos.AsistenciaMonitor.repository;

import Unillanos.AsistenciaMonitor.entity.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MonitorRepository extends JpaRepository<Monitor, Long> {
    boolean existsByUsuario_CorreoAndSemestre_Id(String correo, Long semestreId);
    List<Monitor> findByDiasAsignadosContaining(String dia);
    List<Monitor> findBySemestreId(Long semestreId);
    Monitor findByUsuarioId(Long usuarioId);
    boolean existsBySemestreId(Long semestreId);

}
