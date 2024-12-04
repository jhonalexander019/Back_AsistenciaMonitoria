package Unillanos.AsistenciaMonitor.Service;

import Unillanos.AsistenciaMonitor.Entity.Asistencia;
import Unillanos.AsistenciaMonitor.Entity.Monitor;
import Unillanos.AsistenciaMonitor.Repository.AsistenciaRepository;
import Unillanos.AsistenciaMonitor.Repository.MonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;


@Service
public class AsistenciaService {
    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private MonitorRepository monitorRepository;

    public Asistencia registrarAsistencia(Long monitorId, String estado, Integer horasTrabajadas) {
        Monitor monitor = monitorRepository.findById(monitorId)
                .orElseThrow(() -> new RuntimeException("Monitor no encontrado"));

        Asistencia asistencia = new Asistencia();
        asistencia.setMonitor(monitor);
        asistencia.setFecha(LocalDateTime.now());
        asistencia.setEstado(estado);
        asistencia.setHorasCubiertas(horasTrabajadas);
        return asistenciaRepository.save(asistencia);
    }

    //Control de horas
    public List<Map<String, Object>> listarAsistenciasPorEstadoYSemestre(Long monitorId, String estado, String semestre) {
        // Filtrar asistencias según el monitor, estado y semestre
        List<Asistencia> asistencias = asistenciaRepository.findByMonitorIdAndEstado(monitorId, estado);

        return asistencias.stream()
                .filter(asistencia -> asistencia.getMonitor().getSemestre().getNombre().equalsIgnoreCase(semestre))
                .map(asistencia -> {
                    Map<String, Object> asistenciaMap = new HashMap<>();
                    Monitor monitor = asistencia.getMonitor();
                    asistenciaMap.put("id", asistencia.getId());
                    asistenciaMap.put("nombreMonitor", monitor.getUsuario().getNombre());
                    asistenciaMap.put("apellidoMonitor", monitor.getUsuario().getApellido());
                    asistenciaMap.put("semestre", monitor.getSemestre().getNombre());
                    asistenciaMap.put("horasCubiertas", asistencia.getHorasCubiertas());
                    return asistenciaMap;
                }).collect(Collectors.toList());
    }

}
