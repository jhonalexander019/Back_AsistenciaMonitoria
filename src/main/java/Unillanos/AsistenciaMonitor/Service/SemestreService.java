package Unillanos.AsistenciaMonitor.Service;

import Unillanos.AsistenciaMonitor.DTO.Semestre.RequestCreateSemestreDTO;
import Unillanos.AsistenciaMonitor.DTO.Semestre.ResponseCreateSemestreDTO;
import Unillanos.AsistenciaMonitor.Entity.Semestre;
import Unillanos.AsistenciaMonitor.Mapper.SemestreMapper;
import Unillanos.AsistenciaMonitor.Repository.SemestreRepository;

import Unillanos.AsistenciaMonitor.Utils.ErrorMessages;
import Unillanos.AsistenciaMonitor.Utils.GetData;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class SemestreService {
    @Autowired
    private SemestreRepository semestreRepository;
    @Autowired
    private SemestreMapper semestreMapper;
    @Autowired
    private GetData getData;

    public Semestre crearSemestre(String nombre, LocalDate fechaInicio, LocalDate fechaFin) {
        boolean solapado = getData.obtenerSemestrePorFecha(fechaInicio, fechaFin);

        if (!solapado){
            throw new RuntimeException(ErrorMessages.CREATE_INVALID_SEMESTER);
        }

        Semestre semestre = new Semestre();
        semestre.setNombre(nombre);
        semestre.setFechaInicio(fechaInicio);
        semestre.setFechaFin(fechaFin);
        return semestreRepository.save(semestre);
    }

    public List<Semestre> listarSemestres() {
        return semestreRepository.findAll();
    }

    public void eliminarSemestre(Long id) {
        boolean modificable = getData.validarExistenciaMonitoresEnSemestreId(id);

        if (modificable){
            throw new RuntimeException(ErrorMessages.DELETE_INVALID_SEMESTER);
        }

        Semestre semestre = semestreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.SEMESTER_NOT_FOUND));

        semestreRepository.delete(semestre);
    }

    public ResponseCreateSemestreDTO editarSemestre(Long id, RequestCreateSemestreDTO semestreDTO) {
        Semestre semestre = getData.obtenerSemestrePorId(id);

        if (semestre.getFechaFin().isBefore(getData.obtenerSemestreVigente().getFechaInicio()))
            throw new RuntimeException(ErrorMessages.EDIT_INVALID_SEMESTER);

        if (semestreDTO.getNombre() != null ) {
            semestre.setNombre(semestreDTO.getNombre());
        }
        if (semestreDTO.getFechaInicio() != null) {
            semestre.setFechaInicio(semestreDTO.getFechaInicio());
        }
        if (semestreDTO.getFechaFin() != null) {
            semestre.setFechaFin(semestreDTO.getFechaFin());
        }

        return semestreMapper.toDTO(semestreRepository.save(semestre));
    }
}
