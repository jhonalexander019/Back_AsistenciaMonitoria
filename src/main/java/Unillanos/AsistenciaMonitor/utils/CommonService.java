package Unillanos.AsistenciaMonitor.utils;

import Unillanos.AsistenciaMonitor.entity.Monitor;
import Unillanos.AsistenciaMonitor.entity.Semestre;
import Unillanos.AsistenciaMonitor.entity.Usuario;
import Unillanos.AsistenciaMonitor.exception.ErrorMessages;
import Unillanos.AsistenciaMonitor.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class CommonService {

    private final MonitorRepository monitorRepository;
    private final SemestreRepository semestreRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public CommonService(MonitorRepository monitorRepository,
                         SemestreRepository semestreRepository, UsuarioRepository usuarioRepository) {

        this.monitorRepository = monitorRepository;
        this.semestreRepository = semestreRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Semestre obtenerSemestreVigente() {
        LocalDate fechaActual = LocalDate.now();
        return semestreRepository.findAll().stream()
                .filter(semestre -> !fechaActual.isBefore(semestre.getFechaInicio()) && !fechaActual.isAfter(semestre.getFechaFin()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(ErrorMessages.INVALID_SEMESTER));
    }

    public Monitor obtenerMonitorPorId(Long id) {
        Monitor monitor = monitorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.MONITOR_NOT_FOUND));

        if (Objects.equals(monitor.getSemestre().getId(), obtenerSemestreVigente().getId())) {
            return monitor;
        }
        throw new RuntimeException(ErrorMessages.INVALID_MONITOR);
    }

    public Monitor obtenerMonitorPorIdUsuario(Long id) {
        return monitorRepository.findByUsuarioId(id);
    }

    public Usuario obtenerUsuarioPorCodigo(Integer codigo) {
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.INVALID_SESSION));
    }


}
