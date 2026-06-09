package HospitalCasoSemestral.Habitacion.controller;

import HospitalCasoSemestral.Habitacion.assemblers.HabitacionModelAssembler;
import HospitalCasoSemestral.Habitacion.dto.HabitacionRequestDTO;
import HospitalCasoSemestral.Habitacion.dto.HabitacionResponseDTO;
import HospitalCasoSemestral.Habitacion.service.HabitacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/habitacion")
@RequiredArgsConstructor
@Tag(name = "Gestion de Habitaciones", description = "Endpoints para dministrar el sistema de Habitaciones del hospital")
public class HabitacionController {

    private final HabitacionService habitacionService;
    private final HabitacionModelAssembler assembler;

    //----------OBTENER TODAS LAS HABITACIONES---
    @Operation(summary = "Obtener todas las habitaciones", description = "Retorna una lista compuesta con las habitaciones registradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de habitaciones obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<HabitacionResponseDTO>>> obtenerTodos() {
        List<EntityModel<HabitacionResponseDTO>> hab = habitacionService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                CollectionModel.of(
                        hab,
                        linkTo(methodOn(HabitacionController.class)
                                .obtenerTodos())
                                .withSelfRel()
                )
        );
    }

    //---------BUSCAR POR NUMERO DE HABITACION-----------
    @Operation(summary = "Obtener habitacion por numero de habitacion", description = "Retorna la habitacion que coincida con el numero de habitacion ingresado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitacion encontrada"),
            @ApiResponse(responseCode = "404", description = "Habitacion no encontrada"),
            @ApiResponse(responseCode = "400", description = "Numero de habitacion invalido")
    })
    @GetMapping("/nro_habitacion/{nro_hab}")
    public ResponseEntity<EntityModel<HabitacionResponseDTO>> obtenerPorNroHab(
            @Parameter(description = "Numero de habitacion", example = "101")
            @PathVariable Long nro_hab) {
        HabitacionResponseDTO habitacion =
                habitacionService.obtenerPorNroHabitacion(nro_hab);
        return ResponseEntity.ok(
                assembler.toModel(habitacion)
        );
    }

    //-----BUSCAR POR PISO-----------
    @Operation(summary = "Obtener habitaciones por piso", description = "Retorna una lista de habitaciones registradas en el piso indicado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones encontradas"),
            @ApiResponse(responseCode = "404", description = "No existen habitaciones en ese piso"),
            @ApiResponse(responseCode = "400", description = "Piso invalido")
    })
    @GetMapping("/piso/{piso}")
    public ResponseEntity<CollectionModel<EntityModel<HabitacionResponseDTO>>> obtenerPorPiso(
            @Parameter(description = "Número de piso", example = "3")
            @PathVariable Long piso) {
        List<EntityModel<HabitacionResponseDTO>> habitaciones =
                habitacionService.buscarPorPiso(piso)
                        .stream()
                        .map(assembler::toModel)
                        .collect(Collectors.toList());
        return ResponseEntity.ok(
                CollectionModel.of(habitaciones,
                        linkTo(methodOn(HabitacionController.class)
                                .obtenerPorPiso(piso))
                                .withSelfRel()
                )
        );
    }

    //-------BUSCAR POR TIPO DE CAMA---------
    @Operation(summary = "Obtener habitacion por tipo de cama", description = "Retorna una lista con habitaciones que contengan el tipo de cama")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones encontradas"),
            @ApiResponse(responseCode = "404", description = "No existen habitaciones con ese tipo de cama"),
            @ApiResponse(responseCode = "400", description = "Tipo de cama invalido")
    })
    @GetMapping("/tipo_cama/{tipo_cama}")
    public ResponseEntity<CollectionModel<EntityModel<HabitacionResponseDTO>>> obtenerPorTipoCama(
            @Parameter(description = "Tipo de cama", example = "UCI")
            @PathVariable String tipo) {
        List<EntityModel<HabitacionResponseDTO>> habitaciones =
                habitacionService.buscarPorTipoCama(tipo)
                        .stream()
                        .map(assembler::toModel)
                        .collect(Collectors.toList());
        return ResponseEntity.ok(
                CollectionModel.of(habitaciones,
                        linkTo(methodOn(HabitacionController.class)
                                .obtenerPorTipoCama(tipo))
                                .withSelfRel()
                )
        );
    }

    //---------BUSCAR POR CANTIDAD DE CAMAS----------
    @Operation(summary = "Obtener las habitaciones que tengan X camas disponibles", description = "Retornas una lista con las habitaciones que tengan desde el numero o menos de camas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones encontradas"),
            @ApiResponse(responseCode = "404", description = "No existen habitaciones con esa cantidad de camas."),
            @ApiResponse(responseCode = "400", description = "Numero de camas invalido")
    })
    @GetMapping("/camas_menos/{camas}")
    public ResponseEntity<CollectionModel<EntityModel<HabitacionResponseDTO>>> obtenerPorCamas(
            @Parameter(description = "Numero maximo de camas", example = "3")
            @PathVariable Long camas) {
        List<EntityModel<HabitacionResponseDTO>> habitaciones =
                habitacionService.buscarPorCamas(camas)
                        .stream()
                        .map(assembler::toModel)
                        .collect(Collectors.toList());
        return ResponseEntity.ok(
                CollectionModel.of(habitaciones,
                        linkTo(methodOn(HabitacionController.class)
                                .obtenerPorCamas(camas))
                                .withSelfRel()
                )
        );
    }

    //--------BUSCAR POR MENOR PRECIO----------
    @Operation(summary = "Obtener las habitaciones que cuesten X o menos", description = "Retorna una lista con las habitaciones que tengan desd el precio ingresado o menos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones encontradas"),
            @ApiResponse(responseCode = "404", description = "No existen habitaciones que contengan ese precio"),
            @ApiResponse(responseCode = "400", description = "Rango de precio invalido")
    })
    @GetMapping("/precio_menos/{precio}")
    public ResponseEntity<CollectionModel<EntityModel<HabitacionResponseDTO>>> obtenerPorPrecio(
            @Parameter(description = "Limite de precio pata buscar", example = "19900")
            @PathVariable BigDecimal precio) {
        List<EntityModel<HabitacionResponseDTO>> habitaciones =
                habitacionService.buscarPorPrecio(precio)
                        .stream()
                        .map(assembler::toModel)
                        .collect(Collectors.toList());
        return ResponseEntity.ok(
                CollectionModel.of(habitaciones,
                        linkTo(methodOn(HabitacionController.class)
                                .obtenerPorPrecio(precio))
                                .withSelfRel()
                )
        );
    }

    //--------------BUSCAR ENTRE PRECIOS-----
    @Operation(summary = "obtener habitaciones entre dos precios", description = "Retorna una lista con las habitaciones cuyo valor se encuentre entre el precio mínimo y máximo indicado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones encontradas"),
            @ApiResponse(responseCode = "404", description = "No existen habitaciones entre esos precios"),
            @ApiResponse(responseCode = "400", description = "Rangos de precios invalidos")
    })
    @GetMapping("/precio_min/{min}/precio_max/{max}")
    public ResponseEntity<CollectionModel<EntityModel<HabitacionResponseDTO>>> obtenerEntrePrecios(
            @Parameter(description = "Precio mínimo", example = "150000")
            @PathVariable BigDecimal min,
            @Parameter(description = "Precio máximo", example = "300000")
            @PathVariable BigDecimal max) {
        List<EntityModel<HabitacionResponseDTO>> habitaciones =
                habitacionService.buscarEntrePrecios(min, max)
                        .stream()
                        .map(assembler::toModel)
                        .collect(Collectors.toList());
        return ResponseEntity.ok(
                CollectionModel.of(
                        habitaciones,
                        linkTo(methodOn(HabitacionController.class)
                                .obtenerEntrePrecios(min, max))
                                .withSelfRel()
                )
        );
    }

    //---------CREAR HABITACION---------
    @Operation(summary = "Crear habitación", description = "Registra una nueva habitación")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Habitación creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "La habitación ya existe")
    })
    @PostMapping
    public ResponseEntity<HabitacionResponseDTO> crear(
            @Valid @RequestBody HabitacionRequestDTO dto) {

        return ResponseEntity.status(201)
                .body(habitacionService.guardar(dto));
    }

    //-------Actualizar habitacion
    @Operation(summary = "Actualizar habitación", description = "Actualiza los datos de una habitación existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitación actualizada"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{nro_hab}")
    public ResponseEntity<HabitacionResponseDTO> actualizar(
            @Parameter(description = "Número de habitación", example = "101")
            @PathVariable Long nro_hab,
            @Valid @RequestBody HabitacionRequestDTO dto) {
        return ResponseEntity.ok(
                habitacionService.actualizar(nro_hab, dto));
    }

    //------------ELIMINAR HABITACION
    @Operation(summary = "Eliminar habitación", description = "Elimina una habitación existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Habitación eliminada"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    @DeleteMapping("/{nro_hab}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Número de habitación", example = "101")
            @PathVariable Long nro_hab) {
        habitacionService.eliminar(nro_hab);
        return ResponseEntity.noContent().build();
    }
}