package Unillanos.AsistenciaMonitor.service.helpers;

import Unillanos.AsistenciaMonitor.dto.monitor.RequestDTO;
import Unillanos.AsistenciaMonitor.dto.usuario.RequestCreateUsuarioDTO;
import Unillanos.AsistenciaMonitor.entity.Usuario;
import Unillanos.AsistenciaMonitor.mapper.UsuarioMapper;
import Unillanos.AsistenciaMonitor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UsuarioServiceHelper {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Autowired
    public UsuarioServiceHelper(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }


    public void actualizarDatosUsuario(Usuario usuario, RequestDTO monitorDTO) {
        if (monitorDTO.getNombre() != null) usuario.setNombre(monitorDTO.getNombre());
        if (monitorDTO.getApellido() != null) usuario.setApellido(monitorDTO.getApellido());
        if (monitorDTO.getCorreo() != null) usuario.setCorreo(monitorDTO.getCorreo());
        if (monitorDTO.getGenero() != null) usuario.setGenero(monitorDTO.getGenero());
        usuarioRepository.save(usuario);
    }

    public Integer generarCodigoUnico(Random random, java.util.function.Predicate<Integer> existeCodigo) {
        Integer codigo;
        do {
            codigo = 1000 + random.nextInt(9000); // Genera un número entre 1000 y 9999
        } while (existeCodigo.test(codigo));
        return codigo;
    }
}
