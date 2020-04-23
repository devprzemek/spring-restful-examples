package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password(encoder().encode("user")).roles("USER")
                .and()
                .withUser("admin").password(encoder().encode("admin")).roles("USER", "ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/authors/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/add-event").permitAll()
                .antMatchers(HttpMethod.POST, "/authors/add").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/authors/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/authors/**").hasRole("ADMIN")
                .and().csrf().disable()
                .formLogin().disable();
    }
}
