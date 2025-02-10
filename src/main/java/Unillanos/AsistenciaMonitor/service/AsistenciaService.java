package Unillanos.AsistenciaMonitor.service;

import Unillanos.AsistenciaMonitor.dto.asistencia.AsistenciaDTO;
import Unillanos.AsistenciaMonitor.dto.asistencia.ResponseCreateAsistenciaDTO;
import Unillanos.AsistenciaMonitor.entity.Asistencia;
import Unillanos.AsistenciaMonitor.entity.Monitor;
import Unillanos.AsistenciaMonitor.entity.Semestre;
import Unillanos.AsistenciaMonitor.mapper.AsistenciaMapper;
import Unillanos.AsistenciaMonitor.repository.AsistenciaRepository;
import Unillanos.AsistenciaMonitor.repository.MonitorRepository;
import Unillanos.AsistenciaMonitor.repository.SemestreRepository;
import Unillanos.AsistenciaMonitor.service.helpers.AsistenciaServiceHelper;
import Unillanos.AsistenciaMonitor.exception.ErrorMessages;
import Unillanos.AsistenciaMonitor.utils.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class


AsistenciaService {
    @Autowired
    private AsistenciaRepository asistenciaRepository;
    @Autowired
    private MonitorRepository monitorRepository;
    @Autowired
    private SemestreRepository semestreRepository;
    @Autowired
    private AsistenciaMapper asistenciaMapper;
    @Autowired
    private CommonService getData;
    @Autowired
    private AsistenciaServiceHelper asistenciaHelper;

    public ResponseCreateAsistenciaDTO registrarAsistencia(Long monitorId, String state) {
        LocalDateTime now = LocalDateTime.now();
        LocalTime horaActual = LocalTime.now();

        // Buscar al monitor
        Monitor monitor = monitorRepository.findById(monitorId)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.MONITOR_NOT_FOUND));

        // Obtener la hora actual
        Double horasTrabajadas;
        String turno;

        // Determinar el turno
        if (horaActual.isAfter(LocalTime.of(7, 59)) && horaActual.isBefore(LocalTime.of(12, 5))) {
            turno = "Mañana";
            horasTrabajadas = 4.0; // Horas asignadas en la mañana
        } else if (horaActual.isAfter(LocalTime.of(13, 59)) && horaActual.isBefore(LocalTime.of(17, 35))) {
            turno = "Tarde";
            horasTrabajadas = 3.5; // Horas asignadas en la tarde
        } else {
            throw new IllegalArgumentException(ErrorMessages.INVALID_REGISTER_ATTENDANCE);
        }

        boolean existeAsistencia = asistenciaHelper.validarAsistencia(monitor, now.toLocalDate().atStartOfDay(), now.plusDays(1).toLocalDate().atStartOfDay(), turno);
        if (!existeAsistencia) {

            // Crear el registro de asistencia
            Asistencia asistencia = new Asistencia();
            asistencia.setMonitor(monitor);
            asistencia.setFecha(LocalDateTime.now());
            asistencia.setEstado(state);
            asistencia.setHorasCubiertas(horasTrabajadas);
            asistencia.setJornada(turno);

            return asistenciaMapper.toDTO(asistenciaRepository.save(asistencia));
        }
        throw new IllegalArgumentException(ErrorMessages.REPEATED_ATTENDANCE);
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

    public Double obtenerHorasAusente(Long id) {
        Monitor monitorVigente = getData.obtenerMonitorPorId(id);
        Map<String, Double> asistenciaHoras = asistenciaRepository.findByMonitorId(id).stream()
                .filter(asistencia -> asistencia.getMonitor().getSemestre().equals(monitorVigente.getSemestre()))
                .collect(Collectors.groupingBy(
                        Asistencia::getEstado, // Agrupa por estado ("Ausente", "Presente")
                        Collectors.summingDouble(Asistencia::getHorasCubiertas) // Suma las horas por estado
                ));

        double ausentes = asistenciaHoras.getOrDefault("Ausente", 0.0);
        double recuperadas = asistenciaHoras.getOrDefault("Recuperado", 0.0);


        if ((ausentes - recuperadas) != 0) {
            return ausentes - recuperadas;
        } else {
            throw new RuntimeException(ErrorMessages.ATTENDANCE_NOT_FOUND);
        }
    }

    public void registrarAsistenciasAutomaticas(String turno) {
        String dia = obtenerDiaActual();

        if (dia == null) {
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

        Semestre semestreVigente = getData.obtenerSemestreVigente();

        // Obtener los monitores asignados al día y turno
        List<Monitor> monitores = monitorRepository.findByDiasAsignadosContaining(dia + turno);

        for (Monitor monitor : monitores) {

            if (!monitor.getSemestre().equals(semestreVigente))
                continue;

            // Validar si ya existe un registro de asistencia para el monitor en este día y jornada
            boolean existeAsistencia = asistenciaHelper.validarAsistencia(monitor, now.toLocalDate().atStartOfDay(), now.plusDays(1).toLocalDate().atStartOfDay(), turno);

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

    public List<AsistenciaDTO> listarAsistenciasPorFechaYSemestre(Long semestreId) {
        Semestre semestre = (semestreId != null)
                ? semestreRepository.findById(semestreId)
                .orElseThrow(() -> new RuntimeException("Semestre no encontrado"))
                : getData.obtenerSemestreVigente();

        List<Asistencia> asistencias = asistenciaRepository.findAll().stream()
                .filter(asistencia -> asistencia.getMonitor().getSemestre().equals(semestre))
                .sorted(Comparator.comparing(Asistencia::getFecha).reversed())
                .collect(Collectors.toList());

        return asistencias.stream()
                .map(asistenciaMapper::toAsistenciaDTO)
                .collect(Collectors.toList());
    }



}
