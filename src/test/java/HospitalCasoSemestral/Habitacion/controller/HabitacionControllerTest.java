package HospitalCasoSemestral.Habitacion.controller;

import HospitalCasoSemestral.Habitacion.assemblers.HabitacionModelAssembler;
import HospitalCasoSemestral.Habitacion.dto.HabitacionRequestDTO;
import HospitalCasoSemestral.Habitacion.dto.HabitacionResponseDTO;
import HospitalCasoSemestral.Habitacion.service.HabitacionService;
import com.fasterxml.jackson.databind.ObjectMapper; // Import correcto
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(HabitacionController.class)
@ActiveProfiles("test")
@Import(HabitacionModelAssembler.class)
@WithMockUser
@DisplayName("Tests unitarios - HabitacionController")

public class HabitacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private HabitacionService habitacionService;

    @Test
    @DisplayName("GIVEN: Existen habitaciones WHEN: GET /api/habitacion THEN: Retorna 200 OK y la lista paginada")
    void shouldReturnTodasLasHabitaciones() throws Exception {
        HabitacionResponseDTO hab1 = new HabitacionResponseDTO(115L, 2L, 2L, "Pediatria", "Ocupadas", new BigDecimal(15000));
        HabitacionResponseDTO hab2 = new HabitacionResponseDTO(208L, 1L, 1L, "Aislamiento", "Desocupadas", new BigDecimal(59000));

        List<HabitacionResponseDTO> listaFalsa = Arrays.asList(hab1, hab2);
        Page<HabitacionResponseDTO> paginaFalsa = new PageImpl<>(listaFalsa);
        when(habitacionService.obtenerTodos(any(Pageable.class))).thenReturn(paginaFalsa);
        mockMvc.perform(get("/api/habitacion")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GIVEN: ID válido WHEN: GET /api/habitacion/nro_habitacion/{nro_hab} THEN: Retorna 200 OK")
    void shouldReturnHabitacionById() throws Exception {
        HabitacionResponseDTO habitacionFalsa = new HabitacionResponseDTO();
        when(habitacionService.obtenerPorNroHabitacion(anyLong())).thenReturn(habitacionFalsa);

        mockMvc.perform(get("/api/habitacion/nro_habitacion/202")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GIVEN: DTO válido WHEN: POST /api/habitacion THEN: Retorna 201 Created")
    void shouldCreateHabitacion() throws Exception {
        HabitacionRequestDTO nuevoDTO = new HabitacionRequestDTO();
        nuevoDTO.setNro_habitacion(202L);
        nuevoDTO.setNroCamas(2L);
        nuevoDTO.setPiso(2L);
        nuevoDTO.setTipo_cama("UCI");
        nuevoDTO.setEstado_ocupacion("Desocupadas");
        nuevoDTO.setValor(new BigDecimal("50000"));

        HabitacionResponseDTO respuestaDTO = new HabitacionResponseDTO();
        respuestaDTO.setNro_habitacion(202L);
        respuestaDTO.setTipo_cama("UCI");
        respuestaDTO.setEstado_ocupacion("Desocupadas");

        when(habitacionService.guardar(any(HabitacionRequestDTO.class))).thenReturn(respuestaDTO);

        mockMvc.perform(post("/api/habitacion")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nro_habitacion").value(202))
                .andExpect(jsonPath("$.tipo_cama").value("UCI"));
    }

    @Test
    @DisplayName("GIVEN: ID y DTO válidos WHEN: PUT /api/habitacion/{id} THEN: Retorna 200 OK")
    void shouldUpdateHabitacion() throws Exception {
        Long idHabitacion = 1L;

        HabitacionRequestDTO updateDTO = new HabitacionRequestDTO();
        updateDTO.setNro_habitacion(202L);
        updateDTO.setNroCamas(2L);
        updateDTO.setPiso(2L);
        updateDTO.setTipo_cama("UCI");
        updateDTO.setEstado_ocupacion("Desocupadas");
        updateDTO.setValor(new BigDecimal("50000"));

        HabitacionResponseDTO respuestaDTO = new HabitacionResponseDTO();
        respuestaDTO.setNro_habitacion(202L);

        when(habitacionService.actualizar(eq(idHabitacion), any(HabitacionRequestDTO.class))).thenReturn(respuestaDTO);

        mockMvc.perform(put("/api/habitacion/" + idHabitacion)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nro_habitacion").value(202));
    }

    @Test
    @DisplayName("GIVEN: ID válido WHEN: DELETE /api/habitacion/{id} THEN: Retorna 24 No Content")
    void shouldDeleteHabitacion() throws Exception {
        Long idHabitacion = 1L;
        doNothing().when(habitacionService).eliminar(idHabitacion);

        mockMvc.perform(delete("/api/habitacion/" + idHabitacion).with(csrf()))
                .andExpect(status().isNoContent());
    }
}