package Unillanos.AsistenciaMonitor.Service;

import Unillanos.AsistenciaMonitor.DTO.Usuario.ResponseSessionUsuarioDTO;
import Unillanos.AsistenciaMonitor.Entity.Monitor;
import Unillanos.AsistenciaMonitor.Entity.Usuario;
import Unillanos.AsistenciaMonitor.Mapper.UsuarioMapper;
import Unillanos.AsistenciaMonitor.Repository.UsuarioRepository;
import Unillanos.AsistenciaMonitor.Utils.ErrorMessages;
import Unillanos.AsistenciaMonitor.Utils.GetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SesionService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private GetData getData;
    public ResponseSessionUsuarioDTO iniciarSesion(Integer codigo) {
        Usuario usuario = getData.obtenerUsuarioPorCodigo(codigo);
        if (usuario.getRol().getNombre().equals("Admin")){
            return usuarioMapper.toResponseSessionUsuarioDTO(usuario);
        }

        Monitor monitor = getData.obtenerMonitorPorIdUsuario(usuario.getId());
        return usuarioMapper.toResponseSessionUsuarioDTO(monitor);
    }
}
