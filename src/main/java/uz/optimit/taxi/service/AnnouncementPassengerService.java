package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.AnnouncementPassenger;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.exception.UserNotFoundException;
import uz.optimit.taxi.model.request.AnnouncementPassengerRegisterRequestDto;
import uz.optimit.taxi.repository.AnnouncementPassengerRepository;
import uz.optimit.taxi.repository.RegionRepository;
import uz.optimit.taxi.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AnnouncementPassengerService {
    private final AnnouncementPassengerRepository repository;
    private final RegionRepository regionRepository;
    private final UserRepository userRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> add(AnnouncementPassengerRegisterRequestDto announcementPassengerRegisterRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new UserNotFoundException("User not found");
        }
        String userPhone = (String) authentication.getPrincipal();
        User user = userRepository.findByPhone(userPhone).orElseThrow(() -> new UserNotFoundException("user not found"));
        AnnouncementPassenger announcementPassenger = AnnouncementPassenger.from(announcementPassengerRegisterRequestDto, user, regionRepository);
        repository.save(announcementPassenger);
        return new ResponseEntity<>("Successfully",HttpStatus.CREATED);
    }
}
