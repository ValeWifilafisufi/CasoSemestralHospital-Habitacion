package HospitalCasoSemestral.Habitacion.controller;

import HospitalCasoSemestral.Habitacion.dto.HabitacionRequestDTO;
import HospitalCasoSemestral.Habitacion.dto.HabitacionResponseDTO;
import HospitalCasoSemestral.Habitacion.model.Habitacion;
import HospitalCasoSemestral.Habitacion.service.HabitacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/habitacion")
@RequiredArgsConstructor

public class HabitacionController {

    private final HabitacionService habitacionService;

    @GetMapping
    public ResponseEntity<List<HabitacionResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(habitacionService.obtenerTodos());
    }

    @GetMapping("/nro_habitacion/{nro_hab}")
    public ResponseEntity<HabitacionResponseDTO> obtenerPorNroHab(@PathVariable Long nro_hab) {
        return habitacionService.obtenerPorNroHabitacion(nro_hab).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/piso/{piso}")
    public ResponseEntity<List<HabitacionResponseDTO>> obtenerPorPiso (@PathVariable Long piso){
        return ResponseEntity.ok(habitacionService.buscarPorPiso(piso));
    }

    @GetMapping("/tipo_cama/{tipo_cama}")
    public ResponseEntity<List<HabitacionResponseDTO>> obtenerPorTipoCama(@PathVariable String tipo_cama){
        return ResponseEntity.ok(habitacionService.buscarPorTipoCama(tipo_cama));
    }

    @GetMapping("/camas/{camas}")
    public ResponseEntity<List<HabitacionResponseDTO>> obtenerPorCamas(@PathVariable Long camas){
        return ResponseEntity.ok((habitacionService.buscarPorCamas(camas)));
    }

    @GetMapping("/precio/{precio}")
    public ResponseEntity<List<HabitacionResponseDTO>> obtenerPorPrecio(@PathVariable BigDecimal precio){
        return ResponseEntity.ok(habitacionService.buscarPorPrecio(precio));
    }

    @GetMapping("/precio_min/{min}/precio_max/{max}")
    public ResponseEntity<List<HabitacionResponseDTO>> obtenerEntrePrecio(@PathVariable BigDecimal min, @PathVariable BigDecimal max ){
        return ResponseEntity.ok(habitacionService.buscarEntrePrecios(min, max));
    }

    @PostMapping
    public ResponseEntity<HabitacionResponseDTO> crear(@Valid @RequestBody HabitacionRequestDTO dto){
        return ResponseEntity.status(201).body(habitacionService.guardar(dto));
    }

    @PutMapping("/{nro_hab}")
    public ResponseEntity<HabitacionResponseDTO> actualizar(@PathVariable Long nro_hab,
                                                            @Valid @RequestBody HabitacionRequestDTO dto){
        return habitacionService.actualizar(nro_hab, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{nro_hab}")
    public ResponseEntity<Void> eliminar (@PathVariable Long nro_hab){
        if (habitacionService.obtenerPorNroHabitacion(nro_hab).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        habitacionService.eliminar(nro_hab);
        return ResponseEntity.noContent().build();
    }
}