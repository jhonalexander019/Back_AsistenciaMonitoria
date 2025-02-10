package Unillanos.AsistenciaMonitor.dto.asistencia;

import Unillanos.AsistenciaMonitor.dto.monitor.ResponseCreateMonitorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateAsistenciaDTO {
    private ResponseCreateMonitorDTO monitorId;
    private LocalDateTime fecha;
    private String estado;
    private Double horasCubiertas;
    private String jornada;
}

