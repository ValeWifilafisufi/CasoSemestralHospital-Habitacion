package HospitalCasoSemestral.Habitacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class HabitacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(HabitacionApplication.class, args);
	}

}
