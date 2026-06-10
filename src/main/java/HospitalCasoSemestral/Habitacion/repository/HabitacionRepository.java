package HospitalCasoSemestral.Habitacion.repository;

import HospitalCasoSemestral.Habitacion.model.Habitacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    Page<Habitacion> findByPiso(Long piso, Pageable pageable);
    Page<Habitacion> findByTipoCamaContainingIgnoreCase(String tipoCama, Pageable pageable);
    Page<Habitacion> findByNroCamasLessThan(Long nrocamas, Pageable pageable);
    Page<Habitacion> findByValorLessThan(BigDecimal valor, Pageable pageable);
    Page<Habitacion> findByValorBetween(BigDecimal min, BigDecimal max, Pageable pageable);
}
