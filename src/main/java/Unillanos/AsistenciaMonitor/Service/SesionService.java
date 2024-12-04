package Unillanos.AsistenciaMonitor.Service;

import Unillanos.AsistenciaMonitor.Entity.Usuario;
import Unillanos.AsistenciaMonitor.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SesionService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Valida el código para la sesión y devuelve un mapa con los datos formateados
    public Map<String, Object> iniciarSesion(Integer codigo) {
        Usuario usuario = usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Código inválido o usuario no encontrado"));

        // Construir y devolver los datos del usuario
        Map<String, Object> usuarioInfo = new HashMap<>();
        usuarioInfo.put("id", usuario.getId());
        usuarioInfo.put("nombre", usuario.getNombre());
        usuarioInfo.put("genero", usuario.getGenero());
        usuarioInfo.put("codigo", usuario.getCodigo());
        usuarioInfo.put("rol", usuario.getRol().getNombre());

        return usuarioInfo;
    }
}

