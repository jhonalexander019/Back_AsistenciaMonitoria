package Unillanos.AsistenciaMonitor.Entity;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Schema(description = "Entidad que representa a un usuario del sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@ejemplo.com")
    private String correo;
    @Schema(description = "Género del usuario", example = "Masculino")
    private String genero;
    @Column(unique = true, nullable = false)
    @Schema(description = "Código único del usuario", example = "123456")
    private Integer codigo;
    @Schema(description = "Fecha de creación del usuario", example = "2024-12-02T10:15:30")
    private LocalDateTime fechaCreacion;

    @ManyToOne
    private Rol rol;
}
