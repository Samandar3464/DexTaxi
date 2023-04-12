package uz.optimit.taxi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.exception.RefreshTokeNotFound;
import uz.optimit.taxi.exception.TimeExceededException;
import uz.optimit.taxi.exception.UserNotFoundException;
import uz.optimit.taxi.repository.UserRepository;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final String ACCESS_SECRET_KEY = "404E635266556A586E327235753878F413F4428472B4B6250645367566B5970";
    private static final String REFRESH_SECRET_KEY = "404E635266556A586E327235753878F413F4428472B4B6250645lll367566B5970";
    private static final int REFRESH_SECRET_TIME = 10 * 60 * 1000;
    private static final int ACCESS_SECRET_TIME = 1000 * 60 * 10 * 24;
    private final UserRepository userRepository;

    //GENERATE TOKENS

    public String generateAccessToken(User user) {
        return generateAccessToken(new HashMap<>(), user);
    }

    public String generateRefreshToken(String phoneNumber) {
        return generateRefreshToken(new HashMap<>(), phoneNumber);
    }

    public String generateAccessToken(
            Map<String, Objects> extraClaims,
            User user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getPhone())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_SECRET_TIME))
                .signWith(getSingInKeyAccess(), SignatureAlgorithm.HS256)
                .claim("authorities", user.getAuthorities())
                .compact();
    }

    private String generateRefreshToken(
            Map<String, Object> extraClaims,
            String phoneNumber
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(phoneNumber)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_SECRET_TIME))
                .signWith(getSingInKeyRefresh(), SignatureAlgorithm.HS256)
                .compact();
    }


    // check for access token

    public boolean isAccessTokenValid(String token, String phoneNumber) {
        final String phoneNum = extraAccessToken(token);
        return (phoneNum.equals(phoneNumber)) && !isAccessTokenExpired(token);
    }

    private boolean isAccessTokenExpired(String token) {
        return extraAccessExpiration(token).before(new Date());
    }

    private Date extraAccessExpiration(String token) {
        return extraAccessClaim(token, Claims::getExpiration);
    }


    public String getAccessTokenByRefresh(String token) {
        String username = extraRefreshToken(token);
        if (username==null){
            throw new TimeExceededException("Refresh token time out");
        }
        User user = userRepository.findByPhone(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        return generateAccessToken(user);
    }

    // check for refresh token

    public boolean isRefreshTokenValid(String token, String phoneNumber) {
        final String phoneNum = extraRefreshToken(token);
        return (phoneNum.equals(phoneNumber)) && !isRefreshTokenExpired(token);
    }

    private boolean isRefreshTokenExpired(String token) {
        return extraRefreshExpiration(token).before(new Date());
    }

    private Date extraRefreshExpiration(String token) {
        return extraRefreshClaim(token, Claims::getExpiration);
    }


    // EXTRA CLAIMS

    //access
    public String extraAccessToken(String token) {
        return extraAccessClaim(token, Claims::getSubject);
    }

    public <T> T extraAccessClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraAllAccessClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extraAllAccessClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSingInKeyAccess())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
           throw e;
        }
    }


    //refresh
    public String extraRefreshToken(String token) {
        try {
            return extraRefreshClaim(token, Claims::getSubject);
        }catch (Exception e){
            throw e;
        }

    }

    public <T> T extraRefreshClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraAllRefreshClaims(token);
        return claimsResolver.apply(claims);
    }


    private Claims extraAllRefreshClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSingInKeyRefresh())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }


    //GENERATE KEYS
    private SecretKey getSingInKeyRefresh() {
        byte[] decode = Decoders.BASE64.decode(REFRESH_SECRET_KEY);
        return Keys.hmacShaKeyFor(decode);
    }

    private SecretKey getSingInKeyAccess() {
        byte[] decode = Decoders.BASE64.decode(ACCESS_SECRET_KEY);
        return Keys.hmacShaKeyFor(decode);
    }
}
