package Unillanos.AsistenciaMonitor.mapper;

import Unillanos.AsistenciaMonitor.dto.asistencia.AsistenciaDTO;
import Unillanos.AsistenciaMonitor.dto.asistencia.RequestCreateAsistenciaDTO;
import Unillanos.AsistenciaMonitor.dto.asistencia.ResponseCreateAsistenciaDTO;
import Unillanos.AsistenciaMonitor.entity.Asistencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class AsistenciaMapper {

    @Autowired
    private MonitorMapper monitorMapper;

    public Asistencia toEntity(RequestCreateAsistenciaDTO dto) {
        if (dto == null) return null;

        Asistencia entity = new Asistencia();
        entity.setFecha(dto.getFecha());
        entity.setEstado(dto.getEstado());
        entity.setHorasCubiertas(dto.getHorasCubiertas());
        entity.setJornada(dto.getJornada());
        return entity;
    }

    public ResponseCreateAsistenciaDTO toDTO(Asistencia entity) {
        if (entity == null) return null;

        ResponseCreateAsistenciaDTO dto = new ResponseCreateAsistenciaDTO();
        dto.setId(entity.getId());
        dto.setFecha(entity.getFecha());
        dto.setEstado(entity.getEstado());
        dto.setHorasCubiertas(entity.getHorasCubiertas());
        dto.setJornada(entity.getJornada());
        dto.setMonitor(monitorMapper.toResponseCreateMonitorDTO(entity.getMonitor()));
        return dto;
    }

    public AsistenciaDTO toAsistenciaDTO(Asistencia entity) {
        if (entity == null) return null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setId(entity.getId()); // Agregar el ID de la asistencia
        dto.setFecha(entity.getFecha().format(formatter));
        dto.setNombre(entity.getMonitor().getUsuario().getNombre());
        dto.setApellido(entity.getMonitor().getUsuario().getApellido());
        dto.setJornada(entity.getJornada());
        dto.setEstado(entity.getEstado());

        return dto;
    }


}