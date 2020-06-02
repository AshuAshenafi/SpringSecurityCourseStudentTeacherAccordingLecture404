package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password("user3")
                .roles("USER")
            .and()
            .withUser("admin")
            .password("admin3")
            .roles("ADMIN")
                .and()
                .withUser("manager")
                .password("manager")
                .roles("MANAGER");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/student").hasAnyRole("USER", "MANAGER")
                .antMatchers("/teacher").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/course").hasAnyRole("MANAGER")
                .antMatchers("/").authenticated()
            .and()
            .formLogin()
            .loginPage("/login").permitAll();
    }

}
