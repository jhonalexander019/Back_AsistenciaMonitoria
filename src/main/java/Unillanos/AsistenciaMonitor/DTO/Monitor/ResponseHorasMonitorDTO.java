package Unillanos.AsistenciaMonitor.DTO.Monitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHorasMonitorDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String genero;
    private Double horasCubiertas;
    private Integer totalHoras;
}
