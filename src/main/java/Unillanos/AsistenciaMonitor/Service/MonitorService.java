package Unillanos.AsistenciaMonitor.Service;

import Unillanos.AsistenciaMonitor.Entity.Monitor;
import Unillanos.AsistenciaMonitor.Entity.Semestre;
import Unillanos.AsistenciaMonitor.Entity.Usuario;
import Unillanos.AsistenciaMonitor.Entity.Rol;
import Unillanos.AsistenciaMonitor.Repository.MonitorRepository;
import Unillanos.AsistenciaMonitor.Repository.SemestreRepository;
import Unillanos.AsistenciaMonitor.Repository.UsuarioRepository;
import Unillanos.AsistenciaMonitor.Repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class MonitorService {
    @Autowired
    private MonitorRepository monitorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SemestreRepository semestreRepository;

    @Autowired
    private RolRepository rolRepository;

    // Crear monitor
    public Monitor crearMonitor(String nombre, String apellido, String diasAsignados, Integer totalHoras,
                                String correo, String codigoEstudiantil, String telefono, String genero, Long semestreId) {

        // Obtener el rol de "Monitor"
        Rol rolMonitor = rolRepository.findByNombre("Monitor")
                .orElseThrow(() -> new RuntimeException("Rol 'Monitor' no encontrado"));

        // Crear y guardar el usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreo(correo);
        usuario.setRol(rolMonitor);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuarioRepository.save(usuario);

        // Obtener el semestre
        Semestre semestre = semestreRepository.findById(semestreId)
                .orElseThrow(() -> new RuntimeException("Semestre no encontrado"));

        // Crear y guardar el monitor
        Monitor monitor = new Monitor();
        monitor.setUsuario(usuario);
        monitor.setGenero(genero);
        monitor.setSemestre(semestre);
        monitor.setTotalHoras(totalHoras);
        monitor.setDiasAsignados(diasAsignados);
        return monitorRepository.save(monitor);
    }

    // Listar todos los monitores
    public List<Monitor> listarMonitores() {
        return monitorRepository.findAll();
    }

    // Listar monitores por un día específico en su horario
    public List<Monitor> listarMonitoresPorDia(String dia) {
        return monitorRepository.findByDiasAsignadosContaining(dia);
    }

    // Obtener monitor por ID
    public Monitor obtenerMonitorPorId(Long id) {
        return monitorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monitor no encontrado con ID: " + id));
    }

    // Eliminar un monitor por su ID
    public void eliminarMonitor(Long id) {
        Monitor monitor = monitorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monitor no encontrado"));

        monitorRepository.delete(monitor);
    }
}
