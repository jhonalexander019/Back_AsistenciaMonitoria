package Unillanos.AsistenciaMonitor.Jobs;

import Unillanos.AsistenciaMonitor.Service.AsistenciaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component @EnableScheduling
public class AsistenciaRegisterJob {
    @Autowired
    private AsistenciaService asistenciaService;

    @Scheduled(cron = "0 5 12 * * MON-FRI") // Ejecuta todos los días hábiles a las 1:05 PM
    public void registrarAsistenciasMañana() {
        System.out.println("Entró en la tarea de registrar asistencias de la mañana.");
        asistenciaService.registrarAsistenciasAutomaticas("Mañana");
    }

    @Scheduled(cron = "0 5 17 * * MON-FRI") // Ejecuta todos los días hábiles a las 5:05 PM
    public void registrarAsistenciasTarde() {
        System.out.println("Entró en la tarea de registrar asistencias de la tarde.");
        asistenciaService.registrarAsistenciasAutomaticas("Tarde");
    }
}
