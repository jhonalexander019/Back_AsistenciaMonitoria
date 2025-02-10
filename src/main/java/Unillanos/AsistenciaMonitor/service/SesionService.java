package Unillanos.AsistenciaMonitor.service;

import Unillanos.AsistenciaMonitor.dto.usuario.ResponseSessionUsuarioDTO;
import Unillanos.AsistenciaMonitor.entity.Monitor;
import Unillanos.AsistenciaMonitor.entity.Usuario;
import Unillanos.AsistenciaMonitor.mapper.UsuarioMapper;
import Unillanos.AsistenciaMonitor.repository.UsuarioRepository;
import Unillanos.AsistenciaMonitor.utils.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SesionService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private CommonService getData;
    public ResponseSessionUsuarioDTO iniciarSesion(Integer codigo) {
        Usuario usuario = getData.obtenerUsuarioPorCodigo(codigo);
        if (usuario.getRol().getNombre().equals("Admin")){
            return usuarioMapper.toResponseSessionUsuarioDTO(usuario);
        }

        Monitor monitor = getData.obtenerMonitorPorIdUsuario(usuario.getId());
        return usuarioMapper.toResponseSessionUsuarioDTO(monitor);
    }
}
