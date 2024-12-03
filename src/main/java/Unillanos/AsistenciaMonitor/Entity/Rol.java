package Unillanos.AsistenciaMonitor.Entity;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Schema(description = "Entidad que representa un rol dentro del sistema")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del rol", example = "1")
    private Long id;

    @Schema(description = "Nombre del rol", example = "Monitor")
    private String nombre;

    // Constructor sin argumentos
    public Rol() {
    }

    // Constructor con argumento para inicializar nombre
    public Rol(String nombre) {
        this.nombre = nombre;
    }
    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
