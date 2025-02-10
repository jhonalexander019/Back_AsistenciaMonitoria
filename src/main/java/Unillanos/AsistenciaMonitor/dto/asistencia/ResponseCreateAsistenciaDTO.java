package Unillanos.AsistenciaMonitor.dto.asistencia;

import Unillanos.AsistenciaMonitor.dto.monitor.ResponseCreateMonitorDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseCreateAsistenciaDTO {
    private Long id;
    private ResponseCreateMonitorDTO monitor;
    private LocalDateTime fecha;
    private String estado;
    private Double horasCubiertas;
    private String jornada;
}
