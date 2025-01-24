package Unillanos.AsistenciaMonitor.DTO.Semestre;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseCreateSemestreDTO {
    private Long id;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
