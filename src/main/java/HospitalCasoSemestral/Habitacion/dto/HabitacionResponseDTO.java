package HospitalCasoSemestral.Habitacion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estructura entregada con los detalles entregados")

public class HabitacionResponseDTO {

    @Schema(description = "Numero de habitacion", example = "101")
    private Long nro_habitacion;

    @Schema(description = "Numero de camas en la habitacion", example = "3")
    private Long nro_camas;

    @Schema(description = "Piso en el cual se encuentra la habitacion", example = "2")
    private Long piso;

    @Schema(description = "El tipo de cama que se encuentra en la habitacion", example = "UCI")
    private String tipo_cama;

    @Schema(description = "Estado en el que se encuentran las camas de la habitacion", example = "Ocupadas")
    private String estado_ocupacion;

    @Schema(description = "Valor del uso de la habitacion", example = "50000")
    private BigDecimal valor;
}
