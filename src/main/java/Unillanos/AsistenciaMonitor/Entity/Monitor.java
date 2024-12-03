package Unillanos.AsistenciaMonitor.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Schema(description = "Entidad que representa un monitor asignado a un semestre")
public class Monitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del monitor", example = "1")
    private Long id;

    @ManyToOne
    @Schema(description = "Informacion del monitor", example = "Pepito, Perez, pepito@gmail.com, Monitor, 26/11/2024 ")
    private Usuario usuario;

    @ManyToOne
    @Schema(description = "Semestre del monitor", example = "5")
    private Semestre semestre;

    @Schema(description = "Horas asignadas en el semestre al monitor", example = "150")
    private Integer totalHoras;

    @Schema(description = "Horario asigando en el semestre al monitor", example = "LunesMañana, MiercolesTarde, ViernesMañana")
    private String diasAsignados;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public Integer getTotalHoras() {
        return totalHoras;
    }

    public void setTotalHoras(Integer totalHoras) {
        this.totalHoras = totalHoras;
    }

    public String getDiasAsignados() {
        return diasAsignados;
    }

    public void setDiasAsignados(String diasAsignados) {
        this.diasAsignados = diasAsignados;
    }
}
