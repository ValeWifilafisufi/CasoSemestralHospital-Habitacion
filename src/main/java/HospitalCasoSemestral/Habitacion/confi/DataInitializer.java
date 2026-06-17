package HospitalCasoSemestral.Habitacion.confi;

import HospitalCasoSemestral.Habitacion.model.Habitacion;
import HospitalCasoSemestral.Habitacion.model.Role;
import HospitalCasoSemestral.Habitacion.model.User; // Asegúrate de importar tu clase User
import HospitalCasoSemestral.Habitacion.repository.HabitacionRepository;
import HospitalCasoSemestral.Habitacion.repository.UserRepository; // Importa tu repositorio
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final HabitacionRepository habitacionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.findByUsername("valentina").isEmpty()) {
            log.info(">>> DataInitializer: Creando usuario 'valentina' para validar JWT...");
            User adminUser = User.builder()
                    .username("valentina")
                    .password(passwordEncoder.encode("123")) // Usa la misma clave que le pusiste en el Gateway
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(adminUser);
        } else {
            log.info(">>> DataInitializer: El usuario 'valentina' ya existe.");
        }

        // --- 2. CARGA DE LAS HABITACIONES ---
        if (habitacionRepository.count() > 0) {
            log.info(">>> DataInitializer: la BD ya tiene habitaciones, se omite la carga inicial.");
            return;
        }
        log.info(">>>DataInitializer: BD vacia detectada, insertando datos de prueba...");
        habitacionRepository.save(new Habitacion(123L, 2L, 1L, "UCI", "Desocupadas", new BigDecimal("399300.3")));
        habitacionRepository.save(new Habitacion(323L, 2L, 3L, "Normal", "Ocupadas", new BigDecimal("11231.3")));
        habitacionRepository.save(new Habitacion(298L, 1L, 2L, "UTI", "Desocupadas", new BigDecimal("997554.98")));
        habitacionRepository.save(new Habitacion(175L, 4L, 1L, "UCI", "Ocupadas", new BigDecimal("864343.53")));

        log.info(">>>DataInitializer: {} habitaciones insertadas correctamente.", habitacionRepository.count());
    }
}
