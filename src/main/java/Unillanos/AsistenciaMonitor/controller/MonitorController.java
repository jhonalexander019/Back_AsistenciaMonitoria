package Unillanos.AsistenciaMonitor.controller;

import Unillanos.AsistenciaMonitor.dto.monitor.*;
import Unillanos.AsistenciaMonitor.service.MonitorService;
import Unillanos.AsistenciaMonitor.exception.ErrorMessages;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/monitores")
@Tag(name = "Monitores", description = "Endpoints para la gestión de monitores")
public class MonitorController extends BaseController {
    @Autowired
    private MonitorService monitorService;

    @PostMapping("/crear")
    @Operation(summary = "Crear un nuevo monitor", description = "Permite crear un monitor con los datos especificados.")
    public ResponseEntity<?> crearMonitor(@Valid @RequestBody RequestDTO monitorDTO) {
        try {
            ResponseCreateMonitorDTO monitor = monitorService.crearMonitor(monitorDTO);
            return createSuccessResponse(monitor);
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/listarSemestre")
    @Operation(
            summary = "Listar monitores por semestre vigente",
            description = "Lista todos los monitores asignados al semestre vigente según la fecha actual."
    )
    public ResponseEntity<?> listarMonitoresPorSemestre() {
        try {
            List<ResponseCreateMonitorDTO> monitores = monitorService.listarMonitoresPorSemestre();
            if (monitores.isEmpty()) {
                return createErrorResponse(HttpStatus.NOT_FOUND, ErrorMessages.MONITORS_NOT_FOUND_PER_SEMESTER);
            }
            return createSuccessResponse(monitores);
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_REQUEST);
        }
    }

    @GetMapping("/listarPorDia")
    @Operation(summary = "Listar monitores por horario", description = "Obtiene los Monitores asignados en la mañana y en la tarde para un día específico.")
    public ResponseEntity<?> listarMonitoresPorHorario(@RequestParam String dia) {
        try {
            Map<String, List<Map<String, String>>> monitoresPorHorario = monitorService.listarMonitoresPorHorario(dia);
            return createSuccessResponse(monitoresPorHorario);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_REQUEST);
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/horasCubiertas")
    @Operation(summary = "Obtener horas cubiertas por monitor", description = "Devuelve las horas cubiertas y asignadas de un monitor.")
    @ApiResponse(responseCode = "200", description = "Listado de monitores con horas cubiertas")
    public ResponseEntity<?> obtenerHorasCubiertasPorMonitor(@RequestParam(required = false) Long semestreId) {
        try {
            List<ResponseHorasMonitorDTO> horasCubiertas = monitorService.obtenerHorasCubiertas(semestreId);
            return createSuccessResponse(horasCubiertas);
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/actualizarPerfil/{monitorId}")
    @Operation(summary = "Actualizar perfil del monitor", description = "Permite que un monitor actualice sus datos personales.")
    public ResponseEntity<?> actualizarPerfil(@PathVariable Long monitorId, @RequestBody RequestDTO monitorDTO) {
        try {
            return createSuccessResponse(monitorService.actualizarPerfil(monitorId, monitorDTO));
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_UPDATE_PROFILE);
        }
    }

    @DeleteMapping("/eliminar/{monitorId}")
    @Operation(summary = "Eliminar monitor y su usuario asociado", description = "Elimina un monitor junto con su usuario asociado.")
    public ResponseEntity<?> eliminarMonitor(@PathVariable Long monitorId) {
        try {
            monitorService.eliminarMonitorYUsuario(monitorId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Respuesta sin contenido
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_REQUEST);
        }
    }

    @GetMapping("/totalesPorSemestre")
    @Operation(
            summary = "Obtener totales de horas asignadas y trabajadas por semestre",
            description = "Retorna el total de horas asignadas y trabajadas (estado 'Presente') para todos los monitores de un semestre específico."
    )
    public ResponseEntity<Map<String, Double>> obtenerTotalesPorSemestre(@RequestParam Long semestreId) {
        return createSuccessResponse(monitorService.obtenerTotalesPorSemestre(semestreId));
    }
}