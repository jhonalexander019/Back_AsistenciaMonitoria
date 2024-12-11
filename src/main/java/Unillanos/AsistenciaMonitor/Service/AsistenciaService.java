package Unillanos.AsistenciaMonitor.Service;

import Unillanos.AsistenciaMonitor.Entity.Asistencia;
import Unillanos.AsistenciaMonitor.Entity.Monitor;
import Unillanos.AsistenciaMonitor.Repository.AsistenciaRepository;
import Unillanos.AsistenciaMonitor.Repository.MonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
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

    public Asistencia registrarAsistencia(Long monitorId) {
        // Buscar al monitor
        Monitor monitor = monitorRepository.findById(monitorId)
                .orElseThrow(() -> new RuntimeException("Monitor no encontrado"));

        // Obtener la hora actual
        LocalTime horaActual = LocalTime.now();
        Double horasTrabajadas;
        String turno;

        // Determinar el turno
        if (horaActual.isAfter(LocalTime.of(7, 59)) && horaActual.isBefore(LocalTime.of(12, 1))) {
            turno = "Mañana";
            horasTrabajadas = 4.0; // Horas asignadas en la mañana
        } else if (horaActual.isAfter(LocalTime.of(13, 59)) && horaActual.isBefore(LocalTime.of(17, 31))) {
            turno = "Tarde";
            horasTrabajadas = 3.5; // Horas asignadas en la tarde
        } else {
            throw new IllegalArgumentException("La hora actual no corresponde a un turno válido.");
        }

        // Crear el registro de asistencia
        Asistencia asistencia = new Asistencia();
        asistencia.setMonitor(monitor);
        asistencia.setFecha(LocalDateTime.now());
        asistencia.setEstado("Presente");
        asistencia.setHorasCubiertas(horasTrabajadas);
        asistencia.setJornada(turno);

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

    public void registrarAsistenciasAutomaticas(String turno) {
        String dia = obtenerDiaActual();

        if (dia == null) {
            // No procesar si es fin de semana
            return;
        }

        // Obtener la hora actual
        LocalDateTime now = LocalDateTime.now();
        Double horasCubiertas;

        // Determinar las horas cubiertas basado en el turno
        if (turno.equals("Mañana")) {
            horasCubiertas = 4.0; // Horas asignadas en la mañana
        } else if (turno.equals("Tarde")) {
            horasCubiertas = 3.5; // Horas asignadas en la tarde
        } else {
            // No es un turno válido
            return;
        }

        // Obtener los monitores asignados al día y turno
        List<Monitor> monitores = monitorRepository.findByDiasAsignadosContaining(dia + turno);

        for (Monitor monitor : monitores) {
            // Validar si ya existe un registro de asistencia para el monitor en este día y jornada
            boolean existeAsistencia = asistenciaRepository.existsByMonitorAndFechaBetweenAndJornada(
                    monitor,
                    now.toLocalDate().atStartOfDay(),
                    now.plusDays(1).toLocalDate().atStartOfDay(),
                    turno // Jornada (Mañana o Tarde)
            );

            if (!existeAsistencia) {
                System.out.println("Registrando asistencia automática para el turno: " + turno);

                // Crear y guardar el registro de asistencia
                Asistencia asistencia = new Asistencia();
                asistencia.setMonitor(monitor);
                asistencia.setFecha(LocalDateTime.now());
                asistencia.setEstado("Ausente");
                asistencia.setHorasCubiertas(horasCubiertas);
                asistencia.setJornada(turno);

                asistenciaRepository.save(asistencia);
            }
        }
    }

    // Metodo para obtener el dia Actual
    private String obtenerDiaActual() {
        DayOfWeek diaSemana = LocalDate.now().getDayOfWeek();

        return switch (diaSemana) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            default -> null;
        };
    }



}
