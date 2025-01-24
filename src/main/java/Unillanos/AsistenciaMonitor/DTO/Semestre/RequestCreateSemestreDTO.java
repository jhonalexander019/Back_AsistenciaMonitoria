package Unillanos.AsistenciaMonitor.DTO.Semestre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateSemestreDTO {
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}

