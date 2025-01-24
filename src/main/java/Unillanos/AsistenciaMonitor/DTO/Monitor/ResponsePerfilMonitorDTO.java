package Unillanos.AsistenciaMonitor.DTO.Monitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePerfilMonitorDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String genero;
    private String semestre;
    private Integer totalHoras;
    private Integer codigo;
}
