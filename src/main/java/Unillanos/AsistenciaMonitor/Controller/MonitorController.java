package Unillanos.AsistenciaMonitor.Controller;

import Unillanos.AsistenciaMonitor.Entity.Monitor;
import Unillanos.AsistenciaMonitor.Service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
                (String) payload.get("genero"),
                Long.valueOf((Integer) payload.get("semestre"))
        );
        return ResponseEntity.ok(monitor);
    }
    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar un monitor", description = "Permite eliminar un monitor por su ID.")
    public ResponseEntity<Void> eliminarMonitor(@PathVariable Long id) {
        monitorService.eliminarMonitor(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/listar")
    @Operation(summary = "Listar todos los monitores", description = "Obtiene una lista de todos los monitores registrados.")
    public ResponseEntity<List<Monitor>> listarMonitores() {
        List<Monitor> monitores = monitorService.listarMonitores();
        return ResponseEntity.ok(monitores);
    }

    @GetMapping("/buscar/{id}")
    @Operation(summary = "Obtener un monitor por ID", description = "Permite buscar un monitor especificando su ID.")
    public ResponseEntity<Monitor> obtenerMonitorPorId(@PathVariable Long id) {
        Monitor monitor = monitorService.obtenerMonitorPorId(id);
        return ResponseEntity.ok(monitor);
    }
    @GetMapping("/listarPorDia")
    @Operation(summary = "Listar monitores por día", description = "Devuelve una lista de monitores asignados a un día específico.")
    public ResponseEntity<List<Monitor>> listarMonitoresPorDia(@RequestParam String dia) {
        return ResponseEntity.ok(monitorService.listarMonitoresPorDia(dia));
    }
}
