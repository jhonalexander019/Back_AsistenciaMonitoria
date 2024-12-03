package Unillanos.AsistenciaMonitor.Controller;

import Unillanos.AsistenciaMonitor.Entity.Usuario;
import Unillanos.AsistenciaMonitor.Service.SesionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Void> iniciarSesion(@RequestParam Integer codigo) {
        try {
            sesionService.iniciarSesion(codigo);
            return ResponseEntity.ok().build(); // Estado 200
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build(); // Estado 400 si hay error
        }
    }

}
