package Unillanos.AsistenciaMonitor.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@Schema(description = "Entidad que representa un monitor asignado a un semestre")
public class Monitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del monitor", example = "1")
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(description = "Informacion del monitor", example = "Pepito, Perez, pepito@gmail.com, Monitor, 26/11/2024 ")
    private Usuario usuario;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(description = "Semestre del monitor", example = "5")
    private Semestre semestre;

    @Column(nullable = false)
    @Schema(description = "Horas asignadas en el semestre al monitor", example = "150")
    private Integer totalHoras;

    @Column(nullable = false, length = 255)
    @Schema(description = "Horario asignado en el semestre al monitor", example = "LunesMañana, MiercolesTarde, ViernesMañana")
    private String diasAsignados;
}