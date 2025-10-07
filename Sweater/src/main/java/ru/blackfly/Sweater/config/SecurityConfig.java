package ru.blackfly.Sweater.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.blackfly.Sweater.services.PersonDetailsService;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;

    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }
    //Настроим метод позволяющий настроить Spring security, а так же авторизацию
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//  Отключаем CSRF
        http.csrf(AbstractHttpConfigurer::disable);
//  Настраиваем авторизацию
        http.authorizeHttpRequests((requests) ->
                requests
                        .requestMatchers("/auth/login", "/process_login", "/error").permitAll()
                        .anyRequest().authenticated()
        );
// Настраиваем страницу логина
        http.formLogin((form) ->
                form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/auth/login?error")
                        .permitAll()
        );
//  Настраиваем logout
        http.logout((logout) ->
                logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .permitAll()
        );

        return http.build();
    }

    //Это метод для настройки аутентификации
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(personDetailsService);
        provider.setPasswordEncoder(getPasswordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
