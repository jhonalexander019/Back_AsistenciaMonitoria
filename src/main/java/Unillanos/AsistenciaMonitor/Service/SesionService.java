package Unillanos.AsistenciaMonitor.Service;

import Unillanos.AsistenciaMonitor.Entity.Usuario;
import Unillanos.AsistenciaMonitor.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class SesionService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    //Valida el codigo para la sesion
    public Usuario iniciarSesion(Integer codigo) {
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Código inválido o usuario no encontrado"));
    }
}
