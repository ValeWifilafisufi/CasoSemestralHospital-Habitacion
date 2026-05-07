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
    @Column(name = "numero_habitaciones")
    private Long nroHabitacion;

    @Column(name= "numero_camas", nullable = false)
    private Long nroCamas;

    @Column(nullable = false)
    private Long piso;

    @Column(nullable = false)
    private String tipoCama;

    @Column(nullable = false)
    private String estadoOcupacion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

}
