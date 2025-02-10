package Unillanos.AsistenciaMonitor.service.helpers;

import Unillanos.AsistenciaMonitor.repository.MonitorRepository;
import Unillanos.AsistenciaMonitor.repository.SemestreRepository;
import Unillanos.AsistenciaMonitor.entity.Semestre;
import Unillanos.AsistenciaMonitor.exception.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SemestreServiceHelper {

    private final SemestreRepository semestreRepository;
    private final MonitorRepository monitorRepository;

    @Autowired
    public SemestreServiceHelper(SemestreRepository semestreRepository,MonitorRepository monitorRepository) {
        this.semestreRepository = semestreRepository;
        this.monitorRepository=monitorRepository;
    }

    public Semestre obtenerSemestrePorId(Long semestreId) {
        return semestreRepository.findById(semestreId)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.SEMESTER_NOT_FOUND));
    }
    public boolean obtenerSemestrePorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        return semestreRepository.findSemestreConFechaIncluida(fechaInicio, fechaFin).isEmpty();
    }
    public boolean validarExistenciaMonitoresEnSemestreId(Long semestreId){
        return monitorRepository.existsBySemestreId(semestreId);

    }
}
