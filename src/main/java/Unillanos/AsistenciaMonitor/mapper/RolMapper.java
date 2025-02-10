package Unillanos.AsistenciaMonitor.mapper;

import Unillanos.AsistenciaMonitor.dto.rol.RequestCreateRolDTO;
import Unillanos.AsistenciaMonitor.dto.rol.ResponseCreateRolDTO;
import Unillanos.AsistenciaMonitor.dto.rol.RolDTO;
import Unillanos.AsistenciaMonitor.entity.Rol;
import org.springframework.stereotype.Component;

@Component
public class RolMapper {

    public Rol toEntity(RequestCreateRolDTO dto) {
        if (dto == null) return null;

        Rol entity = new Rol();
        entity.setNombre(dto.getNombre());
        return entity;
    }

    public ResponseCreateRolDTO toResponseDTO(Rol rol) {
        if (rol == null) return null;

        ResponseCreateRolDTO dto = new ResponseCreateRolDTO();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());

        return dto;
    }
    public RolDTO toRolDTO(Rol entity) {
        if (entity == null) return null;

        return new RolDTO(entity.getId(), entity.getNombre());
    }
}