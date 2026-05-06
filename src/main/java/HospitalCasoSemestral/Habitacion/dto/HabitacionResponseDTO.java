package HospitalCasoSemestral.Habitacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class HabitacionResponseDTO {

    private Long nro_habitacion;
    private Long nro_camas;
    private Long piso;
    private String tipo_cama;
    private String estado_ocupacion;
    private BigDecimal valor;
}
