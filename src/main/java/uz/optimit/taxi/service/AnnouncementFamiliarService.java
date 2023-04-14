package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.Familiar;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.UserAlreadyExistException;
import uz.optimit.taxi.exception.UserNotFoundException;
import uz.optimit.taxi.model.request.FamiliarRegisterRequestDto;
import uz.optimit.taxi.repository.FamiliarRepository;
import uz.optimit.taxi.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static uz.optimit.taxi.entity.Enum.Constants.SUCCESSFULLY;
import static uz.optimit.taxi.entity.Enum.Constants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AnnouncementFamiliarService {

     private final FamiliarRepository familiarRepository;

     private final UserRepository userRepository;

     @ResponseStatus(HttpStatus.CREATED)
     public ApiResponse addForFamiliar(FamiliarRegisterRequestDto familiarRegisterRequestDto) {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

          if (authentication instanceof AnonymousAuthenticationToken){
               throw new UserNotFoundException(USER_NOT_FOUND);
          }
          User principal = (User) authentication.getPrincipal();
          User user = userRepository.findByPhone(principal.getPhone())
              .orElseThrow(() -> new UserNotFoundException("user not found"));
          if (familiarRepository.existsByPhoneAndUserId(familiarRegisterRequestDto.getPhone(),user.getId())){
               throw new UserAlreadyExistException("familiar already exist");
          }
          familiarRepository.save(Familiar.from(familiarRegisterRequestDto, user));
          return new ApiResponse(SUCCESSFULLY, true);
     }
     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getFamiliarListByUser() {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          if (authentication instanceof AnonymousAuthenticationToken){
               throw new UserNotFoundException(USER_NOT_FOUND);
          }
          User principal = (User) authentication.getPrincipal();
          User user = userRepository.findByPhone(principal.getPhone())
              .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
          List<Familiar> familiarList = familiarRepository.findAllByUserId(user.getId());

          return new ApiResponse(SUCCESSFULLY, true, getFamiliars(familiarList));
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getFamiliarListByUserId(List<UUID> uuidList) {
          List<Familiar> familiarList = new ArrayList<>();
          for (UUID uuid : uuidList) {
               Optional<Familiar> byId = familiarRepository.findById(uuid);
               byId.ifPresent(familiarList::add);
          }
          return new ApiResponse(SUCCESSFULLY, true, getFamiliars(familiarList));
     }

     private List<Familiar> getFamiliars(List<Familiar> familiarList) {
          List<Familiar> familiars = new ArrayList<>();
          for (Familiar familiar : familiarList) {
               familiars.add(getFamiliar(familiar));
          }
          return familiars;
     }

     private Familiar getFamiliar(Familiar familiar) {
          return Familiar.builder()
              .name(familiar.getName())
              .age(familiar.getAge())
              .id(familiar.getId())
              .status(familiar.getStatus())
              .phone(familiar.getPhone())
              .gender(familiar.getGender())
              .build();
     }
}
