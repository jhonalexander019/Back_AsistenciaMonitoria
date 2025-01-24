package Unillanos.AsistenciaMonitor.Entity;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Schema(description = "Entidad que representa un semestre académico")
public class Semestre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del semestre", example = "1")
    private Long id;

    @Schema(description = "Nombre del semestre", example = "2024-1")
    private String nombre;
    @Schema(description = "Fecha de inicio del semestre", example = "2024-01-15")
    private LocalDate fechaInicio;
    @Schema(description = "Fecha de fin del semestre", example = "2024-06-15")
    private LocalDate fechaFin;
}
