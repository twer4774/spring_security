# Study Spring Security

- FastCampus Spring Security
- https://github.com/jongwon/sp-fastcampus-spring-sec

## gralde 초기 설정

```
#github에서 clonse을 받는다.
#터미널에서
gradle init
1: basic
1: Groovy
```

### settings.gradle

```groovy
rootProject.name = 'spring_security'

["comp", "web", "server"].each {

    def compDir = new File(rootDir, it)
    if(!compDir.exists()){
        compDir.mkdirs()
    }

    compDir.eachDir {subDir ->

        def gradleFile = new File(subDir.absolutePath, "build.gradle")
        if(!gradleFile.exists()){
            gradleFile.text =
                    """
                    
                    dependencies {
                
                    }
                
                    """.stripIndent(20)
        }
        [
                "src/main/java/com/walter/fastcampus",
                "src/main/resources",
                "src/test/java/walter/fastcampus",
                "src/test/resources"
        ].each {srcDir->
            def srcFolder = new File(subDir.absolutePath, srcDir)
            if(!srcFolder.exists()){
                srcFolder.mkdirs()
            }
        }

        def projectName = ":${it}-${subDir.name}";
        include projectName
        project(projectName).projectDir = subDir
    }
}
```

- gradle sync 실행 시 comp, web, server 폴더 생성(서브 프로젝트)
- 각 서브 프로젝트에 basic-test 같이 폴더를 하나 생성한 뒤 다시 gralde sync를 실행하면, main, test를 생성해준다.

## 스프링 시큐리티

- 인증과 권한에 대한 모듈
- 인증 : 로그인을 통한 사용자 확인
- 권한 : 로그인 한 사용자가 접근할 수 있는 범위
  - ex) 관리자, 일반 유저

### 주의 사항

- SecurityConfig로 user를 생성하면, application.yml 에서 설정한 user 정보는 사용할 수 없다.

### yml

```yaml
server:
  port: 9050

spring:
  security:
    user:
      name: user1
      password: 1111
      roles: USER
```

### config

- Password를 암호화 하지 않으면 동작 시킬 수 없다.
- 

```java
package com.walter.fastcampus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //user 생성
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(User.builder()
                        .username("user2")
                        .password(encodedPW("2222"))
                        .roles("USER"))
                .withUser(User.builder()
                        .username("admin")
                        .password(encodedPW("2222"))
                        .roles("ADMIN"));
    }

    private String encodedPW(String pw){
        return passwordEncoder().encode(pw);
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests((requests) ->
                requests.antMatchers("/").permitAll() //모든 사용자가 접근 가능
                        .anyRequest().authenticated());
        http.formLogin();
        http.httpBasic();
    }
}
```

### SecurityMessage.java

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityMessage {

    private Authentication auth;
    private String message;
}
```

### controller

```java
package com.walter.fastcampus.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String index(){
        return "홈페이지";
    }

    //권한 확인
    @GetMapping("/auth")
    public Authentication auth(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping("/user")
    public SecurityMessage user(){
        return SecurityMessage.builder()
                .auth(SecurityContextHolder.getContext().getAuthentication())
                .message("User 정보")
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public SecurityMessage admin(){
        return SecurityMessage.builder()
                .auth(SecurityContextHolder.getContext().getAuthentication())
                .message("Admin 정보")
                .build();
    }
}
```

