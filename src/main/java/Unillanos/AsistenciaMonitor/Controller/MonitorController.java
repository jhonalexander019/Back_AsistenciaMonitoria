package Unillanos.AsistenciaMonitor.Controller;

import Unillanos.AsistenciaMonitor.Entity.Monitor;
import Unillanos.AsistenciaMonitor.Service.MonitorService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "Crear un nuevo monitor",
            description = "Permite crear un monitor con los datos especificados.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Información del monitor a crear",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    example = "{\"nombre\":\"Jhon\",\"apellido\":\"Roa\",\"correo\":\"jhon.roa@unillanos.edu.co\",\"genero\":\"Masculino\",\"semestre\":1,\"total_horas\":150,\"dias_asignados\":\"LunesMañana, MartesTarde\"}"
                            )
                    )
            )
    )
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

    @GetMapping("/listarPorDia")
    @Operation(
            summary = "Listar monitores por horario",
            description = "Obtiene los Monitores asignados en la mañana y en la tarde para un día específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de monitores por horario",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example =
                                            "{\n" +
                                                    "  \"Tarde\": [\n" +
                                                    "    {\n" +
                                                    "      \"nombre\": \"Ana\",\n" +
                                                    "      \"apellido\": \"García\"\n" +
                                                    "    },\n" +
                                                    "    {\n" +
                                                    "      \"nombre\": \"Carlos\",\n" +
                                                    "      \"apellido\": \"Pérez\"\n" +
                                                    "    }\n" +
                                                    "  ],\n" +
                                                    "  \"Mañana\": [\n" +
                                                    "    {\n" +
                                                    "      \"nombre\": \"Jhon\",\n" +
                                                    "      \"apellido\": \"Roa\"\n" +
                                                    "    }\n" +
                                                    "  ]\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error en la solicitud",
                            content = @Content(schema = @Schema(example =
                                    "{\n" +
                                            "  \"error\": \"Día no válido\"\n" +
                                            "}"
                            ))
                    )
            }
    )
    public ResponseEntity<?> listarMonitoresPorHorario(@RequestParam String dia) {
        try {
            Map<String, List<Map<String, String>>> monitoresPorHorario = monitorService.listarMonitoresPorHorario(dia);
            return ResponseEntity.ok(monitoresPorHorario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/perfil")
    @Operation(
            summary = "Obtener perfil de un monitor",
            description = "Obtiene los datos principales de un monitor.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Perfil del monitor",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example =
                                            "{\n" +
                                                    "  \"codigo\": 5666,\n" +
                                                    "  \"apellido\": \"Roa\",\n" +
                                                    "  \"correo\": \"jhon.roa@unillanos.edu.co\",\n" +
                                                    "  \"genero\": \"Masculino\",\n" +
                                                    "  \"totalHoras\": 150,\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"semestre\": \"2024-1\",\n" +
                                                    "  \"nombre\": \"Jhon\"\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error en la solicitud",
                            content = @Content(schema = @Schema(example =
                                    "{\n" +
                                            "  \"error\": \"Monitor no encontrado con ID: 8\"\n" +
                                            "}"
                            ))
                    )
            }
    )
    public ResponseEntity<Map<String, Object>> obtenerPerfilMonitor(@RequestParam Long id) {
        try {
            Map<String, Object> perfil = monitorService.obtenerPerfilMonitor(id);
            return ResponseEntity.ok(perfil);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


}
