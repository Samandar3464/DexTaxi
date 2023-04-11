package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.exception.UserNotFoundException;
import uz.optimit.taxi.model.request.AnnouncementDriverRegisterRequestDto;
import uz.optimit.taxi.repository.AnnouncementDriverRepository;
import uz.optimit.taxi.repository.RegionRepository;
import uz.optimit.taxi.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AnnouncementDriverService {
    private final AnnouncementDriverRepository repository;
    private final RegionRepository regionRepository;
    private final UserRepository userRepository;
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> add(AnnouncementDriverRegisterRequestDto announcementDriverRegisterRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getPrincipal().equals("anonymousUser")) {
            throw new UserNotFoundException("User not found");
        }
        String userPhone = (String) authentication.getPrincipal();
        User user = userRepository.findByPhone(userPhone).orElseThrow(() -> new UserNotFoundException("user not found"));
        AnnouncementDriver announcementDriver = AnnouncementDriver.from(announcementDriverRegisterRequestDto, user, regionRepository);
        repository.save(announcementDriver);
        return new ResponseEntity<>("Successfully",HttpStatus.CREATED);
    }
}
