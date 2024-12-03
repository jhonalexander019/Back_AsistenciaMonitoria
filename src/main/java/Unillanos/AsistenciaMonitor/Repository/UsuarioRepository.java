package Unillanos.AsistenciaMonitor.Repository;

import Unillanos.AsistenciaMonitor.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Buscar un usuario por codigo
    boolean existsByCodigo(Integer codigo);
    // Buscar un usuario por correo
    boolean existsByCorreo(String correo);
    //Busca un usuario por código único y el Optional contiene al usuario si se encuentra
    Optional<Usuario> findByCodigo(Integer codigo);

}
