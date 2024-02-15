package com.db.pratice.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //Users and their details are stored in memory during runtime

    //Only THREE Credentails in InMemoryUserDetailsManager
    @Bean
    public InMemoryUserDetailsManager userDetails(){
        UserDetails admin = User.withUsername("admin")
                .password("{noop}password")
                .roles("ADMINISTRATOR")
                .build();

        //For all the Users same password and username
        UserDetails user = User.withUsername("USER")
                .password("{noop}USER@123")
                .roles("USER")
                .build();

        UserDetails SENIOUR_EMPLOYEE = User.withUsername("EMPLOYEE")
                .password("{noop}EMPLOYEE@123")
                .roles("SENIOUR_EMPLOYEE")
                .build();
        return new InMemoryUserDetailsManager(admin,user,SENIOUR_EMPLOYEE);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/updateUser/**").hasRole("ADMINISTRTOR")
                        .requestMatchers("/getAllUsersNames","/getRoles").hasAnyRole("ADMINISTARTOR","SENIOUR_EMPLOYEE")
                        .requestMatchers(HttpMethod.GET).permitAll()
                        .requestMatchers(HttpMethod.DELETE).hasAnyRole("ADMINISTRATOR","USER")
                        .requestMatchers(HttpMethod.POST).hasAnyRole("ADMINISTRATOR")
                        .anyRequest()
                .authenticated())
                .httpBasic(Customizer.withDefaults()) //Customizer.withDefaults() is used to apply default configurations for form-based login and HTTP Basic authentication.
                .build();
    }
/*
    //@Autowired
    //private CustomerUserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public UserDetailsService getDetailsService(){
        return new CustomerUserDetailsService();
    }
   @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(getDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
*/
}
