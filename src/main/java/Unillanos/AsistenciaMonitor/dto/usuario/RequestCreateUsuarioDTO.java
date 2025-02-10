package Unillanos.AsistenciaMonitor.dto.usuario;

import Unillanos.AsistenciaMonitor.dto.rol.ResponseCreateRolDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateUsuarioDTO {
    private String nombre;
    private String apellido;
    private String correo;
    private String genero;
    private Integer codigo;
    private ResponseCreateRolDTO rol;
    private LocalDateTime fechaCreacion;

}
