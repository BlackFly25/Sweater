package ru.blackfly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.blackfly.services.PersonDetailsService;

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
        /**
         * Ниже код для отключения CSRF, при его удалении - защита автоматически будет включена.
         * Но для корректной работы, нужно добавить этот токен в форму аутентификации
         * CSFR токен нужно добавить в 1 форму, остальные ее автоматически подтянут
         *
         * При включении CSRF, нельзя просто так разлогиниться!
         */
//  Отключаем CSRF
      //  http.csrf(AbstractHttpConfigurer::disable);
//  Настраиваем авторизацию
        http.authorizeHttpRequests((requests) ->
                requests
                        .requestMatchers("/admin").hasRole("ADMIN") // это для доступа к админской страничке.
                        .requestMatchers("/auth/login", "/process_login", "/error", "/auth/registration").permitAll()
                        //.anyRequest().hasAnyRole("USER", "ADMIN") это если мы прям по ролям хотим закрыть доступ!
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
                        .logoutSuccessUrl("/auth/login")
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
        provider.setPasswordEncoder(getPasswordEncoder());//не забываем и сюда добавить что бы пароль прогонялся Spring-ом (именно тут будет идти сравнение)
        return provider;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
