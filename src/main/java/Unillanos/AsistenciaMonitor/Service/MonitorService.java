package Unillanos.AsistenciaMonitor.Service;

import Unillanos.AsistenciaMonitor.Entity.Monitor;
import Unillanos.AsistenciaMonitor.Entity.Semestre;
import Unillanos.AsistenciaMonitor.Entity.Usuario;
import Unillanos.AsistenciaMonitor.Entity.Rol;
import Unillanos.AsistenciaMonitor.Repository.MonitorRepository;
import Unillanos.AsistenciaMonitor.Repository.SemestreRepository;
import Unillanos.AsistenciaMonitor.Repository.UsuarioRepository;
import Unillanos.AsistenciaMonitor.Repository.RolRepository;
import java.util.Random;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class MonitorService {
    private final MonitorRepository monitorRepository;
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final SemestreRepository semestreRepository;

    public MonitorService(MonitorRepository monitorRepository, RolRepository rolRepository,
                          UsuarioRepository usuarioRepository, SemestreRepository semestreRepository) {
        this.monitorRepository = monitorRepository;
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.semestreRepository = semestreRepository;
    }

    // Crear monitor
    public Monitor crearMonitor(String nombre, String apellido, String diasAsignados, Integer totalHoras,
                                String correo, String genero, Long semestreId) {

        // Obtener el rol de "Monitor"
        Rol rolMonitor = rolRepository.findByNombre("Monitor")
                .orElseThrow(() -> new RuntimeException("Rol 'Monitor' no encontrado"));

        // Generar código único
        Integer codigoUnico = generarCodigoUnico();

        // Crear y guardar el usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreo(correo);
        usuario.setGenero(genero);
        usuario.setRol(rolMonitor);
        usuario.setCodigo(codigoUnico);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuarioRepository.save(usuario);

        // Obtener el semestre
        Semestre semestre = semestreRepository.findById(semestreId)
                .orElseThrow(() -> new RuntimeException("Semestre no encontrado"));


        // Crear y guardar el monitor
        Monitor monitor = new Monitor();
        monitor.setUsuario(usuario);
        monitor.setSemestre(semestre);
        monitor.setTotalHoras(totalHoras);
        monitor.setDiasAsignados(diasAsignados);
        return monitorRepository.save(monitor);
    }

    private Integer generarCodigoUnico() {
        Random random = new Random();
        Integer codigo;
        boolean existe;

        do {
            codigo = 1000 + random.nextInt(9000); // Genera un número entre 1000 y 9999
            existe = usuarioRepository.existsByCodigo(codigo);
        } while (existe);

        return codigo;
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
