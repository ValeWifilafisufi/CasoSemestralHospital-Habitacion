package HospitalCasoSemestral.Habitacion.confi;

import HospitalCasoSemestral.Habitacion.model.Habitacion;
import HospitalCasoSemestral.Habitacion.repository.HabitacionRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInicializer implements CommandLineRunner {

    private final HabitacionRepository habitacionRepository;

    @Override
    public void run(String... args) {
        if (habitacionRepository.count() > 0) {
            log.info(">>> DataInitializer: la BD ya tiene datos, se omite la carga inicial.");
            return;
        }
        log.info(">>>DataInitializer: BD vacia detextada, insertando datos de prueba...");
        habitacionRepository.save(new Habitacion(123L, 2L, 4L, "UCI", "Desocupadas", new BigDecimal("3993.3")));
        log.info(">>>DataInitializer: {} habitaciones insertadas correctamente.",
                habitacionRepository.count());
    }
    }

}
