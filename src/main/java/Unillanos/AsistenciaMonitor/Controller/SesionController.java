package Unillanos.AsistenciaMonitor.Controller;

import Unillanos.AsistenciaMonitor.Service.SesionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sesion")
@Tag(name = "Sesión", description = "Endpoints para el manejo de sesiones y autenticación")
public class SesionController {
    @Autowired
    private SesionService sesionService;

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Permite a un usuario iniciar sesión utilizando su código único asignado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inicio de sesión exitoso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{ \"codigo\": 7060, \"genero\": \"Masculino\", \"id\": 1, \"nombre\": \"Sistemas\", \"rol\": \"Admin\" }"
                            )
                    )
            ),
    })
    public ResponseEntity<Map<String, Object>> iniciarSesion(@RequestParam Integer codigo) {
        try {
            Map<String, Object> usuarioInfo = sesionService.iniciarSesion(codigo);
            return ResponseEntity.ok(usuarioInfo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
