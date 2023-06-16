package ua.shplusplus.restapidevelopment.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  @Value("${application.security.password.user}")
  private String passwordUser;

  @Value("${application.security.password.admin}")
  private String passwordAdmin;

  @Value("${application.security.endpoint.path.uri}")
  private String path;
  public static final String ROLE_ADMIN = "ADMIN";
  public static final String ROLE_USER = "USER";

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, path).hasAnyRole(ROLE_ADMIN, ROLE_USER)
        .antMatchers(HttpMethod.POST, path).hasRole(ROLE_ADMIN)
        .antMatchers(HttpMethod.PUT, path).hasAnyRole(ROLE_ADMIN, ROLE_USER)
        .antMatchers(HttpMethod.DELETE, path).hasRole(ROLE_ADMIN)
        .and()
        .httpBasic();
    return http.build();
  }

  @Bean
  public InMemoryUserDetailsManager userDetailsService() {

    UserDetails user = User.builder()
        .username("user")
        .password(passwordEncoder().encode(passwordUser))
        .roles(ROLE_USER)
        .build();

    UserDetails admin = User.builder()
        .username("admin")
        .password(passwordEncoder().encode(passwordAdmin))
        .roles(ROLE_ADMIN)
        .build();

    return new InMemoryUserDetailsManager(user, admin);
  }

  @Bean
  protected PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}