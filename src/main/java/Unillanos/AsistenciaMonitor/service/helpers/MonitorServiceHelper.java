package Unillanos.AsistenciaMonitor.service.helpers;

import Unillanos.AsistenciaMonitor.dto.monitor.RequestDTO;
import Unillanos.AsistenciaMonitor.dto.rol.ResponseCreateRolDTO;
import Unillanos.AsistenciaMonitor.dto.usuario.RequestCreateUsuarioDTO;
import Unillanos.AsistenciaMonitor.entity.Monitor;
import Unillanos.AsistenciaMonitor.entity.Usuario;
import Unillanos.AsistenciaMonitor.mapper.RolMapper;
import Unillanos.AsistenciaMonitor.mapper.UsuarioMapper;
import Unillanos.AsistenciaMonitor.repository.MonitorRepository;
import Unillanos.AsistenciaMonitor.entity.Rol;
import Unillanos.AsistenciaMonitor.repository.UsuarioRepository;
import Unillanos.AsistenciaMonitor.exception.ErrorMessages;
import Unillanos.AsistenciaMonitor.utils.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MonitorServiceHelper {
    private final MonitorRepository monitorRepository;
    private final UsuarioRepository usuarioRepository;
    private final CommonService getData;
    private final UsuarioServiceHelper usuarioServiceHelper;
    private final RolMapper rolMapper;
    private final UsuarioMapper usuarioMapper;

    @Autowired
    public MonitorServiceHelper(
            MonitorRepository monitorRepository,
            UsuarioRepository usuarioRepository,
            CommonService getData,
            UsuarioServiceHelper usuarioServiceHelper,
            RolMapper rolMapper,
            UsuarioMapper usuarioMapper
    ) {
        this.monitorRepository = monitorRepository;
        this.usuarioRepository = usuarioRepository;
        this.getData = getData;
        this.usuarioServiceHelper = usuarioServiceHelper;
        this.rolMapper = rolMapper;
        this.usuarioMapper = usuarioMapper;
    }

    public List<Monitor> obtenerMonitoresPorDia(String dia) {
        List<Monitor> monitors = monitorRepository.findByDiasAsignadosContaining(dia).stream()
                .filter(monitor -> monitor.getSemestre().equals(getData.obtenerSemestreVigente()))
                .toList();

        if (monitors.isEmpty())
            throw new RuntimeException(ErrorMessages.MONITORS_NOT_FOUND_PER_DAY);

        return monitors;
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
        ResponseCreateRolDTO rolResponseDTO = rolMapper.toResponseDTO(rolMonitor);
        RequestCreateUsuarioDTO usuarioDTO = new RequestCreateUsuarioDTO(
                monitorDTO.getNombre(),
                monitorDTO.getApellido(),
                monitorDTO.getCorreo(),
                monitorDTO.getGenero(),
                codigoUnico,
                rolResponseDTO, // Pasar el ResponseCreateRolDTO
                LocalDateTime.now()
        );
        return usuarioRepository.save(usuarioMapper.toEntity(usuarioDTO));
    }

    public void actualizarMonitor(Monitor monitor, RequestDTO monitorDTO) {
        usuarioServiceHelper.actualizarDatosUsuario(monitor.getUsuario(), monitorDTO);
        if (monitorDTO.getDiasAsignados() != null) monitor.setDiasAsignados(monitorDTO.getDiasAsignados());
        if (monitorDTO.getTotalHoras() != null) monitor.setTotalHoras(monitorDTO.getTotalHoras());
        monitorRepository.save(monitor);
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
