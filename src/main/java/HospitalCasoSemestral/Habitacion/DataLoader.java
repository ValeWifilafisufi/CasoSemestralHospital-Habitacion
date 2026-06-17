package HospitalCasoSemestral.Habitacion;

import HospitalCasoSemestral.Habitacion.model.Habitacion;
import HospitalCasoSemestral.Habitacion.repository.HabitacionRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

// @Profile("dev")  <-- Comentado para que siempre se ejecute
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Override
    public void run(String... args) throws Exception {


        if (habitacionRepository.count() > 0) {
            System.out.println(">>> DataFaker: La base de datos ya tiene habitaciones. Carga omitida.");
            return;
        }

        Faker faker = new Faker();
        System.out.println(">>> DataFaker: Generando 20 habitaciones aleatorias...");

        for (int i = 0; i < 20; i++) {
            Habitacion habitacion = new Habitacion();

            // Generar un número de habitación en secuencia
            habitacion.setNroHabitacion(201L + i);

            // Generar cantidad de camas aleatoria entre 1 y 4
            habitacion.setNroCamas((long) faker.number().numberBetween(1, 5));

            // Generar piso aleatorio entre el piso 1 y el 6
            habitacion.setPiso((long) faker.number().numberBetween(1, 7));

            // Elegir aleatoriamente el tipo de cama desde una lista de opciones
            habitacion.setTipoCama(faker.options().option("UCI", "UTI", "Normal", "Aislamiento", "Pediatría"));

            // Elegir aleatoriamente el estado de ocupación
            habitacion.setEstadoOcupacion(faker.options().option("Ocupadas", "Desocupadas"));

            // Generar un valor (precio) aleatorio con 2 decimales, entre 30.000 y 500.000
            double precioAleatorio = faker.number().randomDouble(0, 30000, 500000);
            habitacion.setValor(BigDecimal.valueOf(precioAleatorio));

            // Guardar en la base de datos MySQL
            habitacionRepository.save(habitacion);
        }

        System.out.println(">>> DataFaker: ¡20 habitaciones insertadas exitosamente!");
    }
}
