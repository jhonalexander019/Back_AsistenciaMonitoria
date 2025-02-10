package Unillanos.AsistenciaMonitor.dto.asistencia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistenciaDTO {
    private String fecha;
    private String nombre;
    private String apellido;
    private String jornada;
    private String estado;
}