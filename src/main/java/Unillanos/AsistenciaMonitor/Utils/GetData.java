package Unillanos.AsistenciaMonitor.Utils;

import Unillanos.AsistenciaMonitor.DTO.Monitor.RequestDTO;
import Unillanos.AsistenciaMonitor.DTO.Usuario.RequestCreateUsuarioDTO;
import Unillanos.AsistenciaMonitor.Entity.Monitor;
import Unillanos.AsistenciaMonitor.Entity.Rol;
import Unillanos.AsistenciaMonitor.Entity.Semestre;
import Unillanos.AsistenciaMonitor.Entity.Usuario;
import Unillanos.AsistenciaMonitor.Mapper.RolMapper;
import Unillanos.AsistenciaMonitor.Mapper.UsuarioMapper;
import Unillanos.AsistenciaMonitor.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GetData {

    private final RolRepository rolRepository;
    private final MonitorRepository monitorRepository;
    private final SemestreRepository semestreRepository;
    private final UsuarioRepository usuarioRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final RolMapper rolMapper;
    private final UsuarioMapper usuarioMapper;

    @Autowired
    public GetData(RolRepository rolRepository, MonitorRepository monitorRepository, AsistenciaRepository asistenciaRepository,
                   SemestreRepository semestreRepository, UsuarioRepository usuarioRepository,
                   RolMapper rolMapper, UsuarioMapper usuarioMapper) {
        this.rolRepository = rolRepository;
        this.monitorRepository = monitorRepository;
        this.semestreRepository = semestreRepository;
        this.usuarioRepository = usuarioRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.rolMapper = rolMapper;
        this.usuarioMapper = usuarioMapper;
    }

    public Rol obtenerRolMonitor() {
        return rolRepository.findByNombre("Monitor")
                .orElseThrow(() -> new RuntimeException(ErrorMessages.ROL_NOT_FOUND));
    }

    public Semestre obtenerSemestrePorId(Long semestreId) {
        return semestreRepository.findById(semestreId)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.SEMESTER_NOT_FOUND));
    }

    public Semestre obtenerSemestreVigente() {
        LocalDate fechaActual = LocalDate.now();
        return semestreRepository.findAll().stream()
                .filter(semestre -> !fechaActual.isBefore(semestre.getFechaInicio()) && !fechaActual.isAfter(semestre.getFechaFin()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(ErrorMessages.INVALID_SEMESTER));
    }

    public boolean obtenerSemestrePorFecha(LocalDate fechaInicio,LocalDate fechaFin) {
        return semestreRepository.findSemestreConFechaIncluida(fechaInicio, fechaFin).isEmpty();
    }



    public Monitor obtenerMonitorPorId(Long id) {
        Monitor monitor = monitorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.MONITOR_NOT_FOUND));

        if (Objects.equals(monitor.getSemestre().getId(), obtenerSemestreVigente().getId())){
            return  monitor;
        }
        throw new RuntimeException(ErrorMessages.INVALID_MONITOR);
    }

    public Monitor obtenerMonitorPorIdUsuario(Long id){
        return monitorRepository.findByUsuarioId(id);
    }

    public Usuario obtenerUsuarioPorCodigo(Integer codigo){
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.INVALID_SESSION));

    }

    public List<Monitor> obtenerMonitoresPorDia(String dia) {
        List<Monitor> monitors = monitorRepository.findByDiasAsignadosContaining(dia).stream()
                .filter(monitor -> monitor.getSemestre().equals(this.obtenerSemestreVigente()))
                .toList();

        if (monitors.isEmpty())
            throw new RuntimeException(ErrorMessages.MONITORS_NOT_FOUND_PER_DAY);

        return  monitors;
    }


    public boolean validarExistenciaMonitoresEnSemestreId(Long semestreId){
        return monitorRepository.existsBySemestreId(semestreId);

    }

    public List<Map<String, String>> filtrarMonitoresPorTurno(List<Monitor> monitores, String turno) {
        return monitores.stream()
                .filter(monitor -> monitor.getDiasAsignados().contains(turno))
                .map(monitor -> Map.of(
                        "nombre", monitor.getUsuario().getNombre(),
                        "apellido", monitor.getUsuario().getApellido()
                ))
                .collect(Collectors.toList());
    }

    public Usuario crearUsuario(RequestDTO monitorDTO, Rol rolMonitor, Integer codigoUnico) {
        RequestCreateUsuarioDTO usuarioDTO = new RequestCreateUsuarioDTO(
                monitorDTO.getNombre(),
                monitorDTO.getApellido(),
                monitorDTO.getCorreo(),
                monitorDTO.getGenero(),
                codigoUnico,
                rolMapper.toDTO(rolMonitor),
                LocalDateTime.now()
        );

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        return usuarioRepository.save(usuario);
    }

    public void actualizarMonitor(Monitor monitor, RequestDTO monitorDTO) {
        Usuario usuario = monitor.getUsuario();
        if (monitorDTO.getNombre() != null) usuario.setNombre(monitorDTO.getNombre());
        if (monitorDTO.getApellido() != null) usuario.setApellido(monitorDTO.getApellido());
        if (monitorDTO.getCorreo() != null) usuario.setCorreo(monitorDTO.getCorreo());
        if (monitorDTO.getGenero() != null) usuario.setGenero(monitorDTO.getGenero());
        if (monitorDTO.getDiasAsignados() != null) monitor.setDiasAsignados(monitorDTO.getDiasAsignados());
        if (monitorDTO.getTotalHoras() != null) monitor.setTotalHoras(monitorDTO.getTotalHoras());
        usuarioRepository.save(usuario);
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
