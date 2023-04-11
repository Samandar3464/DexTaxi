package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User , UUID> {
}
