package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.model.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {
}
