package Unillanos.AsistenciaMonitor.Service;

import Unillanos.AsistenciaMonitor.Entity.Semestre;
import Unillanos.AsistenciaMonitor.Repository.SemestreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;

@Service
public class SemestreService {
    @Autowired
    private SemestreRepository semestreRepository;

    // Método para crear un semestre
    public Semestre crearSemestre(String nombre, LocalDate fechaInicio, LocalDate fechaFin) {
        Semestre semestre = new Semestre();
        semestre.setNombre(nombre);
        semestre.setFechaInicio(fechaInicio);
        semestre.setFechaFin(fechaFin);
        return semestreRepository.save(semestre);
    }

    // Método para listar todos los semestres
    public List<Semestre> listarSemestres() {
        return semestreRepository.findAll();
    }

    // Método para eliminar un semestre
    public void eliminarSemestre(Long id) {
        Semestre semestre = semestreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semestre no encontrado con ID: " + id));

        semestreRepository.delete(semestre);
    }

    // Método para editar un semestre
    public Semestre editarSemestre(Long id, String nombre, LocalDate fechaInicio, LocalDate fechaFin) {
        // Buscar el semestre por ID
        Semestre semestre = semestreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semestre no encontrado con ID: " + id));

        // Actualizar los campos del semestre
        semestre.setNombre(nombre);
        semestre.setFechaInicio(fechaInicio);
        semestre.setFechaFin(fechaFin);

        // Guardar los cambios
        return semestreRepository.save(semestre);
    }


    // Otros métodos: editar, eliminar
}
