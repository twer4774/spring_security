package com.walter.fastcampus.config;

import com.walter.fastcampus.student.StudentManager;
import com.walter.fastcampus.teacher.TeacherManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final TeacherManager teacherManager;
    private final StudentManager studentManager;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(teacherManager);
        auth.authenticationProvider(studentManager);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomLoginFilter filter = new CustomLoginFilter(authenticationManager());

        http.authorizeRequests(request -> request.antMatchers("/", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(
                        login ->
                                login.loginPage("/login")
                                        .permitAll()
                                        .defaultSuccessUrl("/", false)
                                        .failureUrl("/login-error")
                )
                //위의 formLogin 동작을 customfilter로 대체, 단. defaultSuccessUrl이나 failureUrl을 위해 추가적인 handler 구현이 필요하다.
                //꼼수로, 둘다 사용하면 필터는 필터대로, 위의 문제는 위에서 처리 된다.(현재 상태)
                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout-> logout.logoutSuccessUrl("/"))
                .exceptionHandling(e -> e.accessDeniedPage("/access-denied"))
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        ;
    }
}
