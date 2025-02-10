package Unillanos.AsistenciaMonitor.dto.usuario;

import Unillanos.AsistenciaMonitor.dto.rol.ResponseCreateRolDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseCreateUsuarioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String genero;
    private Integer codigo;
    private ResponseCreateRolDTO rol;
    private LocalDateTime fechaCreacion;
}