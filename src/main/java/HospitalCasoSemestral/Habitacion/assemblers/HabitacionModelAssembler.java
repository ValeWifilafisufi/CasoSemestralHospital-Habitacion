package HospitalCasoSemestral.Habitacion.assemblers;

import HospitalCasoSemestral.Habitacion.controller.HabitacionController;
import HospitalCasoSemestral.Habitacion.dto.HabitacionResponseDTO;
import HospitalCasoSemestral.Habitacion.model.Habitacion;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HabitacionModelAssembler implements RepresentationModelAssembler<HabitacionResponseDTO, EntityModel<HabitacionResponseDTO>> {

    @Override
    public EntityModel<HabitacionResponseDTO> toModel(HabitacionResponseDTO hab){
        return EntityModel.of(hab,
                linkTo(methodOn(HabitacionController.class)
                        .obtenerPorNroHab(hab.getNro_habitacion()))
                        .withSelfRel(),
                linkTo(methodOn(HabitacionController.class)
                        .obtenerTodos(null, null))
                        .withRel("Todas-las-habitaciones")
        );
    }
}