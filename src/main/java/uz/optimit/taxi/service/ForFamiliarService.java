package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.ForFamiliar;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.UserNotFoundException;
import uz.optimit.taxi.model.request.ForFamiliarRegisterRequestDto;
import uz.optimit.taxi.repository.ForFamiliarRepository;
import uz.optimit.taxi.repository.UserRepository;

import java.util.List;

import static uz.optimit.taxi.entity.Enum.Constants.SUCCESSFULLY;
import static uz.optimit.taxi.entity.Enum.Constants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ForFamiliarService {

    private final ForFamiliarRepository forFamiliarRepository;

    private final UserRepository userRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse addForFamiliar(List<ForFamiliarRegisterRequestDto> familiarRegisterRequestDtoList){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken){
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        User principal = (User) authentication.getPrincipal();
        User user = userRepository.findByPhone(principal.getPhone()).orElseThrow(() -> new UserNotFoundException("user not found"));

       familiarRegisterRequestDtoList.forEach(family->
               forFamiliarRepository.save(ForFamiliar.from(family, user)));
        return new ApiResponse(SUCCESSFULLY, true);
    }
}
