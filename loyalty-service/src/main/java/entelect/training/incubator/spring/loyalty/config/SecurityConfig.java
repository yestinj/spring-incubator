package entelect.training.incubator.spring.loyalty.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    InMemoryUserDetailsManager inMemoryAuthManager() {
        return new InMemoryUserDetailsManager(
                User.builder().username("user").password("{noop}the_cake").roles("USER").build(),
                User.builder().username("loyalty_user").password("{noop}the_cheese_cake")
                        .roles("USER", "LOYALTY_USER").build(),
                User.builder().username("admin").password("{noop}is_a_lie").roles("ADMIN").build()
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // !!! Disclaimer: NEVER DISABLE CSRF IN PRODUCTION !!!
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/ws/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/ws/**").hasAnyRole("USER", "SYSTEM", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        return http.build();
    }
}