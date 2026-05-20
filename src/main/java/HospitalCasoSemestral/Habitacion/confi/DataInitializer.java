package HospitalCasoSemestral.Habitacion.confi;

import HospitalCasoSemestral.Habitacion.model.Habitacion;
import HospitalCasoSemestral.Habitacion.repository.HabitacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final HabitacionRepository habitacionRepository;

    @Override
    public void run(String... args) {
        if (habitacionRepository.count() > 0) {
            log.info(">>> DataInitializer: la BD ya tiene datos, se omite la carga inicial.");
            return;
        }
        log.info(">>>DataInitializer: BD vacia detextada, insertando datos de prueba...");
        habitacionRepository.save(new Habitacion(123L, 2L, 1L, "UCI", "Desocupadas", new BigDecimal("399300.3")));
        habitacionRepository.save(new Habitacion(323L, 2L, 3L, "Normal", "Ocupadas", new BigDecimal("11231.3")));
        habitacionRepository.save(new Habitacion(298L, 1L, 2L, "UTI", "Desocupadas", new BigDecimal("997554.98")));
        habitacionRepository.save(new Habitacion(175L, 4L, 1L, "UCI", "Ocupadas", new BigDecimal("864343.53")));


        log.info(">>>DataInitializer: {} habitaciones insertadas correctamente.",
                habitacionRepository.count());
    }
}
