package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.Region;
import uz.optimit.taxi.exception.RecordAlreadyExistException;
import uz.optimit.taxi.model.request.RegionRegisterRequestDto;
import uz.optimit.taxi.repository.RegionRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;

  @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addRegion(RegionRegisterRequestDto regionRegisterRequestDto) {
        Optional<Region> byName = regionRepository.findByName(regionRegisterRequestDto.getName());
        if (byName.isPresent()) {
            throw new RecordAlreadyExistException("Region already have");
        }
        Region region = Region.builder().name(regionRegisterRequestDto.getName()).build();
        regionRepository.save(region);
        return new ResponseEntity<>("Successfully" , HttpStatus.CREATED);
    }
}
