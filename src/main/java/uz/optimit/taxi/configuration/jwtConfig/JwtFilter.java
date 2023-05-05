package uz.optimit.taxi.configuration.jwtConfig;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import uz.optimit.taxi.exception.FileInputException;
import uz.optimit.taxi.exception.InputException;
import uz.optimit.taxi.service.AuthService;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static uz.optimit.taxi.entity.Enum.Constants.*;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final AuthService authService;

    @Qualifier("handlerExceptionResolver")
    @Autowired
    private  HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException {
        try {
            String requestHeader = request.getHeader(AUTHORIZATION);
            if (requestHeader == null || !requestHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = requestHeader.replace("Bearer ", "");
            boolean valid = JwtGenerate.isValid(token);
            if (valid) {
                Claims claims = JwtGenerate.isValidAccessToken(token);
                if (claims == null) {
                    filterChain.doFilter(request, response);
                    return;
                }
                UserDetails userDetails = authService.loadUserByUsername(claims.getSubject());
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
            }
        } catch(ExpiredJwtException | SignatureException | UnsupportedJwtException | MalformedJwtException |
                 IllegalArgumentException e) {
            resolver.resolveException(request, response, null, e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        catch (IOException e){
//            throw new InputException(INPUT_EXCEPTION);
//        }
//        catch ( ServletException e){
//            throw new FileInputException(SOMETHING_WRONG);
//        }
    }
}
