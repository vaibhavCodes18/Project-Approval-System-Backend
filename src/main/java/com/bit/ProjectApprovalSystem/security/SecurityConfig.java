package com.bit.ProjectApprovalSystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                // Public Endpoints
                                .requestMatchers("/api/v1/auth/register", "/api/v1/auth/login", "/api/v1/auth/refresh", "/api/v1/users/hod").permitAll()
                                .requestMatchers("/", "/error").permitAll() // Add /error to permitAll for proper exception handling

                                // Authenticated endpoints in Auth Controller
                                .requestMatchers("/api/v1/auth/me", "/api/v1/auth/logout").authenticated()

                                // HOD Exclusives
                                .requestMatchers("/api/v1/users/**").hasRole("HOD")
                                .requestMatchers("/api/v1/hod/**").hasRole("HOD")
                                .requestMatchers("/api/v1/dashboard/hod").hasRole("HOD")

                                // Guide Exclusives
                                .requestMatchers("/api/v1/guide/projects/all-with-projects").authenticated()
                                .requestMatchers("/api/v1/guide/**").hasRole("GUIDE")
                                .requestMatchers("/api/v1/dashboard/guide").hasRole("GUIDE")

                                // Student Dashboard
                                .requestMatchers("/api/v1/dashboard/student").hasRole("STUDENT")

                                // Projects Shared Access (Everyone can Read projects, members and approval history)
                                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/projects/**").hasAnyRole("STUDENT", "GUIDE", "HOD")

                                // Projects Write Access (Only Students mutate projects e.g., create, update, submit, members)
                                .requestMatchers("/api/v1/projects/**").hasRole("STUDENT")

                                .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder encoder){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(encoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
