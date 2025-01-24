package Unillanos.AsistenciaMonitor.DTO.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSessionUsuarioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private Integer codigo;
    private String genero;
    private String rol;
    private String diasAsignados;
}