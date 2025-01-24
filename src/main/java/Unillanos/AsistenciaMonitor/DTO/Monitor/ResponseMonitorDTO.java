package Unillanos.AsistenciaMonitor.DTO.Monitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMonitorDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private Integer codigo;
    private String genero;
    private String semestre;
}