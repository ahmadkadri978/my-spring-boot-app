package com.kadri.springboot.employee.security;


import com.kadri.springboot.employee.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class DemoSecurityConfig {

    //bcrypt bean definition
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //authenticationProvider bean definition
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService); //set the custom user details service
        auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http , AuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {

        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/list").hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
                                .requestMatchers("/logout").permitAll()
                                .requestMatchers("/addEmployee/**").hasRole("MANAGER")
                                .requestMatchers("/showFormForUpdate/**").hasRole("MANAGER")
                                .requestMatchers("/downloadReport/**").hasRole("MANAGER")
                                .requestMatchers("/deleteTask/**").hasRole("MANAGER")
                                .requestMatchers("/delete/**").hasRole("ADMIN")
                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers("/uploadReport/**").hasRole("EMPLOYEE")
                                .requestMatchers("/employee/{employeeId}/tasks/{id}").hasRole("EMPLOYEE")
                                .requestMatchers("/employee/{employeeId}/tasks/{id}/deleteReport").hasRole("EMPLOYEE")
                                .requestMatchers("/employee/{employeeId}/tasks/{id}/uploadReport").hasRole("EMPLOYEE")
                                .requestMatchers("/employee/{employeeId}/tasks").hasAnyRole("EMPLOYEE", "MANAGER")

                                .requestMatchers("/employee/{employeeId}/tasks/{id}/downloadReport").hasRole("MANAGER")
                                .requestMatchers("/employee/{employeeId}/tasks/{id}/edit").hasRole("MANAGER")
                                .requestMatchers("/employee/{employeeId}/tasks/{id}/save").hasRole("MANAGER")
                                .requestMatchers("/employee/{employeeId}/tasks/{id}/delete").hasRole("MANAGER")
                                .requestMatchers("/employee/{employeeId}/addtask").hasRole("MANAGER")
                                .requestMatchers("/").permitAll()
                                .anyRequest().authenticated()


                )

                .formLogin(form ->
                        form
                                .loginPage("/showMyLoginPage")

                                .loginProcessingUrl("/authenticateTheUser")
                                .successHandler(customAuthenticationSuccessHandler)
                                .permitAll()
                )


                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling((configurer ->
                                configurer
                                        .accessDeniedPage("/access-denied")

                        )


                );

        return http.build();

    }

}