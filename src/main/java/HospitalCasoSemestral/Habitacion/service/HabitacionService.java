package HospitalCasoSemestral.Habitacion.service;

import HospitalCasoSemestral.Habitacion.dto.HabitacionRequestDTO;
import HospitalCasoSemestral.Habitacion.dto.HabitacionResponseDTO;
import HospitalCasoSemestral.Habitacion.model.Habitacion;
import HospitalCasoSemestral.Habitacion.repository.HabitacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class HabitacionService {
    private final HabitacionRepository habitacionRepository;

    public HabitacionResponseDTO mapToDTO(Habitacion habitacion) {
        return new HabitacionResponseDTO(
                habitacion.getNro_habitacion(),
                habitacion.getNro_camas(),
                habitacion.getPiso(),
                habitacion.getTipo_cama(),
                habitacion.getEstado_ocupacion(),
                habitacion.getValor()
        );
    }

    public List<HabitacionResponseDTO> obtenerTodos(){
        return habitacionRepository.findAll()
                .stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<HabitacionResponseDTO> obtenerPorNroHabitacion(Long nro_hab){
        return habitacionRepository.findById(nro_hab).map(this::mapToDTO);
    }

    public HabitacionResponseDTO guardar(HabitacionRequestDTO dto){
        Habitacion hab = new Habitacion(
                dto.getNro_habitacion(),
                dto.getNro_camas(),
                dto.getPiso(),
                dto.getTipo_cama(),
                dto.getEstado_ocupacion(),
                dto.getValor()
        );
        return mapToDTO(habitacionRepository.save(hab));
    }

    public Optional<HabitacionResponseDTO> actualizar(Long nro_hab, HabitacionRequestDTO dto){
        return habitacionRepository.findById(nro_hab).map(existente ->{
            existente.setNro_habitacion(dto.getNro_habitacion());
            existente.setNro_camas(dto.getNro_camas());
            existente.setPiso(dto.getPiso());
            existente.setTipo_cama(dto.getTipo_cama());
            existente.setEstado_ocupacion(dto.getEstado_ocupacion());
            existente.setValor(dto.getValor());
            return mapToDTO(habitacionRepository.save(existente));
        });
    }

    public void eliminar(Long nro_hab){
        habitacionRepository.deleteById(nro_hab);
    }

    public List<HabitacionResponseDTO> buscarPorPiso(Long piso){
        return habitacionRepository.findByPiso(piso).stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public List<HabitacionResponseDTO> buscarPorTipoCama(String tipo){
        return habitacionRepository.findByTipoCamaContainingIgnoreCase(tipo).stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public List<HabitacionResponseDTO> buscarPorCamas(Long camas){
        return habitacionRepository.findByNroCamasLessThan(camas).stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public List<HabitacionResponseDTO> buscarPorPrecio(BigDecimal precio){
        return habitacionRepository.findByValorLessThan(precio).stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public List<HabitacionResponseDTO> buscarEntrePrecios(BigDecimal min,BigDecimal max){
        return habitacionRepository.findByValorBetween(min, max).stream().map(this::mapToDTO).collect(Collectors.toList());
    }
}