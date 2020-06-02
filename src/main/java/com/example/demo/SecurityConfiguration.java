package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .withDefaultSchema()
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

    // using inMemory database
//@Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user")
//                .password("user3")
//                .roles("USER")
//            .and()
//            .withUser("admin")
//            .password("admin3")
//            .roles("ADMIN")
//                .and()
//                .withUser("manager")
//                .password("manager")
//                .roles("MANAGER");
//    }

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
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/").authenticated()
            .and()
            .formLogin()
            .loginPage("/login").permitAll();
    }

}
