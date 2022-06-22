package fore.rtre.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins="*")
public class main {
	public static String BimPort = "http://host.docker.internal:8082";
	public static void main(String[] args) {
		SpringApplication.run(main.class, args);
	}
}
