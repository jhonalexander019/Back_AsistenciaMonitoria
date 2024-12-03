package Unillanos.AsistenciaMonitor.Repository;

import Unillanos.AsistenciaMonitor.Entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
    // Buscar un rol por nombre
    boolean existsByNombre(String nombre);
}