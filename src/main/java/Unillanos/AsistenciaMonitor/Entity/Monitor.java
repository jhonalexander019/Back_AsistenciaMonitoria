package Unillanos.AsistenciaMonitor.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
public class Monitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del monitor", example = "1")
    private Long id;

    @ManyToOne
    @Schema(description = "Informacion del monitor", example = "Pepito, Perez, pepito@gmail.com, Monitor, 26/11/2024 ")
    private Usuario usuario;

    @Schema(description = "Código estudiantil del monitor", example = "12345678")
    private String codigoEstudiantil;

    @Schema(description = "Telefono del monitor", example = "311 222 4444")
    private String telefono;

    @Schema(description = "Genero del monitor", example = "Masculino | Femenino")
    private String genero;

    @ManyToOne
    @Schema(description = "Semestre del monitor", example = "5")
    private Semestre semestre;

    @Schema(description = "Horas asignadas en el semestre al monitor", example = "150")
    private Integer totalHoras;

    @Schema(description = "Jornada asignada al monitor", example = "Mañana | Tarde")
    private String horario;

    @Schema(description = "Horario asigando en el semestre al monitor", example = "Lunes, Miercoles, Viernes")
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

    public String getCodigoEstudiantil() {
        return codigoEstudiantil;
    }

    public void setCodigoEstudiantil(String codigoEstudiantil) {
        this.codigoEstudiantil = codigoEstudiantil;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
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

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDiasAsignados() {
        return diasAsignados;
    }

    public void setDiasAsignados(String diasAsignados) {
        this.diasAsignados = diasAsignados;
    }
}
