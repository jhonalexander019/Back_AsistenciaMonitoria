package Unillanos.AsistenciaMonitor;

import Unillanos.AsistenciaMonitor.Entity.Rol;
import Unillanos.AsistenciaMonitor.Entity.Usuario;
import Unillanos.AsistenciaMonitor.Repository.RolRepository;
import Unillanos.AsistenciaMonitor.Repository.UsuarioRepository;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@OpenAPIDefinition(info = @Info(title = "Asistencia Monitores Unillanos",
        version = "1.0.3",
        description = "APIs Swagger servicio asistencia de monitores Unillanos",
        license = @License(name = "Apache 2.0"),
		contact = @Contact(url = "", name = "", email = "")),
        security = {@SecurityRequirement(name = "")},
        servers = {
                @Server(description = "ambiente local", url = "http://localhost:8080/"),
        }
)
@SpringBootApplication
@EnableAsync
public class AsistenciaMonitorApplication{
	@Autowired
	private RolRepository rolRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	public static void main(String[]args) {
		SpringApplication.run(AsistenciaMonitorApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData() {
		return args -> {
			// Crear roles
			if (!rolRepository.existsByNombre("Admin")) {
				rolRepository.save(new Rol("Admin"));
			}
			if (!rolRepository.existsByNombre("Monitor")) {
				rolRepository.save(new Rol("Monitor"));
			}

			// Crear usuario administrador
			if (!usuarioRepository.existsByCorreo("sistemas@unillanos.edu.co")) {
				Usuario adminUser = new Usuario();
				adminUser.setNombre("Sistemas");
				adminUser.setApellido("Administrador");
				adminUser.setCorreo("sistemas@unillanos.edu.co");
				adminUser.setGenero("Masculino");
				adminUser.setFechaCreacion(LocalDateTime.now());
				adminUser.setCodigo(7060); // Código

				// Buscar el rol "ADMIN"
				Rol adminRol = rolRepository.findByNombre("Admin")
						.orElseThrow(() -> new RuntimeException("Rol Admin no encontrado"));
				//Asociar el rol al usuario
				adminUser.setRol(adminRol);

				usuarioRepository.save(adminUser);
			}
		};
	}
}
