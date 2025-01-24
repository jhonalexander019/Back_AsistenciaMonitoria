package Unillanos.AsistenciaMonitor.DTO.Monitor;

import lombok.Data;

@Data
public class ResponseCreateMonitorDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String genero;
    private Long semestre;
    private Integer totalHoras;
    private String diasAsignados;
    private Integer codigo;
}