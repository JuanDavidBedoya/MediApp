package com.mediapp.juanb.juanm.mediapp.mappers;

import com.mediapp.juanb.juanm.mediapp.dtos.CityRequestDTO;
import com.mediapp.juanb.juanm.mediapp.dtos.CityResponseDTO;
import com.mediapp.juanb.juanm.mediapp.entities.City;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {

    public CityResponseDTO toResponseDTO(City city) {
        if (city == null) {
            return null;
        }
        return new CityResponseDTO(
            city.getIdCity(),
            city.getName()
        );
    }

    public City toEntity(CityRequestDTO cityDTO) {
        if (cityDTO == null) {
            return null;
        }
        City city = new City();
        city.setName(cityDTO.name());
        return city;
    }
}
