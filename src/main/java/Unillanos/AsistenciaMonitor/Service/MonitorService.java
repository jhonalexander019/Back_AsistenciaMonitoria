package Unillanos.AsistenciaMonitor.Service;

import Unillanos.AsistenciaMonitor.DTO.Monitor.*;
import Unillanos.AsistenciaMonitor.Entity.*;
import Unillanos.AsistenciaMonitor.Mapper.*;
import Unillanos.AsistenciaMonitor.Repository.*;
import Unillanos.AsistenciaMonitor.Utils.CodeGenerator;
import Unillanos.AsistenciaMonitor.Utils.ErrorMessages;
import Unillanos.AsistenciaMonitor.Utils.GetData;
import Unillanos.AsistenciaMonitor.Utils.ValidationUtils;
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
    private GetData getData;
    @Autowired
    private ValidationUtils validationUtils;
    @Autowired
    private MonitorMapper monitorMapper;

    public MonitorService(MonitorRepository monitorRepository,
                          UsuarioRepository usuarioRepository,
                          AsistenciaRepository asistenciaRepository) {
        this.monitorRepository = monitorRepository;
        this.usuarioRepository = usuarioRepository;
        this.asistenciaRepository = asistenciaRepository;
    }

    public ResponseMonitorDTO crearMonitor(RequestDTO monitorDTO) {

        validationUtils.validarMonitorExistente(monitorDTO.getCorreo(), monitorDTO.getSemestre());
        Rol rolMonitor = getData.obtenerRolMonitor();
        Integer codigoUnico = CodeGenerator.generarCodigoUnico(new Random(), usuarioRepository::existsByCodigo);

        Usuario usuario = getData.crearUsuario(monitorDTO, rolMonitor, codigoUnico);
        Monitor monitor = monitorMapper.toEntity(monitorDTO);
        monitor.setUsuario(usuario);
        monitor.setSemestre(getData.obtenerSemestreVigente());

        return monitorMapper.toResponseMonitorDTO(monitorRepository.save(monitor));
    }

    public List<ResponseCreateMonitorDTO> listarMonitoresPorSemestre() {
        Semestre semestreVigente = getData.obtenerSemestreVigente();
        return monitorRepository.findBySemestreId(semestreVigente.getId())
                .stream()
                .map(monitorMapper::toResponseCreateMonitorDTO)
                .collect(Collectors.toList());
    }

    public Map<String, List<Map<String, String>>> listarMonitoresPorHorario(String dia) {
        validationUtils.validarDia(dia);
        List<Monitor> monitores = getData.obtenerMonitoresPorDia(dia);

        Map<String, List<Map<String, String>>> resultado = new HashMap<>();
        resultado.put("Mañana", getData.filtrarMonitoresPorTurno(monitores, dia + "Mañana"));
        resultado.put("Tarde", getData.filtrarMonitoresPorTurno(monitores, dia + "Tarde"));
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
        getData.actualizarMonitor(monitor, monitorDTO);
        return monitorMapper.toResponseCreateMonitorDTO(monitorRepository.save(monitor));
    }
}