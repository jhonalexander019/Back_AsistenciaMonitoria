package Unillanos.AsistenciaMonitor.Utils;

import Unillanos.AsistenciaMonitor.Repository.MonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtils {

    private final MonitorRepository monitorRepository;

    @Autowired
    public ValidationUtils(MonitorRepository monitorRepository) {
        this.monitorRepository = monitorRepository;
    }

    public void validarMonitorExistente(String correo, Long semestreId) {
        if (monitorRepository.existsByUsuario_CorreoAndSemestre_Id(correo, semestreId)) {
            throw new RuntimeException(ErrorMessages.MONITOR_DUPLICATE_EMAIL);
        }
    }

    public void validarDia(String dia) {
        if (dia == null || dia.trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_DAY);
        }
    }
}
