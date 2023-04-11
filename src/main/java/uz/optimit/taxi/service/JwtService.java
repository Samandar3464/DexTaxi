package uz.optimit.taxi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String ACCESS_SECRET_KEY = "404E635266556A586E327235753878F413F4428472B4B6250645367566B5970";
    private static final String REFRESH_SECRET_KEY = "404E635266556A586E327235753878F413F4428472B4B6250645lll367566B5970";
    private static final int REFRESH_SECRET_TIME = 10 * 60 * 1000;
    private static final int ACCESS_SECRET_TIME = 1000 * 60 * 10 * 24;


    //GENERATE TOKENS

    public String generateAccessToken(String phoneNumber) {
        return generateAccessToken(new HashMap<>(), phoneNumber);
    }

    public String generateRefreshToken(String phoneNumber) {
        return generateRefreshToken(new HashMap<>(), phoneNumber);
    }

    public String generateAccessToken(
            Map<String, Objects> extraClaims,
            String phoneNumber) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(phoneNumber)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_SECRET_TIME))
                .signWith(getSingInKeyAccess(), SignatureAlgorithm.HS256)
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
            return null;
        }
    }


    //refresh
    public String extraRefreshToken(String token) {
        return extraRefreshClaim(token, Claims::getSubject);
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
