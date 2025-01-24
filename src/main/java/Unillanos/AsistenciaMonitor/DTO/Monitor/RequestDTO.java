package Unillanos.AsistenciaMonitor.DTO.Monitor;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    private String nombre;
    private String apellido;
    private String correo;
    private String genero;
    private Long semestre;
    private Integer totalHoras;
    private String diasAsignados;
}