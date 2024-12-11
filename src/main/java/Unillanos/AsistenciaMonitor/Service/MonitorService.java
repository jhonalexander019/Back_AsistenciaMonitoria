package Unillanos.AsistenciaMonitor.Service;

import Unillanos.AsistenciaMonitor.Entity.Monitor;
import Unillanos.AsistenciaMonitor.Entity.Semestre;
import Unillanos.AsistenciaMonitor.Entity.Usuario;
import Unillanos.AsistenciaMonitor.Entity.Asistencia;

import Unillanos.AsistenciaMonitor.Entity.Rol;
import Unillanos.AsistenciaMonitor.Repository.*;

import java.time.LocalDate;
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

    // Genera el codigo de acceso
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

    // Listar monitores por semestre vigente
    public List<Map<String, Object>> listarMonitoresPorSemestre() {
        LocalDate fechaActual = LocalDate.now(); // Obtener la fecha actual

        // Buscar el semestre vigente
        Semestre semestreVigente = semestreRepository.findAll().stream()
                .filter(semestre ->
                        !fechaActual.isBefore(semestre.getFechaInicio()) && !fechaActual.isAfter(semestre.getFechaFin())
                )
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay un semestre vigente en la fecha actual."));

        // Obtener los monitores del semestre vigente
        List<Monitor> monitores = monitorRepository.findBySemestreId(semestreVigente.getId());

        // Mapear los monitores a una estructura de datos adecuada
        return monitores.stream()
                .map(monitor -> {
                    Map<String, Object> monitorMap = new HashMap<>();
                    monitorMap.put("id", monitor.getId());
                    monitorMap.put("nombre", monitor.getUsuario().getNombre());
                    monitorMap.put("apellido", monitor.getUsuario().getApellido());
                    monitorMap.put("codigo", monitor.getUsuario().getCodigo());
                    monitorMap.put("genero", monitor.getUsuario().getGenero());
                    monitorMap.put("semestre", monitor.getSemestre().getNombre());
                    return monitorMap;
                }).collect(Collectors.toList());
    }


    // Listar monitores por un día específico en su horario
    public Map<String, List<Map<String, String>>> listarMonitoresPorHorario(String dia) {
        if (dia == null || dia.trim().isEmpty()) {
            throw new IllegalArgumentException("El día proporcionado no es válido.");
        }

        LocalDate fechaActual = LocalDate.now(); // Obtener la fecha actual

        // Filtrar monitores que tengan semestres vigentes y asignaciones para el día dado
        List<Monitor> monitores = monitorRepository.findByDiasAsignadosContaining(dia).stream()
                .filter(m -> {
                    Semestre semestre = m.getSemestre();
                    // Validar si el semestre está vigente
                    return !fechaActual.isBefore(semestre.getFechaInicio()) && !fechaActual.isAfter(semestre.getFechaFin());
                })
                .collect(Collectors.toList());

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

    // Control de horas
    public List<Map<String, Object>> obtenerHorasCubiertas() {
        LocalDate fechaActual = LocalDate.now(); // Obtener la fecha actual

        List<Monitor> monitores = monitorRepository.findAll();

        return monitores.stream()
                .filter(monitor -> {
                    Semestre semestre = monitor.getSemestre();
                    // Validar si el semestre está vigente
                    return !fechaActual.isBefore(semestre.getFechaInicio()) && !fechaActual.isAfter(semestre.getFechaFin());
                })
                .map(monitor -> {
                    // Cambiar a mapToDouble para manejar valores double
                    double horasCubiertas = asistenciaRepository
                            .findByMonitorIdAndEstado(monitor.getId(), "Presente")
                            .stream()
                            .mapToDouble(Asistencia::getHorasCubiertas) // Cambiado a mapToDouble
                            .sum(); // Utiliza sum() para double

                    // Crear el mapa de resultados
                    Map<String, Object> resultado = new HashMap<>();
                    resultado.put("id", monitor.getId());
                    resultado.put("nombre", monitor.getUsuario().getNombre());
                    resultado.put("apellido", monitor.getUsuario().getApellido());
                    resultado.put("genero", monitor.getUsuario().getGenero());
                    resultado.put("horasCubiertas", horasCubiertas);
                    resultado.put("totalHoras", monitor.getTotalHoras());
                    return resultado;
                }).collect(Collectors.toList());
    }


    public void actualizarPerfil(Long monitorId, Map<String, Object> payload) {
        // Buscar el monitor por ID
        Monitor monitor = monitorRepository.findById(monitorId)
                .orElseThrow(() -> new RuntimeException("Monitor no encontrado"));

        // Actualizar los datos del monitor y usuario
        Usuario usuario = monitor.getUsuario();
        if (payload.containsKey("nombre")) {
            usuario.setNombre((String) payload.get("nombre"));
        }
        if (payload.containsKey("apellido")) {
            usuario.setApellido((String) payload.get("apellido"));
        }
        if (payload.containsKey("correo")) {
            usuario.setCorreo((String) payload.get("correo"));
        }
        if (payload.containsKey("genero")) {
            usuario.setGenero((String) payload.get("genero"));
        }
        if (payload.containsKey("totalHoras")) {
            monitor.setTotalHoras((Integer) payload.get("totalHoras"));
        }

        // Guardar cambios
        usuarioRepository.save(usuario);
        monitorRepository.save(monitor);
    }

}
