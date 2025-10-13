package com.mediapp.juanb.juanm.mediapp.mappers;

import com.mediapp.juanb.juanm.mediapp.dtos.EpsRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.EpsResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.Eps;
import org.springframework.stereotype.Component;

@Component
public class EpsMapper {

    public EpsResponseDTO toResponseDTO(Eps eps) {
        if (eps == null) {
            return null;
        }
        return new EpsResponseDTO(
            eps.getIdEps(),
            eps.getName()
        );
    }
    
    public Eps toEntity(EpsRequestDTO epsDTO) {
        if (epsDTO == null) {
            return null;
        }
        Eps eps = new Eps();
        eps.setName(epsDTO.name());
        return eps;
    }
}
