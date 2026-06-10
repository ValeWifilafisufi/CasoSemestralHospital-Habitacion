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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/habitacion")
@RequiredArgsConstructor
@Tag(name = "Gestion de Habitaciones", description = "Endpoints para dministrar el sistema de Habitaciones del hospital")
public class HabitacionController {
    private final HabitacionService habitacionService;
    private final HabitacionModelAssembler assembler;

    //---------- OBTENER TODAS LAS HABITACIONES ---
    @Operation(summary = "Obtener todas las habitaciones", description = "Retorna un catálogo con las habitaciones registradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Catálogo de habitaciones obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<HabitacionResponseDTO>>> obtenerTodos(
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
            PagedResourcesAssembler<HabitacionResponseDTO> pagedAssembler) {
        Page<HabitacionResponseDTO> pagina = habitacionService.obtenerTodos(pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pagina, assembler));
    }

    //--------- BUSCAR POR NUMERO DE HABITACION -----------
    @Operation(summary = "Obtener habitacion por numero de habitacion", description = "Retorna la habitacion que coincida con el numero de habitacion ingresado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitacion encontrada"),
            @ApiResponse(responseCode = "404", description = "Habitacion no encontrada")
    })
    @GetMapping("/nro_habitacion/{nro_hab}")
    public ResponseEntity<EntityModel<HabitacionResponseDTO>> obtenerPorNroHab(
            @Parameter(description = "Numero de habitacion", example = "101")
            @PathVariable Long nro_hab) {

        HabitacionResponseDTO habitacion = habitacionService.obtenerPorNroHabitacion(nro_hab);
        return ResponseEntity.ok(assembler.toModel(habitacion));
    }

    //----- BUSCAR POR PISO -----------
    @Operation(summary = "Obtener habitaciones por piso", description = "Retorna una lista de habitaciones registradas en el piso indicado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones encontradas")
    })
    @GetMapping("/piso/{piso}")
    public ResponseEntity<PagedModel<EntityModel<HabitacionResponseDTO>>> obtenerPorPiso(
            @Parameter(description = "Número de piso", example = "3") @PathVariable Long piso,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            PagedResourcesAssembler<HabitacionResponseDTO> pagedAssembler) {
        Page<HabitacionResponseDTO> pagina = habitacionService.buscarPorPiso(piso, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pagina, assembler));
    }

    //------- BUSCAR POR TIPO DE CAMA  ---------
    @Operation(summary = "Obtener habitacion por tipo de cama", description = "Retorna una lista con habitaciones que contengan el tipo de cama")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones encontradas")
    })
    @GetMapping("/tipo_cama/{tipo_cama}")
    public ResponseEntity<PagedModel<EntityModel<HabitacionResponseDTO>>> obtenerPorTipoCama(
            @Parameter(description = "Tipo de cama", example = "UCI")
            @PathVariable("tipo_cama") String tipo,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            PagedResourcesAssembler<HabitacionResponseDTO> pagedAssembler) {
        Page<HabitacionResponseDTO> pagina = habitacionService.buscarPorTipoCama(tipo, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pagina, assembler));
    }

    //--------- BUSCAR POR CANTIDAD DE CAMAS ----------
    @Operation(summary = "Obtener las habitaciones que tengan X camas disponibles", description = "Retorna una lista con las habitaciones filtradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones encontradas")
    })
    @GetMapping("/camas_menos/{camas}")
    public ResponseEntity<PagedModel<EntityModel<HabitacionResponseDTO>>> obtenerPorCamas(
            @Parameter(description = "Numero maximo de camas", example = "3") @PathVariable Long camas,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            PagedResourcesAssembler<HabitacionResponseDTO> pagedAssembler) {

        Page<HabitacionResponseDTO> pagina = habitacionService.buscarPorCamas(camas, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pagina, assembler));
    }

    //-------- BUSCAR POR MENOR PRECIO  ----------
    @Operation(summary = "Obtener las habitaciones que cuesten X o menos", description = "Retorna una lista con las habitaciones de menor o igual precio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones encontradas")
    })
    @GetMapping("/precio_menos/{precio}")
    public ResponseEntity<PagedModel<EntityModel<HabitacionResponseDTO>>> obtenerPorPrecio(
            @Parameter(description = "Limite de precio para buscar", example = "19900") @PathVariable BigDecimal precio,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            PagedResourcesAssembler<HabitacionResponseDTO> pagedAssembler) {

        Page<HabitacionResponseDTO> pagina = habitacionService.buscarPorPrecio(precio, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pagina, assembler));
    }

    //-------------- BUSCAR ENTRE PRECIOS -----
    @Operation(summary = "obtener habitaciones entre dos precios", description = "Retorna una lista cuyo valor se encuentre en el rango indicado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones encontradas")
    })
    @GetMapping("/precio_min/{min}/precio_max/{max}")
    public ResponseEntity<PagedModel<EntityModel<HabitacionResponseDTO>>> obtenerEntrePrecios(
            @Parameter(description = "Precio mínimo", example = "150000") @PathVariable BigDecimal min,
            @Parameter(description = "Precio máximo", example = "300000") @PathVariable BigDecimal max,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            PagedResourcesAssembler<HabitacionResponseDTO> pagedAssembler) {

        Page<HabitacionResponseDTO> pagina = habitacionService.buscarEntrePrecios(min, max, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pagina, assembler));
    }

    //--------- CREAR HABITACION CON HATEOAS ---------
    @Operation(summary = "Crear habitación", description = "Registra una nueva habitación y retorna sus enlaces")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Habitación creada correctamente")
    })
    @PostMapping
    public ResponseEntity<EntityModel<HabitacionResponseDTO>> crear(
            @Valid @RequestBody HabitacionRequestDTO dto) {

        HabitacionResponseDTO creada = habitacionService.guardar(dto);
        return ResponseEntity.status(201).body(assembler.toModel(creada));
    }

    //------- ACTUALIZAR HABITACION CON HATEOAS -------
    @Operation(summary = "Actualizar habitación", description = "Actualiza los datos de una habitación existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitación actualizada correctamente")
    })
    @PutMapping("/{nro_hab}")
    public ResponseEntity<EntityModel<HabitacionResponseDTO>> actualizar(
            @Parameter(description = "Número de habitación", example = "101") @PathVariable Long nro_hab,
            @Valid @RequestBody HabitacionRequestDTO dto) {

        HabitacionResponseDTO actualizada = habitacionService.actualizar(nro_hab, dto);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    //------------ ELIMINAR HABITACION -------
    @Operation(summary = "Eliminar habitación", description = "Elimina una habitación existente de la base de datos")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Habitación eliminada exitosamente"),
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