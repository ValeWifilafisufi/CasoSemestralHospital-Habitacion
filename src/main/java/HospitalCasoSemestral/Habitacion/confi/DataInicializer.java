package HospitalCasoSemestral.Habitacion.confi;

import HospitalCasoSemestral.Habitacion.repository.HabitacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInicializer {

    private final HabitacionRepository habitacionRepository;

    @Override
    public void run(String... args) {
        if (habitacionRepository.count() > 0) {
            log.info(">>> DataInitializer: la BD ya tiene datos, se omite la carga inicial.");
            return;
        }
        log.info(">>>DataInitializer: BD vacia detextada, insertando datos de prueba...");

        log.info(">>>DataInitializer: {} productos insertados correctamente.",
                productoRepository.count());
    }
}
