package pl.bartlomiej.emailverifydemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
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
                                .requestMatchers("/users").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers("/logs/**").hasAuthority("ADMIN")
                ).formLogin(formLoginCustomizer -> //todo: make JSON for frontend endpoint for login
                        formLoginCustomizer
                                .loginPage("/login") //todo: this own login endpoint
                                .permitAll()
                ).logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login"));
        return httpSecurity.build();
    }
}
