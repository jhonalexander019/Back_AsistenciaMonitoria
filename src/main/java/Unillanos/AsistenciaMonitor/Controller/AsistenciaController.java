package Unillanos.AsistenciaMonitor.Controller;

import Unillanos.AsistenciaMonitor.Entity.Asistencia;
import Unillanos.AsistenciaMonitor.Service.AsistenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.List;


@RestController
@RequestMapping("/asistencias")
@Tag(name = "Asistencias", description = "Endpoints para la gestión de las asistencias")
public class AsistenciaController {
    @Autowired
    private AsistenciaService asistenciaService;

    @PostMapping("/registrar")
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
    public ResponseEntity<Asistencia> registrarAsistencia(@RequestParam Long monitorId) {
        try {
            Asistencia asistencia = asistenciaService.registrarAsistencia(monitorId);
            return ResponseEntity.ok(asistencia);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // O manejar el error con un mensaje más detallado
        }
    }



    @GetMapping("/filtrar")
    @Operation(
            summary = "Filtrar asistencias",
            description = "Lista las asistencias de monitores filtradas por monitor, estado y semestre.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado de asistencias filtradas",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(
                                            example = "[\n" +
                                                    "  {\n" +
                                                    "    \"id\": 1,\n" +
                                                    "    \"nombreMonitor\": \"Jhon\",\n" +
                                                    "    \"apellidoMonitor\": \"Roa\",\n" +
                                                    "    \"semestre\": \"2024-1\",\n" +
                                                    "    \"horasCubiertas\": 4\n" +
                                                    "  },\n" +
                                                    "  {\n" +
                                                    "    \"id\": 3,\n" +
                                                    "    \"nombreMonitor\": \"Jhon\",\n" +
                                                    "    \"apellidoMonitor\": \"Roa\",\n" +
                                                    "    \"semestre\": \"2024-1\",\n" +
                                                    "    \"horasCubiertas\": 4\n" +
                                                    "  }\n" +
                                                    "]"
                                    ))
                            )
                    ),
            }
    )
    public ResponseEntity<List<Map<String, Object>>> listarAsistenciasFiltradas(
            @RequestParam Long monitorId,
            @RequestParam String estado,
            @RequestParam String semestre) {
        List<Map<String, Object>> asistencias = asistenciaService.listarAsistenciasPorEstadoYSemestre(monitorId, estado, semestre);
        return ResponseEntity.ok(asistencias);
    }

}
