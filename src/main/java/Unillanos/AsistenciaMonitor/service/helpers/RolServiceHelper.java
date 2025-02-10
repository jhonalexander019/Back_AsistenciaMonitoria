package Unillanos.AsistenciaMonitor.service.helpers;

import Unillanos.AsistenciaMonitor.entity.Rol;
import Unillanos.AsistenciaMonitor.mapper.RolMapper;
import Unillanos.AsistenciaMonitor.repository.RolRepository;
import Unillanos.AsistenciaMonitor.exception.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolServiceHelper {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    @Autowired
    public RolServiceHelper(RolRepository rolRepository, RolMapper rolMapper) {
        this.rolRepository = rolRepository;
        this.rolMapper = rolMapper;
    }

    public Rol obtenerRolMonitor() {
        return rolRepository.findByNombre("Monitor")
                .orElseThrow(() -> new RuntimeException(ErrorMessages.ROL_NOT_FOUND));
    }
}
