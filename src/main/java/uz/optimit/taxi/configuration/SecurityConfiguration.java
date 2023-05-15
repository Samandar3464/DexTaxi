package uz.optimit.taxi.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import uz.optimit.taxi.configuration.jwtConfig.JwtFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final String[] WHITE_LINE = new String[]{
            "/**",
            "/api/v1/city/**",
            "/api/v1/region/**",
            "/api/v1/driver/**",
            "/api/v1/passenger/**",
            "/api/v1/user/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
    };
    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOriginPatterns(List.of("*"));
                    configuration.setAllowedMethods(List.of("GET", "POST",  "PUT", "DELETE", "PATCH"));
                    configuration.setAllowedHeaders(List.of("*"));
                    configuration.setAllowCredentials(true);
                    return configuration;
                })
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(WHITE_LINE)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
