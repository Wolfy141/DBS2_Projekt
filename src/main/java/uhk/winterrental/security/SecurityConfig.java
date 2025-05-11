package uhk.winterrental.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import uhk.winterrental.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    private final PasswordEncoder passwordEncoder;
    private UserService userService;

    @Autowired
    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Admin-only paths
                        .requestMatchers("/admin/**", "/templates/admin.html").hasRole("ADMIN")

                        // Publicly accessible paths
                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**").permitAll()

                        // All other requests
                        .anyRequest().permitAll()
                )
                //.formLogin(Customizer.withDefaults())
                .formLogin((form) -> form
                        .loginPage("/login") // Custom login page
                        .loginProcessingUrl("/login") // Form submission URL
                        .defaultSuccessUrl("/", true)
                        .permitAll()) // Permit all to access the login page
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .exceptionHandling((exceptions) -> exceptions
                        .accessDeniedHandler(accessDeniedHandler()));

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.sendRedirect("/403");
        };
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
