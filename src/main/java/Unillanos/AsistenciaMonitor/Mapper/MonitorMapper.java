package Unillanos.AsistenciaMonitor.Mapper;

import Unillanos.AsistenciaMonitor.DTO.Monitor.*;
import Unillanos.AsistenciaMonitor.Entity.Monitor;
import Unillanos.AsistenciaMonitor.Entity.Asistencia;
import Unillanos.AsistenciaMonitor.Entity.Semestre;
import Unillanos.AsistenciaMonitor.Repository.AsistenciaRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MonitorMapper {

    public Monitor toEntity(RequestDTO dto) {
        if (dto == null) return null;

        Monitor entity = new Monitor();
        entity.setTotalHoras(dto.getTotalHoras());
        entity.setDiasAsignados(dto.getDiasAsignados());
        return entity;
    }

    public ResponseCreateMonitorDTO toResponseCreateMonitorDTO(Monitor entity) {
        if (entity == null) return null;

        ResponseCreateMonitorDTO dto = new ResponseCreateMonitorDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getUsuario().getNombre());
        dto.setApellido(entity.getUsuario().getApellido());
        dto.setCorreo(entity.getUsuario().getCorreo());
        dto.setGenero(entity.getUsuario().getGenero());
        dto.setSemestre(entity.getSemestre().getId());
        dto.setTotalHoras(entity.getTotalHoras());
        dto.setDiasAsignados(entity.getDiasAsignados());
        dto.setCodigo(entity.getUsuario().getCodigo());
        return dto;
    }

    public ResponseMonitorDTO toResponseMonitorDTO(Monitor entity) {
        if (entity == null) return null;

        ResponseMonitorDTO dto = new ResponseMonitorDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getUsuario().getNombre());
        dto.setApellido(entity.getUsuario().getApellido());
        dto.setCodigo(entity.getUsuario().getCodigo());
        dto.setGenero(entity.getUsuario().getGenero());
        dto.setSemestre(entity.getSemestre().getNombre());
        return dto;
    }

    public ResponsePerfilMonitorDTO toResponsePerfilMonitorDTO(Monitor entity) {
        if (entity == null) return null;

        ResponsePerfilMonitorDTO dto = new ResponsePerfilMonitorDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getUsuario().getNombre());
        dto.setApellido(entity.getUsuario().getApellido());
        dto.setCorreo(entity.getUsuario().getCorreo());
        dto.setGenero(entity.getUsuario().getGenero());
        dto.setSemestre(entity.getSemestre().getNombre());
        dto.setTotalHoras(entity.getTotalHoras());
        dto.setCodigo(entity.getUsuario().getCodigo());
        return dto;
    }

    public ResponseHorasMonitorDTO toResponseHorasMonitorDTO(Monitor entity, AsistenciaRepository asistenciaRepository, Long semestreId) {
        if (entity == null) return null;

        ResponseHorasMonitorDTO dto = new ResponseHorasMonitorDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getUsuario().getNombre());
        dto.setApellido(entity.getUsuario().getApellido());
        dto.setGenero(entity.getUsuario().getGenero());

        Map<String, Double> asistenciaHoras = asistenciaRepository.findByMonitorId(entity.getId()).stream()
                .filter(asistencia -> asistencia.getMonitor().getSemestre().getId().equals(semestreId))
                .collect(Collectors.groupingBy(
                        Asistencia::getEstado, // Agrupa por estado ("Ausente", "Presente")
                        Collectors.summingDouble(Asistencia::getHorasCubiertas) // Suma las horas por estado
                ));

        dto.setHorasCubiertas(asistenciaHoras.getOrDefault("Recuperado", 0.0) + asistenciaHoras.getOrDefault("Presente", 0.0));
        dto.setTotalHoras(entity.getTotalHoras());
        return dto;
    }
}