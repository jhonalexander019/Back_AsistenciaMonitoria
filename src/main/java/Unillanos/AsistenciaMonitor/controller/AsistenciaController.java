package Unillanos.AsistenciaMonitor.controller;

import Unillanos.AsistenciaMonitor.dto.asistencia.AsistenciaDTO;
import Unillanos.AsistenciaMonitor.dto.asistencia.ResponseCreateAsistenciaDTO;
import Unillanos.AsistenciaMonitor.service.AsistenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/asistencias")
@Tag(name = "Asistencias", description = "Endpoints para la gestión de las asistencias")
public class AsistenciaController extends BaseController {

    @Autowired
    private AsistenciaService asistenciaService;

    @PostMapping("/registrar/{monitorId}")
    @Operation(summary = "Registrar asistencia de un monitor")
    public ResponseEntity<?> registrarAsistencia(@PathVariable Long monitorId, @RequestParam String state) {
        try {
            ResponseCreateAsistenciaDTO asistencia = asistenciaService.registrarAsistencia(monitorId, state);
            return createSuccessResponse(asistencia);
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/crear-manual")
    public ResponseEntity<ResponseCreateAsistenciaDTO> crearAsistenciaManual(
            @RequestParam Long monitorId,
            @RequestParam String jornada,
            @RequestParam Double horas,
            @RequestParam String estado) {

        ResponseCreateAsistenciaDTO asistencia = asistenciaService.crearAsistenciaManual(monitorId, jornada, horas, estado);
        return ResponseEntity.ok(asistencia);
    }

    @GetMapping("/horasAusente/{monitorId}")
    public ResponseEntity<?> obtenerHorasAusentes(@PathVariable Long monitorId) {
        try {
            return createSuccessResponse(asistenciaService.obtenerHorasAusente(monitorId));
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/listarAsistencias")
    @Operation(summary = "Listar asistencias por fecha, nombre, jornada y estado")
    public ResponseEntity<?> listarAsistenciasPorFechaYSemestre(@RequestParam(required = false) Long semestreId) {
        try {
            List<AsistenciaDTO> asistencias = asistenciaService.listarAsistenciasPorFechaYSemestre(semestreId);
            return createSuccessResponse(asistencias);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @PutMapping("/editar/{asistenciaId}")
    public ResponseEntity<ResponseCreateAsistenciaDTO> editarAsistencia(
            @PathVariable Long asistenciaId,
            @RequestParam Optional<Double> horas,
            @RequestParam Optional<String> estado) {

        ResponseCreateAsistenciaDTO asistencia = asistenciaService.editarAsistencia(asistenciaId, horas, estado);
        return ResponseEntity.ok(asistencia);
    }

}
