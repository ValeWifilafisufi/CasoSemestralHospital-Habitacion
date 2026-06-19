package HospitalCasoSemestral.Habitacion;

import HospitalCasoSemestral.Habitacion.model.Habitacion;
import HospitalCasoSemestral.Habitacion.model.Role;
import HospitalCasoSemestral.Habitacion.model.User;
import HospitalCasoSemestral.Habitacion.repository.HabitacionRepository;
import HospitalCasoSemestral.Habitacion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    private final HabitacionRepository habitacionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.findByUsername("valentina").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("valentina");
            adminUser.setPassword(passwordEncoder.encode("123"));
            adminUser.setRole(Role.ADMIN);
            userRepository.save(adminUser);
            log.info("Usuario administrador creado automáticamente (username: valentina).");
        }

        if (userRepository.findByUsername("Maty").isEmpty()) {
            User adminUser2 = new User();
            adminUser2.setUsername("Maty");
            adminUser2.setPassword(passwordEncoder.encode("1234"));
            adminUser2.setRole(Role.ADMIN);
            userRepository.save(adminUser2);
            log.info("Usuario administrador creado automáticamente (username: Maty).");
        }

        if (habitacionRepository.count() > 0) {
            System.out.println(">>> DataFaker: La base de datos ya tiene habitaciones. Carga omitida.");
            return;
        }

        Faker faker = new Faker();
        System.out.println(">>> DataFaker: Generando 20 habitaciones aleatorias...");

        for (int i = 0; i < 50; i++) {
            Habitacion habitacion = new Habitacion();
            habitacion.setNroHabitacion(201L + i);
            habitacion.setNroCamas((long) faker.number().numberBetween(1, 5));
            habitacion.setPiso((long) faker.number().numberBetween(1, 7));
            habitacion.setTipoCama(faker.options().option("UCI", "UTI", "Normal", "Aislamiento", "Pediatría"));
            habitacion.setEstadoOcupacion(faker.options().option("Ocupadas", "Desocupadas"));
            double precioAleatorio = faker.number().randomDouble(0, 30000, 500000);
            habitacion.setValor(BigDecimal.valueOf(precioAleatorio));
            habitacionRepository.save(habitacion);
        }
        System.out.println(">>> DataFaker: ¡50 habitaciones insertadas exitosamente!");
    }
}
