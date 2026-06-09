package HospitalCasoSemestral.Habitacion.service;

import HospitalCasoSemestral.Habitacion.dto.HabitacionRequestDTO;
import HospitalCasoSemestral.Habitacion.dto.HabitacionResponseDTO;
import HospitalCasoSemestral.Habitacion.model.Habitacion;
import HospitalCasoSemestral.Habitacion.repository.HabitacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import HospitalCasoSemestral.Habitacion.exception.RecursoDuplicadoException;
import HospitalCasoSemestral.Habitacion.exception.RecursoNoEncontradoException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

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

    public List<HabitacionResponseDTO> obtenerTodos() {
        return habitacionRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public HabitacionResponseDTO obtenerPorNroHabitacion(Long nro_hab) {
        Habitacion habitacion = habitacionRepository.findById(nro_hab)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("No existe una habitación con número " + nro_hab));
        return mapToDTO(habitacion);
    }

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

    public HabitacionResponseDTO actualizar(Long nrohab,
                                            HabitacionRequestDTO dto) {
        Habitacion existente = habitacionRepository.findById(nrohab)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("No existe una habitación con número " + nrohab));
        existente.setNroCamas(dto.getNroCamas());
        existente.setPiso(dto.getPiso());
        existente.setTipoCama(dto.getTipo_cama());
        existente.setEstadoOcupacion(dto.getEstado_ocupacion());
        existente.setValor(dto.getValor());
        return mapToDTO(habitacionRepository.save(existente));
    }

    public void eliminar(Long nro_hab) {
        if (!habitacionRepository.existsById(nro_hab)) {
            throw new RecursoNoEncontradoException("No existe una habitación con número " + nro_hab);
        }
        habitacionRepository.deleteById(nro_hab);
    }

    public List<HabitacionResponseDTO> buscarPorPiso(Long piso) {
        List<HabitacionResponseDTO> habitaciones =
                habitacionRepository.findByPiso(piso)
                        .stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        if (habitaciones.isEmpty()) {
            throw new RecursoNoEncontradoException("No existen habitaciones registradas en el piso " + piso);
        }
        return habitaciones;
    }

    public List<HabitacionResponseDTO> buscarPorTipoCama(String tipo) {
        List<HabitacionResponseDTO> habitaciones =
                habitacionRepository.findByTipoCamaContainingIgnoreCase(tipo)
                        .stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        if (habitaciones.isEmpty()) {
            throw new RecursoNoEncontradoException("No existen habitaciones con tipo de cama " + tipo);
        }
        return habitaciones;
    }

    public List<HabitacionResponseDTO> buscarPorCamas(Long camas) {
        List<HabitacionResponseDTO> habitaciones =
                habitacionRepository.findByNroCamasLessThan(camas)
                        .stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        if (habitaciones.isEmpty()) {
            throw new RecursoNoEncontradoException("No existen habitaciones con menos de " + camas + " camas");
        }
        return habitaciones;
    }

    public List<HabitacionResponseDTO> buscarPorPrecio(BigDecimal precio) {
        List<HabitacionResponseDTO> habitaciones =
                habitacionRepository.findByValorLessThan(precio)
                        .stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        if (habitaciones.isEmpty()) {
            throw new RecursoNoEncontradoException("No existen habitaciones con valor menor a " + precio);
        }
        return habitaciones;
    }

    public List<HabitacionResponseDTO> buscarEntrePrecios(BigDecimal min,BigDecimal max){
        if(min.compareTo(max) > 0){
            throw new IllegalArgumentException(
                "El precio mínimo no puede ser mayor que el precio máximo");
        }
        List<HabitacionResponseDTO> habitaciones =
                habitacionRepository.findByValorBetween(min, max)
                        .stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        if(habitaciones.isEmpty()){
            throw new RecursoNoEncontradoException(
                    "No existen habitaciones entre $" + min + " y $" + max);
        }
        return habitaciones;
    }
}