package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.Familiar;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.UserAlreadyExistException;
import uz.optimit.taxi.model.request.FamiliarRegisterRequestDto;
import uz.optimit.taxi.repository.FamiliarRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class AnnouncementFamiliarService {

     private final FamiliarRepository familiarRepository;

     private final UserService userService;

     @ResponseStatus(HttpStatus.CREATED)
     public ApiResponse addForFamiliar(FamiliarRegisterRequestDto familiarRegisterRequestDto) {
          User user = userService.checkUserExistByContext();
          if (familiarRepository.existsByPhoneAndUserIdAndActive(familiarRegisterRequestDto.getPhone(), user.getId(), true)) {
               throw new UserAlreadyExistException(FAMILIAR_ALREADY_EXIST);
          }
          familiarRepository.save(Familiar.from(familiarRegisterRequestDto, user));
          return new ApiResponse(SUCCESSFULLY, true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getFamiliarListByUser() {
          User user = userService.checkUserExistByContext();
          List<Familiar> familiarList = familiarRepository.findAllByUserIdAndActive(user.getId(), true);
          return new ApiResponse(SUCCESSFULLY, true, familiarList);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getFamiliarListByUserId(List<UUID> uuidList) {
          List<Familiar> familiarList = familiarRepository.findByIdInAndActive(uuidList, true);
          return new ApiResponse(SUCCESSFULLY, true, familiarList);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse deleteFamiliar(UUID uuid) {
          Optional<Familiar> byId = familiarRepository.findById(uuid);
          byId.get().setActive(false);
          familiarRepository.save(byId.get());
          return new ApiResponse(DELETED, true);
     }
}
