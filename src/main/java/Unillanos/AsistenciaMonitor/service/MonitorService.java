package Unillanos.AsistenciaMonitor.service;

import Unillanos.AsistenciaMonitor.dto.monitor.*;
import Unillanos.AsistenciaMonitor.entity.*;
import Unillanos.AsistenciaMonitor.mapper.*;
import Unillanos.AsistenciaMonitor.repository.*;
import Unillanos.AsistenciaMonitor.service.helpers.MonitorServiceHelper;
import Unillanos.AsistenciaMonitor.service.helpers.RolServiceHelper;
import Unillanos.AsistenciaMonitor.service.helpers.UsuarioServiceHelper;
import Unillanos.AsistenciaMonitor.utils.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MonitorService {

    private final MonitorRepository monitorRepository;
    private final UsuarioRepository usuarioRepository;
    private final AsistenciaRepository asistenciaRepository;

    @Autowired
    private CommonService getData;
    @Autowired
    private MonitorServiceHelper monitorHelper;
    @Autowired
    private MonitorMapper monitorMapper;
    @Autowired
    private RolServiceHelper rolHelper;
    @Autowired
    private UsuarioServiceHelper usuarioHelper;

    public MonitorService(MonitorRepository monitorRepository,
                          UsuarioRepository usuarioRepository,
                          AsistenciaRepository asistenciaRepository) {
        this.monitorRepository = monitorRepository;
        this.usuarioRepository = usuarioRepository;
        this.asistenciaRepository = asistenciaRepository;
    }

    public ResponseCreateMonitorDTO crearMonitor(RequestDTO monitorDTO) {

        monitorHelper.validarMonitorExistente(monitorDTO.getCorreo(), monitorDTO.getSemestre());
        Rol rolMonitor = rolHelper.obtenerRolMonitor();
        Integer codigoUnico = usuarioHelper.generarCodigoUnico(new Random(), usuarioRepository::existsByCodigo);

        Usuario usuario = monitorHelper.crearUsuario(monitorDTO, rolMonitor, codigoUnico);
        Monitor monitor = monitorMapper.toEntity(monitorDTO);
        monitor.setUsuario(usuario);
        monitor.setSemestre(getData.obtenerSemestreVigente());

        return monitorMapper.toResponseCreateMonitorDTO(monitorRepository.save(monitor));
    }

    public List<ResponseCreateMonitorDTO> listarMonitoresPorSemestre() {
        Semestre semestreVigente = getData.obtenerSemestreVigente();
        return monitorRepository.findBySemestreId(semestreVigente.getId())
                .stream()
                .map(monitorMapper::toResponseCreateMonitorDTO)
                .collect(Collectors.toList());
    }

    public Map<String, List<Map<String, String>>> listarMonitoresPorHorario(String dia) {
        monitorHelper.validarDia(dia);
        List<Monitor> monitores = monitorHelper.obtenerMonitoresPorDia(dia);

        Map<String, List<Map<String, String>>> resultado = new HashMap<>();
        resultado.put("Mañana", monitorHelper.filtrarMonitoresPorTurno(monitores, dia + "Mañana"));
        resultado.put("Tarde", monitorHelper.filtrarMonitoresPorTurno(monitores, dia + "Tarde"));
        return resultado;
    }

    public List<ResponseHorasMonitorDTO> obtenerHorasCubiertas(Long semestreId) {
        if (semestreId == null) {
            Semestre semestreVigente = getData.obtenerSemestreVigente();
            return monitorRepository.findAll().stream()
                    .filter(monitor -> monitor.getSemestre().equals(semestreVigente))
                    .map(monitor -> monitorMapper.toResponseHorasMonitorDTO(monitor, asistenciaRepository, semestreVigente.getId()))
                    .collect(Collectors.toList());
        }
        return monitorRepository.findAll().stream()
                .filter(monitor -> monitor.getSemestre().getId().equals(semestreId))
                .map(monitor -> monitorMapper.toResponseHorasMonitorDTO(monitor, asistenciaRepository, semestreId))
                .collect(Collectors.toList());
    }


    public ResponseCreateMonitorDTO actualizarPerfil(Long monitorId, RequestDTO monitorDTO) {
        Monitor monitor = getData.obtenerMonitorPorId(monitorId);
        monitorHelper.actualizarMonitor(monitor, monitorDTO);
        return monitorMapper.toResponseCreateMonitorDTO(monitorRepository.save(monitor));
    }
    public void eliminarMonitorYUsuario(Long monitorId) {
        // Buscar el monitor por su ID
        Monitor monitor = monitorRepository.findById(monitorId)
                .orElseThrow(() -> new RuntimeException("Monitor no encontrado"));

        // Obtener el usuario asociado al monitor
        Usuario usuario = monitor.getUsuario();

        // Eliminar el monitor
        monitorRepository.delete(monitor);

        // Eliminar el usuario asociado
        usuarioRepository.delete(usuario);
    }
    public Map<String, Double> obtenerTotalesPorSemestre(Long semestreId) {
        // Obtener monitores del semestre (sin necesidad de validación adicional)
        List<Monitor> monitores = monitorRepository.findBySemestreId(semestreId);

        // Si no hay monitores, devolver valores 0.0
        if (monitores.isEmpty()) {
            return Map.of(
                    "totalHorasAsignadas", 0.0,
                    "totalHorasTrabajadas", 0.0
            );
        }

        // Calcular total de horas asignadas
        double totalHorasAsignadas = monitores.stream().mapToDouble(Monitor::getTotalHoras).sum();

        // Obtener IDs de los monitores para buscar sus asistencias
        List<Long> monitorIds = monitores.stream()
                .map(Monitor::getId)
                .toList();

        // Obtener suma de horas trabajadas directamente desde el repositorio
        Double totalHorasTrabajadas = asistenciaRepository.sumarHorasCubiertasPorMonitores(monitorIds, "Presente");

        // Manejar caso donde la consulta devuelva null (ninguna asistencia registrada)
        if (totalHorasTrabajadas == null) {
            totalHorasTrabajadas = 0.0;
        }

        return Map.of(
                "totalHorasAsignadas", totalHorasAsignadas,
                "totalHorasTrabajadas", totalHorasTrabajadas
        );
    }

}