package Unillanos.AsistenciaMonitor.mapper;

import Unillanos.AsistenciaMonitor.dto.semestre.RequestCreateSemestreDTO;
import Unillanos.AsistenciaMonitor.dto.semestre.ResponseCreateSemestreDTO;
import Unillanos.AsistenciaMonitor.entity.Semestre;
import org.springframework.stereotype.Component;

@Component
public class SemestreMapper {

    public Semestre toEntity(RequestCreateSemestreDTO dto) {
        if (dto == null) return null;

        Semestre entity = new Semestre();
        entity.setNombre(dto.getNombre());
        entity.setFechaInicio(dto.getFechaInicio());
        entity.setFechaFin(dto.getFechaFin());
        return entity;
    }

    public ResponseCreateSemestreDTO toDTO(Semestre entity) {
        if (entity == null) return null;

        ResponseCreateSemestreDTO dto = new ResponseCreateSemestreDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setFechaInicio(entity.getFechaInicio());
        dto.setFechaFin(entity.getFechaFin());
        return dto;
    }
}