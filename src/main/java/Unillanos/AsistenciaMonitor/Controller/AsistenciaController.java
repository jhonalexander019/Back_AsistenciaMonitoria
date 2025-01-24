package Unillanos.AsistenciaMonitor.Controller;

import Unillanos.AsistenciaMonitor.DTO.Asistencia.ResponseCreateAsistenciaDTO;
import Unillanos.AsistenciaMonitor.Service.AsistenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/asistencias")
@Tag(name = "Asistencias", description = "Endpoints para la gestión de las asistencias")
public class AsistenciaController {
    @Autowired
    private AsistenciaService asistenciaService;

    @PostMapping("/registrar/{monitorId}")
    @Operation(
            summary = "Registrar asistencia de un monitor",
            description = "Registra la asistencia de un monitor con el estado predeterminado 'Presente', calculando automáticamente las horas según el turno (mañana o tarde).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID del monitor para registrar asistencia"
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Asistencia registrada exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Error al registrar asistencia")
            }
    )
    public ResponseEntity<?> registrarAsistencia(@PathVariable Long monitorId,
                                                 @RequestParam String state) {
        try {
            ResponseCreateAsistenciaDTO asistencia = asistenciaService.registrarAsistencia(monitorId, state);
            return ResponseEntity.ok(asistencia);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/horasAusente/{monitorId}")

    public ResponseEntity<?> obtenerHorasAusentes(@PathVariable Long monitorId){
        try {
            return ResponseEntity.ok(asistenciaService.obtenerHorasAusente(monitorId));
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
