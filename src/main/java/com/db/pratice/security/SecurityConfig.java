package com.db.pratice.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //Users and their details are stored in memory during runtime

    @Bean
    public InMemoryUserDetailsManager userDetails(){
        UserDetails employee = User.withUsername("Employee")
                .password("{noop}123456789")
                .roles("SeniourEmp")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password("{noop}password")
                .roles("ADMIN")
                .build();
        //For all the Users same password and username
        UserDetails user = User.withUsername("USER")
                .password("{noop}Dhanesh@123")
                .roles("user")
                .build();
        return new InMemoryUserDetailsManager(admin,employee,user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/updateUser/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET).hasRole("user")//.permitAll()
                        .requestMatchers(HttpMethod.POST).hasAnyRole("ADMIN","SeniourEmp")

                        .anyRequest()
                .authenticated())
                .httpBasic(Customizer.withDefaults()) //Customizer.withDefaults() is used to apply default configurations for form-based login and HTTP Basic authentication.
                .build();
    }

    //@Autowired
    //private CustomerUserDetailsService userDetailsService;
/*
   @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        //provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
*/

/*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
*/
}
