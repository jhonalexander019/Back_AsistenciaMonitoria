package Unillanos.AsistenciaMonitor.Controller;

import Unillanos.AsistenciaMonitor.DTO.Semestre.RequestCreateSemestreDTO;
import Unillanos.AsistenciaMonitor.Entity.Semestre;
import Unillanos.AsistenciaMonitor.Service.SemestreService;
import Unillanos.AsistenciaMonitor.Utils.ErrorMessages;
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

    @PostMapping("/crear")
    @Operation(
            summary = "Crear semestres",
            description = "Permite crear un semestre con los datos especificados."
    )
    public ResponseEntity<?> crearSemestre(@RequestBody Map<String, Object> payload) {
        try {
            Semestre semestre = semestreService.crearSemestre(
                    (String) payload.get("nombre"),
                    LocalDate.parse((String) payload.get("fechaInicio")),
                    LocalDate.parse((String) payload.get("fechaFin"))
            );
            return ResponseEntity.ok(semestre);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @GetMapping("/listar")
    @Operation(
            summary = "Listar semestres",
            description = "Devuelve una lista de todos los semestres registrados."
    )
    public ResponseEntity<List<Semestre>> listarSemestres() {
        List<Semestre> semestres = semestreService.listarSemestres();
        return ResponseEntity.ok(semestres);
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(
            summary = "Eliminar semestre",
            description = "Elimina un semestre por su ID."
    )
    public ResponseEntity<?> eliminarSemestre(@PathVariable Long id) {
        try {
            semestreService.eliminarSemestre(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(ErrorMessages.DELETE_SUCCESS_SEMESTER);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

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
    public ResponseEntity<?> editarSemestre(@PathVariable Long id, @RequestBody RequestCreateSemestreDTO semestreDTO) {
        try {
            return ResponseEntity.ok(semestreService.editarSemestre(id, semestreDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( e.getMessage());
        }
    }
}