package Unillanos.AsistenciaMonitor.Mapper;

import Unillanos.AsistenciaMonitor.DTO.Asistencia.RequestCreateAsistenciaDTO;
import Unillanos.AsistenciaMonitor.DTO.Asistencia.ResponseCreateAsistenciaDTO;
import Unillanos.AsistenciaMonitor.Entity.Asistencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}