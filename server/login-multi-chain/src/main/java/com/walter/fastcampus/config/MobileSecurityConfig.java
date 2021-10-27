package com.walter.fastcampus.config;

import com.walter.fastcampus.student.StudentManager;
import com.walter.fastcampus.teacher.TeacherManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Order(1)
@Configuration
@RequiredArgsConstructor
public class MobileSecurityConfig extends WebSecurityConfigurerAdapter {


    private final TeacherManager teacherManager;
    private final StudentManager studentManager;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(teacherManager);
        auth.authenticationProvider(studentManager);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
                .csrf().disable()
                .authorizeRequests(request -> request.anyRequest().authenticated())
                .httpBasic();
    }

}
