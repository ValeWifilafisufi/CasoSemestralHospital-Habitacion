package HospitalCasoSemestral.Habitacion.repository;

import HospitalCasoSemestral.Habitacion.model.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.util.List;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    List<Habitacion> findByPiso (Long piso);
    List<Habitacion> findByTipoCamaContainingIgnoreCase (String Tipo_cama);
    List<Habitacion> findByValorLessThan(BigDecimal valor);
    List<Habitacion> findByValorBetween (BigDecimal min, BigDecimal max);
}
