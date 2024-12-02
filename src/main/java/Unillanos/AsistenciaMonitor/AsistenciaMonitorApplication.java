package Unillanos.AsistenciaMonitor;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

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

	public static void main(String[]args) {
		SpringApplication.run(AsistenciaMonitorApplication.class, args);
	}
}
