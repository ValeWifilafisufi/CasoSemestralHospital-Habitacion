package HospitalCasoSemestral.Habitacion.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionRequestDTO {

    @NotNull(message = "El numero de habitacion no puede ser nulo")
    @Positive(message = "El numero de habitacion debe ser mayor a 0")
    private Long nro_habitacion;

    @NotNull(message = "El numero de camas no puede ser nulo")
    @PositiveOrZero(message = "El numero de camas debe ser igual o mayor a 0")
    private Long nroCamas;

    @NotNull(message = "El piso no puede ser nulo")
    @PositiveOrZero(message = "El piso debe ser igual o mayor a 0")
    private Long piso;

    @NotBlank(message = "El tipo de cama no puede estar vacio")
    private String tipo_cama;

    @NotBlank(message = "El estado de ocupacion no puede estar vacio")
    private String estado_ocupacion;

    @NotNull(message = "el valor de la habitacion no puede ser nulo")
    @Positive(message = "El valor de la habitacion debe ser positivo")
    private BigDecimal valor;

}
