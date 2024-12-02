package Unillanos.AsistenciaMonitor.Service;

import Unillanos.AsistenciaMonitor.Entity.Monitor;
import Unillanos.AsistenciaMonitor.Entity.Semestre;
import Unillanos.AsistenciaMonitor.Entity.Usuario;
import Unillanos.AsistenciaMonitor.Repository.MonitorRepository;
import Unillanos.AsistenciaMonitor.Repository.SemestreRepository;
import Unillanos.AsistenciaMonitor.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MonitorService {
    @Autowired
    private MonitorRepository monitorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SemestreRepository semestreRepository;

    public Monitor crearMonitor(String nombre, String apellido, String diasAsignados, Integer totalHoras,
                                String correo, String codigoEstudiantil, String telefono, String genero, Long semestreId) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreo(correo);
        usuario.setRol("MONITOR");
        usuario.setFechaCreacion(LocalDateTime.now());
        usuarioRepository.save(usuario);

        Semestre semestre = semestreRepository.findById(semestreId)
                .orElseThrow(() -> new RuntimeException("Semestre no encontrado"));

        Monitor monitor = new Monitor();
        monitor.setUsuario(usuario);
        monitor.setCodigoEstudiantil(codigoEstudiantil);
        monitor.setTelefono(telefono);
        monitor.setGenero(genero);
        monitor.setSemestre(semestre);
        monitor.setTotalHoras(totalHoras);
        monitor.setDiasAsignados(diasAsignados);
        return monitorRepository.save(monitor);
    }

    // Otros métodos: editar, eliminar, listar por turno
}
