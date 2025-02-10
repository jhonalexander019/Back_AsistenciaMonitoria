package Unillanos.AsistenciaMonitor.service.helpers;

import Unillanos.AsistenciaMonitor.entity.Monitor;
import Unillanos.AsistenciaMonitor.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class AsistenciaServiceHelper {
    private final AsistenciaRepository asistenciaRepository;
    @Autowired
    public AsistenciaServiceHelper(AsistenciaRepository asistenciaRepository) {
        this.asistenciaRepository =asistenciaRepository;
    }

    public boolean validarAsistencia(Monitor monitor, LocalDateTime start, LocalDateTime end, String jornada){
        return asistenciaRepository.existsByMonitorAndFechaBetweenAndJornada(
                monitor,
                start,
                end,
                jornada
        );
    }
}
