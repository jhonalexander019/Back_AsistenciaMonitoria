package Unillanos.AsistenciaMonitor.controller;

import Unillanos.AsistenciaMonitor.dto.usuario.ResponseSessionUsuarioDTO;
import Unillanos.AsistenciaMonitor.service.SesionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sesion")
@Tag(name = "Sesión", description = "Endpoints para el manejo de sesiones y autenticación")
public class SesionController extends BaseController {

    @Autowired
    private SesionService sesionService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<?> iniciarSesion(@RequestParam Integer codigo) {
        try {
            ResponseSessionUsuarioDTO usuarioInfo = sesionService.iniciarSesion(codigo);
            return createSuccessResponse(usuarioInfo);
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
