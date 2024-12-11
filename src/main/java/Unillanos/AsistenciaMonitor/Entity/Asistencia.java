package Unillanos.AsistenciaMonitor.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Schema(description = "Entidad que representa la asistencia de un monitor en un día específico")
public class Asistencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la asistencia", example = "1")
    private Long id;

    @ManyToOne
    @Schema(description = "Monitor asociado a la asistencia")
    private Monitor monitor;

    @Schema(description = "Fecha de la asistencia", example = "2024-12-01")
    private LocalDateTime fecha;
    @Schema(description = "Estado de la asistencia", example = "Presente | Ausente")
    private String estado;
    @Schema(description = "Horas trabajadas en esta asistencia", example = "4")
    private Double horasCubiertas;
    @Schema(description = "Jornada del turno", example = "Mañana | Tarde")
    private String jornada;

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getHorasCubiertas() {
        return horasCubiertas;
    }

    public void setHorasCubiertas(Double horasCubiertas) {
        this.horasCubiertas = horasCubiertas;
    }
}
