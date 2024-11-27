package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails user1 = User.withDefaultPasswordEncoder()
                .username("player1")
                .password("password1")
                .roles("PLAYER")
                .build();

        UserDetails user2 = User.withDefaultPasswordEncoder()
                .username("player2")
                .password("password2")
                .roles("PLAYER")
                .build();

        UserDetails user3 = User.withDefaultPasswordEncoder()
                .username("player3")
                .password("password3")
                .roles("PLAYER")
                .build();

        return new InMemoryUserDetailsManager(user1, user2, user3);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/game/**").authenticated()
                        .anyRequest().permitAll())
                .httpBasic(withDefaults());

        return http.build();
    }
}





