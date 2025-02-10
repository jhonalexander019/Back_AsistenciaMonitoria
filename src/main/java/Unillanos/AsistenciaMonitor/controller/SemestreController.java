package Unillanos.AsistenciaMonitor.controller;

import Unillanos.AsistenciaMonitor.dto.semestre.RequestCreateSemestreDTO;
import Unillanos.AsistenciaMonitor.entity.Semestre;
import Unillanos.AsistenciaMonitor.service.SemestreService;
import Unillanos.AsistenciaMonitor.exception.ErrorMessages;
import io.swagger.v3.oas.annotations.Operation;
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
public class SemestreController extends BaseController {

    @Autowired
    private SemestreService semestreService;

    @PostMapping("/crear")
    @Operation(summary = "Crear semestres")
    public ResponseEntity<?> crearSemestre(@RequestBody Map<String, Object> payload) {
        try {
            Semestre semestre = semestreService.crearSemestre(
                    (String) payload.get("nombre"),
                    LocalDate.parse((String) payload.get("fechaInicio")),
                    LocalDate.parse((String) payload.get("fechaFin"))
            );
            return createSuccessResponse(semestre);
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar semestres")
    public ResponseEntity<List<Semestre>> listarSemestres() {
        return createSuccessResponse(semestreService.listarSemestres());
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar semestre")
    public ResponseEntity<?> eliminarSemestre(@PathVariable Long id) {
        try {
            semestreService.eliminarSemestre(id);
            return createSuccessResponse(ErrorMessages.DELETE_SUCCESS_SEMESTER);
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/editar/{id}")
    @Operation(summary = "Editar semestre")
    public ResponseEntity<?> editarSemestre(@PathVariable Long id, @RequestBody RequestCreateSemestreDTO semestreDTO) {
        try {
            return createSuccessResponse(semestreService.editarSemestre(id, semestreDTO));
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
