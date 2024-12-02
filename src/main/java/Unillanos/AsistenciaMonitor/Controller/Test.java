package Unillanos.AsistenciaMonitor.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class Test {

    @GetMapping("/crear")
    public void crearSemestre() {
        System.out.println("llega");
    }
}
