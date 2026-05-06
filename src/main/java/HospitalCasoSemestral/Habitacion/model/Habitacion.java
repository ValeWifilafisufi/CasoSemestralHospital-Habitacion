package HospitalCasoSemestral.Habitacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table

public class Habitacion {

    @Id
    private Long nro_habitacion;

    @Column(nullable = false)
    private Long nro_camas;

    @Column(nullable = false)
    private Long piso;

    @Column(nullable = false)
    private String tipo_cama;

    @Column(nullable = false)
    private String estado_ocupacion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

}
