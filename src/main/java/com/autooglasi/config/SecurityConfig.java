package com.autooglasi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

/**
 * Centralna Spring Security konfiguracija.
 *
 * Pokriva ispitne stavke:
 *  - Autentifikacija/Autorizacija preko rola (BE): form login, BCrypt, hasRole zaštita
 *  - Upravljanje sesijama (umesto JWT refresh): session-fixation zaštita, concurrent
 *    session control, remember-me, logout invalidacija sesije.
 */
@Configuration
@EnableMethodSecurity   // omogucava @PreAuthorize na servisima/kontrolerima
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Javno dostupno: pocetna, lista oglasa, detalj, registracija, login, statika
                .requestMatchers(
                        "/", "/ads", "/ads/search",
                        "/register", "/login", "/access-denied",
                        "/css/**", "/js/**", "/images/**", "/webjars/**",
                        "/favicon.ico", "/h2-console/**"
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/ads/{id:[0-9]+}").permitAll()
                // Admin panel samo za ADMIN rolu
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Sve ostalo (npr. /ads/new, /ads/*/edit, /my-ads) zahteva prijavu
                .anyRequest().authenticated()
            )
            // ===== Form login (FE autentifikacija) =====
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            // ===== Logout: invalidacija sesije =====
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // ===== Remember-me (perzistentna prijava preko cookie-ja) =====
            .rememberMe(rm -> rm
                .key("auto-oglasi-remember-me-key")
                .tokenValiditySeconds(7 * 24 * 60 * 60) // 7 dana
                .rememberMeParameter("remember-me")
            )
            // ===== Upravljanje sesijama =====
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation(fixation -> fixation.migrateSession()) // nova sesija nakon login-a
                .maximumSessions(1)                                     // jedan aktivan login po nalogu
                .maxSessionsPreventsLogin(false)                        // novi login gasi stari
                .expiredUrl("/login?expired")
            )
            // ===== Pristup odbijen (403) =====
            .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"));

        // CSRF: koristimo obican (ne-XOR) handler da se token koji Thymeleaf iscrta
        // poklapa sa onim koji se validira; H2 konzola je izuzeta.
        http.csrf(csrf -> csrf
            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
            .ignoringRequestMatchers("/h2-console/**"));
        // Potrebno samo za H2 konzolu (frame embedding) u 'h2' profilu; bezopasno inace.
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}
