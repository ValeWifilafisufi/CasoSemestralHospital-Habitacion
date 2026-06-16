package HospitalCasoSemestral.Habitacion.service;

import HospitalCasoSemestral.Habitacion.dto.HabitacionRequestDTO;
import HospitalCasoSemestral.Habitacion.dto.HabitacionResponseDTO;
import HospitalCasoSemestral.Habitacion.model.Habitacion;
import HospitalCasoSemestral.Habitacion.repository.HabitacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import HospitalCasoSemestral.Habitacion.exception.RecursoDuplicadoException;
import HospitalCasoSemestral.Habitacion.exception.RecursoNoEncontradoException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HabitacionService {
    private final HabitacionRepository habitacionRepository;

    public HabitacionResponseDTO mapToDTO(Habitacion habitacion) {
        return new HabitacionResponseDTO(
                habitacion.getNroHabitacion(),
                habitacion.getNroCamas(),
                habitacion.getPiso(),
                habitacion.getTipoCama(),
                habitacion.getEstadoOcupacion(),
                habitacion.getValor()
        );
    }

    public Page<HabitacionResponseDTO> obtenerTodos(Pageable pageable) {
        return habitacionRepository.findAll(pageable).map(this::mapToDTO);
    }

    public HabitacionResponseDTO obtenerPorNroHabitacion(Long nroHabitacion) {
        Habitacion habitacion = habitacionRepository.findById(nroHabitacion)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una habitación con número " + nroHabitacion));
        return mapToDTO(habitacion);
    }

    @Transactional
    public HabitacionResponseDTO guardar(HabitacionRequestDTO dto) {
        if (habitacionRepository.existsById(dto.getNroHabitacion())) {
            throw new RecursoDuplicadoException("La habitación " + dto.getNroHabitacion() + " ya existe");
        }
        Habitacion hab = new Habitacion(
                dto.getNroHabitacion(),
                dto.getNroCamas(),
                dto.getPiso(),
                dto.getTipoCama(),
                dto.getEstadoOcupacion(),
                dto.getValor()
        );
        return mapToDTO(habitacionRepository.save(hab));
    }

    @Transactional
    public HabitacionResponseDTO actualizar(Long nroHabitacion, HabitacionRequestDTO dto) {
        Habitacion existente = habitacionRepository.findById(nroHabitacion)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una habitación con número " + nroHabitacion));
        existente.setNroCamas(dto.getNroCamas());
        existente.setPiso(dto.getPiso());
        existente.setTipoCama(dto.getTipoCama());
        existente.setEstadoOcupacion(dto.getEstadoOcupacion());
        existente.setValor(dto.getValor());
        return mapToDTO(habitacionRepository.save(existente));
    }

    @Transactional
    public void eliminar(Long nroHabitacion) {
        if (!habitacionRepository.existsById(nroHabitacion)) {
            throw new RecursoNoEncontradoException("No existe una habitación con número " + nroHabitacion);
        }
        habitacionRepository.deleteById(nroHabitacion);
    }

    public Page<HabitacionResponseDTO> buscarPorPiso(Long piso, Pageable pageable) {
        Page<HabitacionResponseDTO> pagina = habitacionRepository.findByPiso(piso, pageable).map(this::mapToDTO);
        if (pagina.isEmpty()) {
            throw new RecursoNoEncontradoException("No existen habitaciones registradas en el piso " + piso);
        }
        return pagina;
    }

    public Page<HabitacionResponseDTO> buscarPorTipoCama(String tipo, Pageable pageable) {
        Page<HabitacionResponseDTO> pagina = habitacionRepository.findByTipoCamaContainingIgnoreCase(tipo, pageable).map(this::mapToDTO);
        if (pagina.isEmpty()) {
            throw new RecursoNoEncontradoException("No existen habitaciones con tipo de cama " + tipo);
        }
        return pagina;
    }

    public Page<HabitacionResponseDTO> buscarPorCamas(Long camas, Pageable pageable) {
        Page<HabitacionResponseDTO> pagina = habitacionRepository.findByNroCamasLessThan(camas, pageable).map(this::mapToDTO);
        if (pagina.isEmpty()) {
            throw new RecursoNoEncontradoException("No existen habitaciones con menos de " + camas + " camas");
        }
        return pagina;
    }

    public Page<HabitacionResponseDTO> buscarPorPrecio(BigDecimal precio, Pageable pageable) {
        Page<HabitacionResponseDTO> pagina = habitacionRepository.findByValorLessThan(precio, pageable).map(this::mapToDTO);
        if (pagina.isEmpty()) {
            throw new RecursoNoEncontradoException("No existen habitaciones con valor menor a " + precio);
        }
        return pagina;
    }

    public Page<HabitacionResponseDTO> buscarEntrePrecios(BigDecimal min, BigDecimal max, Pageable pageable) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor que el precio máximo");
        }
        Page<HabitacionResponseDTO> pagina = habitacionRepository.findByValorBetween(min, max, pageable).map(this::mapToDTO);
        if (pagina.isEmpty()) {
            throw new RecursoNoEncontradoException("No existen habitaciones entre $" + min + " y $" + max);
        }
        return pagina;
    }
}