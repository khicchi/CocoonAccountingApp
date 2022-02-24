package com.cocoon.config;

import com.cocoon.service.SecurityService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityService securityService;
    private final com.cocoon.config.AuthSuccessHandler authSuccessHandler;

    public SecurityConfig(SecurityService securityService, com.cocoon.config.AuthSuccessHandler authSuccessHandler) {
        this.securityService = securityService;
        this.authSuccessHandler = authSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/user/**").hasAnyAuthority("ROOT","ADMIN")
                .antMatchers("/company/**").hasAnyAuthority("ROOT")
                .antMatchers("/category/**").hasAnyAuthority("ADMIN", "MANAGER","EMPLOYEE")
                .antMatchers("/product/**").hasAnyAuthority("ADMIN", "MANAGER","EMPLOYEE")
                .antMatchers("/invoice/**").hasAnyAuthority("ADMIN", "MANAGER","EMPLOYEE")
                .antMatchers("/report/**").hasAnyAuthority("ADMIN", "MANAGER")
                .antMatchers("/payment/**").hasAnyAuthority("ADMIN")
                .antMatchers(
                        "/",
                        "/login",
                        "/images/**",
                        "/static/**",
                        "/html-template/**"
                ).permitAll()
                .and()
                .formLogin()
                    .loginPage("/login")
                    // everything happens here
                    .successHandler(authSuccessHandler)   // hangi rol hangi sayfaya yönlendirilecek?...
                    .failureUrl("/login?error=true")
                    .permitAll()
                .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                .and()
                .rememberMe()
                .tokenValiditySeconds(120)
                .key("cocoonSecret")
                .userDetailsService(securityService);

    }
}
