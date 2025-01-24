package Unillanos.AsistenciaMonitor.Controller;

import Unillanos.AsistenciaMonitor.DTO.Monitor.*;
import Unillanos.AsistenciaMonitor.Service.MonitorService;
import Unillanos.AsistenciaMonitor.Utils.ErrorMessages;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
    public ResponseEntity<?> crearMonitor(@Valid @RequestBody RequestDTO monitorDTO) {
        try {
            ResponseMonitorDTO monitor = monitorService.crearMonitor(monitorDTO);
            return ResponseEntity.ok(monitor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/listarSemestre")
    @Operation(
            summary = "Listar monitores por semestre vigente",
            description = "Lista todos los monitores asignados al semestre vigente según la fecha actual.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de monitores obtenida con éxito"),
                    @ApiResponse(responseCode = "404", description = "No hay un semestre vigente"),
                    @ApiResponse(responseCode = "400", description = "Error en la solicitud")
            }
    )
    public ResponseEntity<?> listarMonitoresPorSemestre() {
        try {
            List<ResponseCreateMonitorDTO> monitores = monitorService.listarMonitoresPorSemestre();
            if (monitores.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.MONITORS_NOT_FOUND_PER_SEMESTER);
            }
            return ResponseEntity.ok(monitores);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.INVALID_REQUEST);
        }
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
            return ResponseEntity.badRequest().body(ErrorMessages.INVALID_REQUEST);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
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
    public ResponseEntity<?> obtenerPerfilMonitor(@RequestParam Long id) {
        try {
            ResponsePerfilMonitorDTO perfil = null;
            return ResponseEntity.ok(perfil);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/horasCubiertas")
    @Operation(
            summary = "Obtener horas cubiertas por monitor",
            description = "Devuelve el nombre, apellido y la suma de las horas cubiertas cuando el estado es 'Presente', además del total de horas asignadas."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Listado de monitores con horas cubiertas",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(
                            example = "[\n" +
                                    "  {\n" +
                                    "    \"id\": 1,\n" +
                                    "    \"nombre\": \"Jhon\",\n" +
                                    "    \"apellido\": \"Roa\",\n" +
                                    "    \"genero\": \"Masculino\",\n" +
                                    "    \"horasCubiertas\": 8,\n" +
                                    "    \"totalHoras\": 150\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"id\": 2,\n" +
                                    "    \"nombre\": \"Monik\",\n" +
                                    "    \"apellido\": \"Gomex\",\n" +
                                    "    \"genero\": \"Femenino\",\n" +
                                    "    \"horasCubiertas\": 12,\n" +
                                    "    \"totalHoras\": 180\n" +
                                    "  }\n" +
                                    "]"
                    ))
            )
    )
    public ResponseEntity<?> obtenerHorasCubiertasPorMonitor(
            @RequestParam(required = false) Long semestreId) {
        try {
            List<ResponseHorasMonitorDTO> horasCubiertas = monitorService.obtenerHorasCubiertas(semestreId);
            return ResponseEntity.ok(horasCubiertas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/actualizarPerfil/{monitorId}")
    @Operation(
            summary = "Actualizar perfil del monitor",
            description = "Permite que un monitor actualice sus datos personales.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Datos actualizados correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            examples = "{\n" +
                                                    "  \"success\": \"Perfil actualizado correctamente\"\n" +
                                                    "}")

                            )),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Monitor no encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example =
                                                    "{\n" +
                                                            "  \"error\": \"Monitor no encontrado\"\n" +
                                                            "}"
                                    ))),
                    @ApiResponse(responseCode = "400",
                            description = "Error en la solicitud",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example =
                                                    "{\n" +
                                                            "  \"error\": \"Error al actualizar el perfil. Verifique los datos enviados.\"\n" +
                                                            "}"
                                    )))
            }
    )
    public ResponseEntity<?> actualizarPerfil(
            @PathVariable Long monitorId,
            @RequestBody RequestDTO monitorDTO) {
        try {
            return ResponseEntity.ok(monitorService.actualizarPerfil(monitorId, monitorDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.INVALID_UPDATE_PROFILE);
        }
    }
}
