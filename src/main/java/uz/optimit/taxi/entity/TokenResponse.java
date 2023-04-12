package uz.optimit.taxi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken;

     public TokenResponse(String refreshToken) {
          this.refreshToken=refreshToken;
     }
}
