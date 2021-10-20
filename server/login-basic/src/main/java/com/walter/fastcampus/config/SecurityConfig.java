package com.walter.fastcampus.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthDetail customAuthDetail;

    public SecurityConfig(CustomAuthDetail customAuthDetail) {
        this.customAuthDetail = customAuthDetail;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(
                        User.builder()
                                .username("user1")
                                .password(encodePW("1111"))
                                .roles("USER")
                )
                .withUser(
                        User.builder()
                                .username("admin")
                                .password(encodePW("2222"))
                                .roles("ADMIN")
                );
    }

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(

                        request -> request.antMatchers("/").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(
                        login -> login.loginPage("/login")
                                .loginProcessingUrl("/loginprocess")
                                .permitAll() //반드시 설정 필요. 없으면 무한루프에 빠진다.
                                .defaultSuccessUrl("/", false)
                                .authenticationDetailsSource(customAuthDetail)
                                .failureUrl("/login-error")
                )
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .exceptionHandling(error -> error.accessDeniedPage("/access-denied"))
        ;
    }

    //web resources가 시큐리티에 걸리지 않도록 설정(static 파일의 css, js 등을 쓸 수 있게 한다.)
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    private String encodePW(String pw) {
        return passwordEncoder().encode(pw);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
