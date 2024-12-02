package Unillanos.AsistenciaMonitor.Controller;

import Unillanos.AsistenciaMonitor.Entity.Monitor;
import Unillanos.AsistenciaMonitor.Service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@RestController
@RequestMapping("/monitores")
@Tag(name = "Monitores", description = "Endpoints para la gestión de monitores")
public class MonitorController {
    @Autowired
    private MonitorService monitorService;

    @PostMapping("/crear")
    @Operation(summary = "Crear un nuevo monitor", description = "Permite crear un monitor con los datos especificados.")
    public ResponseEntity<Monitor> crearMonitor(@RequestBody Map<String, Object> payload) {
        Monitor monitor = monitorService.crearMonitor(
                (String) payload.get("nombre"),
                (String) payload.get("apellido"),
                (String) payload.get("dias_asignados"),
                (Integer) payload.get("total_horas"),
                (String) payload.get("correo"),
                (String) payload.get("codigo_estudiantil"),
                (String) payload.get("telefono"),
                (String) payload.get("genero"),
                Long.valueOf((Integer) payload.get("semestre"))
        );
        return ResponseEntity.ok(monitor);
    }

    // Otros endpoints: editar, eliminar, listar por turno
}
