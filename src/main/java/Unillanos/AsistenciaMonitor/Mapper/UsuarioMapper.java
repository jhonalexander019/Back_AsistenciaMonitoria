package Unillanos.AsistenciaMonitor.Mapper;

import Unillanos.AsistenciaMonitor.DTO.Monitor.ResponseMonitorDTO;
import Unillanos.AsistenciaMonitor.DTO.Usuario.RequestCreateUsuarioDTO;
import Unillanos.AsistenciaMonitor.DTO.Usuario.ResponseSessionUsuarioDTO;
import Unillanos.AsistenciaMonitor.Entity.Monitor;
import Unillanos.AsistenciaMonitor.Entity.Rol;
import Unillanos.AsistenciaMonitor.Entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class UsuarioMapper {

    @Autowired
    private RolMapper rolMapper;

    public Usuario toEntity(RequestCreateUsuarioDTO dto) {
        if (dto == null) return null;
        System.out.println();

        Usuario entity = new Usuario();
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setCorreo(dto.getCorreo());
        entity.setGenero(dto.getGenero());
        entity.setCodigo(dto.getCodigo());
        entity.setRol(new Rol(dto.getRol().getId(), dto.getRol().getNombre()));
        entity.setFechaCreacion(LocalDateTime.now());
        return entity;
    }

    public ResponseSessionUsuarioDTO toResponseSessionUsuarioDTO(Monitor entity) {
        if (entity == null) return null;
        ResponseSessionUsuarioDTO dto = new ResponseSessionUsuarioDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getUsuario().getNombre());
        dto.setApellido(entity.getUsuario().getApellido());
        dto.setCodigo(entity.getUsuario().getCodigo());
        dto.setGenero(entity.getUsuario().getGenero());
        dto.setRol(entity.getUsuario().getRol().getNombre());
        dto.setDiasAsignados(entity.getDiasAsignados());
        return dto;
    }

    public ResponseSessionUsuarioDTO toResponseSessionUsuarioDTO(Usuario entity) {
        if (entity == null) return null;

        ResponseSessionUsuarioDTO dto = new ResponseSessionUsuarioDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setCodigo(entity.getCodigo());
        dto.setGenero(entity.getGenero());
        dto.setRol(entity.getRol().getNombre());
        return dto;
    }
}