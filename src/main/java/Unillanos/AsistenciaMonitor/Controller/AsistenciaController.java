package Unillanos.AsistenciaMonitor.Controller;

import Unillanos.AsistenciaMonitor.Entity.Asistencia;
import Unillanos.AsistenciaMonitor.Service.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/asistencias")
public class AsistenciaController {
    @Autowired
    private AsistenciaService asistenciaService;

    @PostMapping("/registrar")
    public ResponseEntity<Asistencia> registrarAsistencia(@RequestBody Map<String, Object> payload) {
        Asistencia asistencia = asistenciaService.registrarAsistencia(
                Long.valueOf((Integer) payload.get("monitor_id")),
                (String) payload.get("estado"),
                (Integer) payload.get("horas_trabajadas")
        );
        return ResponseEntity.ok(asistencia);
    }
}
