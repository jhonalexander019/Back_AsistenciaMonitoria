package Unillanos.AsistenciaMonitor.Controller;

import Unillanos.AsistenciaMonitor.Entity.Semestre;
import Unillanos.AsistenciaMonitor.Service.SemestreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/semestres")
public class SemestreController {
    @Autowired
    private SemestreService semestreService;

    @PostMapping("/crear")
    public ResponseEntity<Semestre> crearSemestre(@RequestBody Map<String, Object> payload) {
        Semestre semestre = semestreService.crearSemestre(
                (String) payload.get("nombre"),
                LocalDate.parse((String) payload.get("fecha_inicio_semestre")),
                LocalDate.parse((String) payload.get("fecha_fin_semestre"))
        );
        return ResponseEntity.ok(semestre);
    }

    // Otros endpoints: editar, eliminar
}
