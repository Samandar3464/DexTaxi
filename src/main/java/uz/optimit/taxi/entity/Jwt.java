package uz.optimit.taxi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Jwt {
    private String accessToken;
    private String refreshToken;
}
