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
public class HabitacionService {7
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

    public HabitacionResponseDTO obtenerPorNroHabitacion(Long nro_hab) {
        Habitacion habitacion = habitacionRepository.findById(nro_hab)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una habitación con número " + nro_hab));
        return mapToDTO(habitacion);
    }

    @Transactional
    public HabitacionResponseDTO guardar(HabitacionRequestDTO dto) {
        if (habitacionRepository.existsById(dto.getNro_habitacion())) {
            throw new RecursoDuplicadoException("La habitación " + dto.getNro_habitacion() + " ya existe");
        }
        Habitacion hab = new Habitacion(
                dto.getNro_habitacion(),
                dto.getNroCamas(),
                dto.getPiso(),
                dto.getTipo_cama(),
                dto.getEstado_ocupacion(),
                dto.getValor()
        );
        return mapToDTO(habitacionRepository.save(hab));
    }

    @Transactional
    public HabitacionResponseDTO actualizar(Long nrohab, HabitacionRequestDTO dto) {
        Habitacion existente = habitacionRepository.findById(nrohab)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una habitación con número " + nrohab));
        existente.setNroCamas(dto.getNroCamas());
        existente.setPiso(dto.getPiso());
        existente.setTipoCama(dto.getTipo_cama());
        existente.setEstadoOcupacion(dto.getEstado_ocupacion());
        existente.setValor(dto.getValor());
        return mapToDTO(habitacionRepository.save(existente));
    }

    @Transactional
    public void eliminar(Long nro_hab) {
        if (!habitacionRepository.existsById(nro_hab)) {
            throw new RecursoNoEncontradoException("No existe una habitación con número " + nro_hab);
        }
        habitacionRepository.deleteById(nro_hab);
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