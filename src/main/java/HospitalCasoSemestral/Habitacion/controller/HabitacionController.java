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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
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

    //---------- OBTENER TODAS LAS HABITACIONES ---------
    @Operation(summary = "Obtener todas las habitaciones", description = "Retorna un catálogo paginado con las habitaciones registradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Catálogo de habitaciones obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<HabitacionResponseDTO>>> obtenerTodos(
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "nroHabitacion") Pageable pageable,
            @Parameter(hidden = true) PagedResourcesAssembler<HabitacionResponseDTO> pagedAssembler) {

        Page<HabitacionResponseDTO> pagina = habitacionService.obtenerTodos(pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pagina, assembler));
    }

    //--------- BUSCAR POR NUMERO DE HABITACION -----------
    @Operation(summary = "Obtener habitación por número", description = "Retorna la habitación que coincida con el número de habitación ingresado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitación encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    @GetMapping("/nro_habitacion/{nroHabitacion}")
    public ResponseEntity<EntityModel<HabitacionResponseDTO>> obtenerPorNroHab(
            @Parameter(description = "Número de habitación", example = "101")
            @PathVariable Long nroHabitacion) {

        HabitacionResponseDTO habitacion = habitacionService.obtenerPorNroHabitacion(nroHabitacion);
        return ResponseEntity.ok(assembler.toModel(habitacion));
    }

    //----- BUSCAR POR PISO -----------
    @Operation(summary = "Obtener habitaciones por piso", description = "Retorna una lista paginada de habitaciones registradas en el piso indicado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones encontradas exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron habitaciones en el piso indicado")
    })
    @GetMapping("/piso/{piso}")
    public ResponseEntity<PagedModel<EntityModel<HabitacionResponseDTO>>> obtenerPorPiso(
            @Parameter(description = "Número de piso", example = "3") @PathVariable Long piso,
            @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable,
            @Parameter(hidden = true) PagedResourcesAssembler<HabitacionResponseDTO> pagedAssembler) {

        Page<HabitacionResponseDTO> pagina = habitacionService.buscarPorPiso(piso, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pagina, assembler));
    }

    //------- BUSCAR POR TIPO DE CAMA  ---------
    @Operation(summary = "Obtener habitación por tipo de cama", description = "Retorna una lista paginada con habitaciones que contengan el tipo de cama")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones encontradas exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron habitaciones con ese tipo de cama")
    })
    @GetMapping("/tipoCama/{tipoCama}")
    public ResponseEntity<PagedModel<EntityModel<HabitacionResponseDTO>>> obtenerPorTipoCama(
            @Parameter(description = "Tipo de cama", example = "UCI")
            @PathVariable("tipoCama") String tipo,
            @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable,
            @Parameter(hidden = true) PagedResourcesAssembler<HabitacionResponseDTO> pagedAssembler) {

        Page<HabitacionResponseDTO> pagina = habitacionService.buscarPorTipoCama(tipo, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pagina, assembler));
    }

    //--------- BUSCAR POR CANTIDAD DE CAMAS ----------
    @Operation(summary = "Obtener las habitaciones que tengan X camas disponibles", description = "Retorna una lista paginada con las habitaciones filtradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones encontradas exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron habitaciones con la cantidad de camas indicada")
    })
    @GetMapping("/camasMenos/{camas}")
    public ResponseEntity<PagedModel<EntityModel<HabitacionResponseDTO>>> obtenerPorCamas(
            @Parameter(description = "Número máximo de camas", example = "3") @PathVariable Long camas,
            @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable,
            @Parameter(hidden = true) PagedResourcesAssembler<HabitacionResponseDTO> pagedAssembler) {

        Page<HabitacionResponseDTO> pagina = habitacionService.buscarPorCamas(camas, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pagina, assembler));
    }

    //-------- BUSCAR POR MENOR PRECIO  ----------
    @Operation(summary = "Obtener las habitaciones que cuesten X o menos", description = "Retorna una lista paginada con las habitaciones de menor o igual precio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones encontradas exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron habitaciones en ese rango de precio")
    })
    @GetMapping("/precioMenos/{precio}")
    public ResponseEntity<PagedModel<EntityModel<HabitacionResponseDTO>>> obtenerPorPrecio(
            @Parameter(description = "Límite de precio para buscar", example = "19900") @PathVariable BigDecimal precio,
            @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable,
            @Parameter(hidden = true) PagedResourcesAssembler<HabitacionResponseDTO> pagedAssembler) {

        Page<HabitacionResponseDTO> pagina = habitacionService.buscarPorPrecio(precio, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pagina, assembler));
    }

    //-------------- BUSCAR ENTRE PRECIOS -----
    @Operation(summary = "Obtener habitaciones entre dos precios", description = "Retorna una lista cuyo valor se encuentre en el rango indicado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones encontradas exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron habitaciones en el rango de precios indicado")
    })
    @GetMapping("/precioMin/{min}/precioMax/{max}")
    public ResponseEntity<PagedModel<EntityModel<HabitacionResponseDTO>>> obtenerEntrePrecios(
            @Parameter(description = "Precio mínimo", example = "150000") @PathVariable BigDecimal min,
            @Parameter(description = "Precio máximo", example = "300000") @PathVariable BigDecimal max,
            @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable,
            @Parameter(hidden = true) PagedResourcesAssembler<HabitacionResponseDTO> pagedAssembler) {

        Page<HabitacionResponseDTO> pagina = habitacionService.buscarEntrePrecios(min, max, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(pagina, assembler));
    }

    //--------- CREAR HABITACION CON HATEOAS ---------
    @Operation(summary = "Crear habitación", description = "Registra una nueva habitación y retorna sus enlaces")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Habitación creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el cuerpo de la petición")
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
            @ApiResponse(responseCode = "200", description = "Habitación actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el cuerpo de la petición"),
            @ApiResponse(responseCode = "404", description = "El número de habitación no existe")
    })
    @PutMapping("/{nroHabitacion}")
    public ResponseEntity<EntityModel<HabitacionResponseDTO>> actualizar(
            @Parameter(description = "Número de habitación", example = "101") @PathVariable Long nroHabitacion,
            @Valid @RequestBody HabitacionRequestDTO dto) {

        HabitacionResponseDTO actualizada = habitacionService.actualizar(nroHabitacion, dto);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    //------------ ELIMINAR HABITACION -------
    @Operation(summary = "Eliminar habitación", description = "Elimina una habitación existente de la base de datos")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Habitación eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "El número de habitación no existe")
    })
    @DeleteMapping("/{nroHabitacion}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Número de habitación", example = "101")
            @PathVariable Long nroHabitacion) {

        habitacionService.eliminar(nroHabitacion);
        return ResponseEntity.noContent().build();
    }
}