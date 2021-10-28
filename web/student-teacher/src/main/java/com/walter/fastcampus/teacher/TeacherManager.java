package com.walter.fastcampus.teacher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

@Component
public class TeacherManager implements AuthenticationProvider, InitializingBean {

    private HashMap<String, Teacher> teacherDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            if (teacherDB.containsKey(token.getName())) {
                return getAuthenticationToken(token.getName());
            }

            return null;
        }
        TeacherAuthenticationToken token = (TeacherAuthenticationToken)  authentication;
        if (teacherDB.containsKey(token.getCredentials())) {
            return getAuthenticationToken(token.getCredentials());
        }

        //처리할 수 없는 authenticationtoken은 null로 처리
        return null;
    }

    private TeacherAuthenticationToken getAuthenticationToken(String id) {
        Teacher teacher = teacherDB.get(id);
        return TeacherAuthenticationToken.builder()
                .principal(teacher)
                .details(teacher.getUsername())
                .authenticated(true)
                .build();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //Mobile과 Web Security 멀티 체인 프록시를 위해서는
        //웹에서 사용하는 커스텀(TeacherAuthenticationToken)과
        //모바일에서 사용하는 HttpBasic을 모두 지원하도록 설정한다.
        return authentication == TeacherAuthenticationToken.class || authentication == UsernamePasswordAuthenticationToken.class;
    }

    //initializingBean
    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new Teacher("jo", "조선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER"))),
                new Teacher("so", "소선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER"))),
                new Teacher("bom", "봄선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER")))
        ).forEach(s -> teacherDB.put(s.getId(), s));
    }
}
