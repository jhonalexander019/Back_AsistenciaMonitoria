package Unillanos.AsistenciaMonitor.Controller;

import Unillanos.AsistenciaMonitor.Entity.Semestre;
import Unillanos.AsistenciaMonitor.Service.SemestreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/semestres")
@Tag(name = "Semestres", description = "Endpoints para la gestión de los semestres")
public class SemestreController {
    @Autowired
    private SemestreService semestreService;

    // Endpoint para crear un semestre
    @PostMapping("/crear")
    @Operation(
            summary = "Crear semestres",
            description = "Permite crear un semestre con los datos especificados."
    )
    public ResponseEntity<Semestre> crearSemestre(@RequestBody Map<String, Object> payload) {
        Semestre semestre = semestreService.crearSemestre(
                (String) payload.get("nombre"),
                LocalDate.parse((String) payload.get("fecha_inicio_semestre")),
                LocalDate.parse((String) payload.get("fecha_fin_semestre"))
        );
        return ResponseEntity.ok(semestre);
    }

    // Endpoint para listar todos los semestres
    @GetMapping("/listar")
    @Operation(
            summary = "Listar semestres",
            description = "Devuelve una lista de todos los semestres registrados."
    )
    public ResponseEntity<List<Semestre>> listarSemestres() {
        List<Semestre> semestres = semestreService.listarSemestres();
        return ResponseEntity.ok(semestres);
    }

    // Endpoint para eliminar un semestre por ID
    @DeleteMapping("/eliminar/{id}")
    @Operation(
            summary = "Eliminar semestre",
            description = "Elimina un semestre por su ID."
    )
    public ResponseEntity<Void> eliminarSemestre(@PathVariable Long id) {
        try {
            semestreService.eliminarSemestre(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            // No se encontró el semestre
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Endpoint para editar un semestre por ID
    @PutMapping("/editar/{id}")
    @Operation(
            summary = "Editar semestre",
            description = "Permite editar los datos de un semestre existente por su ID.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del semestre a editar",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    example = "{\"nombre\":\"2024-2\",\"fecha_inicio_semestre\":\"2024-06-01\",\"fecha_fin_semestre\":\"2024-11-30\"}"
                            )
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Semestre editado exitosamente",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "object",
                                            example = "{\"id\":1,\"nombre\":\"2024-2\",\"fechaInicio\":\"2024-06-01\",\"fechaFin\":\"2024-11-30\"}"
                                    )
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Error al editar el semestre",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "object",
                                            example = "{\"error\":\"Semestre no encontrado con ID: 123\"}"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> editarSemestre(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            Semestre semestreEditado = semestreService.editarSemestre(
                    id,
                    (String) payload.get("nombre"),
                    LocalDate.parse((String) payload.get("fecha_inicio_semestre")),
                    LocalDate.parse((String) payload.get("fecha_fin_semestre"))
            );
            return ResponseEntity.ok(semestreEditado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}