package entelect.training.incubator.spring.flight.config;

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

    /**
     * Disclaimer! In a production system you will never store your credentials in either clear text or in the code.
     * It is done here so that development is both easy to understand and change.
     * The commented code below shows you how to connect to a DB. You will also want to use some kind of password encoding/hashing.
     */

    //    @Autowired
    //    private DataSource securityDataSource;
    //
    //    @Override
    //    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //        auth.jdbcAuthentication().dataSource(securityDataSource);
    //    }
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
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // !!! Disclaimer: NEVER DISABLE CSRF IN PRODUCTION !!!
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/flights/specials/**").hasAnyRole("LOYALTY_USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/flights/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/flights/**").hasAnyRole("SYSTEM", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        return http.build();
    }

}
