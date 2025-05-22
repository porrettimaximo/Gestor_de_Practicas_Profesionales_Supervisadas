package ing.gpps;

import ing.gpps.entity.users.Estudiante;
import ing.gpps.repository.*;
import ing.gpps.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	ProyectoRepository proyectoRepository;

	@Autowired
	EntregaRepository entregaRepository;

	@Autowired
	EntidadRepository entidadRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}


	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("Los beans de la aplicación son:" + ctx.getBeanDefinitionCount());
			System.out.println("La aplicación ha iniciado correctamente."+usuarioRepository.count());
			SetupDataBase setupDataBase = new SetupDataBase(usuarioRepository, usuarioService, proyectoRepository, entregaRepository, entidadRepository);
			System.out.println("-------------------");

		};
	}

	public static int devolverNumero(int numero){
		return numero;
	}


}
