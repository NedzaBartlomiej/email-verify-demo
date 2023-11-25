package pl.bartlomiej.emailverifydemo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/users/*/roles").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/logs/**").hasAuthority("ADMIN")
                ).formLogin(formLoginConfigurer ->
                        formLoginConfigurer
                                .defaultSuccessUrl("/users", true)
                ).logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login"));
        return httpSecurity.build();
    }
}
