package Unillanos.AsistenciaMonitor.Mapper;

import Unillanos.AsistenciaMonitor.DTO.Rol.RequestCreateRolDTO;
import Unillanos.AsistenciaMonitor.DTO.Rol.ResponseCreateRolDTO;
import Unillanos.AsistenciaMonitor.Entity.Rol;
import org.springframework.stereotype.Component;

@Component
public class RolMapper {

    public Rol toEntity(RequestCreateRolDTO dto) {
        if (dto == null) return null;

        Rol entity = new Rol();
        entity.setNombre(dto.getNombre());
        return entity;
    }

    public ResponseCreateRolDTO toDTO(Rol entity) {
        if (entity == null) return null;

        ResponseCreateRolDTO dto = new ResponseCreateRolDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        return dto;
    }
}