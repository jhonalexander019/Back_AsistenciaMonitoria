package Unillanos.AsistenciaMonitor.mapper;

import Unillanos.AsistenciaMonitor.dto.asistencia.ResponseCreateAsistenciaDTO;
import Unillanos.AsistenciaMonitor.entity.Asistencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AsistenciaMapper {

    @Autowired
    private MonitorMapper monitorMapper;

    public ResponseCreateAsistenciaDTO toDTO(Asistencia entity) {
        if (entity == null) return null;

        ResponseCreateAsistenciaDTO dto = new ResponseCreateAsistenciaDTO();
        dto.setId(entity.getId());
        dto.setFecha(entity.getFecha());
        dto.setEstado(entity.getEstado());
        dto.setHorasCubiertas(entity.getHorasCubiertas());
        dto.setJornada(entity.getJornada());
        dto.setMonitor(monitorMapper.toResponseCreateMonitorDTO(entity.getMonitor()));
        return dto;
    }
}