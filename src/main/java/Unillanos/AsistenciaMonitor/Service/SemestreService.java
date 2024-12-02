package Unillanos.AsistenciaMonitor.Service;

import Unillanos.AsistenciaMonitor.Entity.Semestre;
import Unillanos.AsistenciaMonitor.Repository.SemestreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SemestreService {
    @Autowired
    private SemestreRepository semestreRepository;

    public Semestre crearSemestre(String nombre, LocalDate fechaInicio, LocalDate fechaFin) {
        Semestre semestre = new Semestre();
        semestre.setNombre(nombre);
        semestre.setFechaInicio(fechaInicio);
        semestre.setFechaFin(fechaFin);
        return semestreRepository.save(semestre);
    }

    // Otros métodos: editar, eliminar
}
