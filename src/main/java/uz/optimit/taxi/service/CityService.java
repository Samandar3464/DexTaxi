package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.City;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.RecordAlreadyExistException;
import uz.optimit.taxi.model.request.CityRequestDto;
import uz.optimit.taxi.repository.CityRepository;
import uz.optimit.taxi.repository.RegionRepository;

import java.util.Optional;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class CityService {

     private final CityRepository cityRepository;

     private final RegionRepository repository;

     @ResponseStatus(HttpStatus.CREATED)
     public ApiResponse saveCity(CityRequestDto cityRequestDto) {
          Optional<City> byName = cityRepository.findByNameAndRegionId(cityRequestDto.getName(),cityRequestDto.getRegionId());
          if (byName.isPresent()) {
               throw new RecordAlreadyExistException(CITY_ALREADY_EXIST);
          }

          City city = City.builder()
              .name(cityRequestDto.getName()).region(repository.findById(cityRequestDto.getRegionId()).get()).build();
          City save = cityRepository.save(city);
          return new ApiResponse(SUCCESSFULLY, true,save);
     }
     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getCityList() {
          return new ApiResponse(cityRepository.findAll(),true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getCityById(Integer id) {
          return new ApiResponse(cityRepository.findById(id).get(),true);
     }
}
