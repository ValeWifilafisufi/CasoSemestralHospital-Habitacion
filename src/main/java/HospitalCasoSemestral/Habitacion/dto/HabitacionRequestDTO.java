package HospitalCasoSemestral.Habitacion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estructura requerida para generar una nueva habitacion")
public class HabitacionRequestDTO {

    @Schema(description = "Numero de habitacion", example = "101")
    @NotNull(message = "El numero de habitacion no puede ser nulo")
    @Positive(message = "El numero de habitacion debe ser mayor a 0")
    private Long nro_habitacion;

    @Schema(description = "Numero de camas en la habitacion", example = "3")
    @NotNull(message = "El numero de camas no puede ser nulo")
    @PositiveOrZero(message = "El numero de camas debe ser igual o mayor a 0")
    private Long nroCamas;

    @Schema(description = "Piso en el cual se encuentra la habitacion", example = "2")
    @NotNull(message = "El piso no puede ser nulo")
    @PositiveOrZero(message = "El piso debe ser igual o mayor a 0")
    private Long piso;

    @Schema(description = "El tipo de cama que se encuentra en la habitacion", example = "UCI")
    @NotBlank(message = "El tipo de cama no puede estar vacio")
    private String tipo_cama;

    @Schema(description = "Estado en el que se encuentran las camas de la habitacion", example = "Ocupadas")
    @NotBlank(message = "El estado de ocupacion no puede estar vacio")
    @Pattern(
            regexp = "Ocupadas|Desocupadas",
            message = "El estado debe ser Ocupadas o Desocupadas"
    )
    private String estado_ocupacion;

    @Schema(description = "Valor del uso de la habitacion", example = "50000")
    @NotNull(message = "el valor de la habitacion no puede ser nulo")
    @Positive(message = "El valor de la habitacion debe ser positivo")
    private BigDecimal valor;

}
