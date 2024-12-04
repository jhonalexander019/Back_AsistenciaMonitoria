package Unillanos.AsistenciaMonitor.Service;

import Unillanos.AsistenciaMonitor.Entity.Monitor;
import Unillanos.AsistenciaMonitor.Entity.Semestre;
import Unillanos.AsistenciaMonitor.Entity.Usuario;
import Unillanos.AsistenciaMonitor.Entity.Asistencia;

import Unillanos.AsistenciaMonitor.Entity.Rol;
import Unillanos.AsistenciaMonitor.Repository.*;

import java.util.HashMap;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class MonitorService {
    private final MonitorRepository monitorRepository;
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final SemestreRepository semestreRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

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

        // Verificar si ya existe un monitor con el correo
        boolean existeMonitor = monitorRepository.existsByUsuario_CorreoAndSemestre_Id(correo, semestreId);
        if (existeMonitor) {
            throw new RuntimeException("Ya existe un monitor con el mismo correo en este semestre.");
        }

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

    // Eliminar un monitor por su ID
    public void eliminarMonitor(Long id) {
        Monitor monitor = monitorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monitor no encontrado"));

        monitorRepository.delete(monitor);
    }

    // Listar monitores por un día específico en su horario
    public Map<String, List<Map<String, String>>> listarMonitoresPorHorario(String dia) {
        if (dia == null || dia.trim().isEmpty()) {
            throw new IllegalArgumentException("El día proporcionado no es válido.");
        }

        List<Monitor> monitores = monitorRepository.findByDiasAsignadosContaining(dia);

        if (monitores.isEmpty()) {
            throw new RuntimeException("No hay monitores asignados para el día: " + dia);
        }

        // Filtrar monitores según la mañana y la tarde
        List<Map<String, String>> monitoresManana = monitores.stream()
                .filter(m -> m.getDiasAsignados().contains(dia + "Mañana"))
                .map(m -> Map.of("nombre", m.getUsuario().getNombre(), "apellido", m.getUsuario().getApellido()))
                .collect(Collectors.toList());

        List<Map<String, String>> monitoresTarde = monitores.stream()
                .filter(m -> m.getDiasAsignados().contains(dia + "Tarde"))
                .map(m -> Map.of("nombre", m.getUsuario().getNombre(), "apellido", m.getUsuario().getApellido()))
                .collect(Collectors.toList());

        // Crear el mapa de respuesta
        Map<String, List<Map<String, String>>> resultado = new HashMap<>();
        resultado.put("Mañana", monitoresManana);
        resultado.put("Tarde", monitoresTarde);

        return resultado;
    }

    // Perfil del monitor
    public Map<String, Object> obtenerPerfilMonitor(Long id) {
        Monitor monitor = monitorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monitor no encontrado con ID: " + id));

        // Construir el perfil con la información principal
        Map<String, Object> perfil = new HashMap<>();
        perfil.put("id", monitor.getId());
        perfil.put("nombre", monitor.getUsuario().getNombre());
        perfil.put("apellido", monitor.getUsuario().getApellido());
        perfil.put("correo", monitor.getUsuario().getCorreo());
        perfil.put("genero", monitor.getUsuario().getGenero());
        perfil.put("semestre", monitor.getSemestre().getNombre());
        perfil.put("totalHoras", monitor.getTotalHoras());
        perfil.put("codigo", monitor.getUsuario().getCodigo());

        return perfil;
    }
    public List<Map<String, Object>> obtenerHorasCubiertas() {
        List<Monitor> monitores = monitorRepository.findAll();

        return monitores.stream().map(monitor -> {
            int horasCubiertas = asistenciaRepository
                    .findByMonitorIdAndEstado(monitor.getId(), "Presente")
                    .stream()
                    .mapToInt(Asistencia::getHorasCubiertas)
                    .sum();

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("id", monitor.getId());
            resultado.put("nombre", monitor.getUsuario().getNombre());
            resultado.put("apellido", monitor.getUsuario().getApellido());
            resultado.put("horasCubiertas", horasCubiertas);
            resultado.put("totalHoras", monitor.getTotalHoras());
            return resultado;
        }).collect(Collectors.toList());
    }



}
