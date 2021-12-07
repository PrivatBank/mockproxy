package com.pb.mockproxy.config;

import com.pb.mockproxy.bean.auth.role.Roles;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/*.js", "/*.css", "favicon.ico").permitAll()
                    .anyRequest()
                    .hasAuthority(Roles.USER)
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?error=1")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login");

    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/health", "/test-service/**");
    }
}