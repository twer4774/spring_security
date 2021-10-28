package com.walter.fastcampus.student;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StudentManager implements AuthenticationProvider, InitializingBean {

    private HashMap<String, Student> studentDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            if (studentDB.containsKey(token.getName())) {
                return getAuthenticationToken(token.getName());
            }
            return null;
        }

        StudentAuthenticationToken token = (StudentAuthenticationToken)  authentication;

        if (studentDB.containsKey(token.getCredentials())) {
            return getAuthenticationToken(token.getCredentials());
        }
        //처리할 수 없는 authenticationtoken은 null로 처리
        return null;
    }

    private StudentAuthenticationToken getAuthenticationToken(String id) {
        Student student = studentDB.get(id);
        return StudentAuthenticationToken.builder()
                .principal(student)
                .details(student.getUsername())
                .authenticated(true)
                .build();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //Mobile과 Web Security 멀티 체인 프록시를 위해서는
        //웹에서 사용하는 커스텀(StudentAuthenticationToken)과
        //모바일에서 사용하는 HttpBasic을 모두 지원하도록 설정한다.
        return authentication == StudentAuthenticationToken.class || authentication == UsernamePasswordAuthenticationToken.class;
    }

    public List<Student> myStudents(String teacherId){
        return studentDB.values().stream().filter(s->s.getTeacherId().equals(teacherId))
                .collect(Collectors.toList());
    }

    //initializingBean
    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new Student("hong", "홍길동", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")), "jo"),
                new Student("kang", "강아지", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")), "jo"),
                new Student("rang", "호랑이", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")), "jo")
        ).forEach(s -> studentDB.put(s.getId(), s));
    }
}
