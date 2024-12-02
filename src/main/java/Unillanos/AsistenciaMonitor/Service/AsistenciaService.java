package Unillanos.AsistenciaMonitor.Service;

import Unillanos.AsistenciaMonitor.Entity.Asistencia;
import Unillanos.AsistenciaMonitor.Entity.Monitor;
import Unillanos.AsistenciaMonitor.Repository.AsistenciaRepository;
import Unillanos.AsistenciaMonitor.Repository.MonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AsistenciaService {
    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private MonitorRepository monitorRepository;

    public Asistencia registrarAsistencia(Long monitorId, LocalDate fecha, String estado, Integer horasTrabajadas) {
        Monitor monitor = monitorRepository.findById(monitorId)
                .orElseThrow(() -> new RuntimeException("Monitor no encontrado"));

        Asistencia asistencia = new Asistencia();
        asistencia.setMonitor(monitor);
        asistencia.setFecha(fecha);
        asistencia.setEstado(estado);
        asistencia.setHorasCubiertas(horasTrabajadas);
        return asistenciaRepository.save(asistencia);
    }
}
