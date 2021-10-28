package com.walter.fastcampus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walter.fastcampus.student.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 해결함. => 원인 : 모바일 환경에서 이용되는 HttpBasic는 DaoUserNamePasswordAuthenticationToken을 이용한다.
 * 따라서, StudentManager와 TeacherManager에서 HttpBasic처리를 위한 코드를 support에 적어주어야 하며, token을 구분해서 발급해야 한다.
 *
 * 테스트 실패. 실제 서버를 돌리면 되는데, 테스트 코드로는 계속 실패한다.
 * 원하는 값 : studentManager.myStudentList()를 불러와서 student 리스트를 출력해야 한다.
 * 나오는 값 : LoginForm의 HTML 태그를 출력하고 있다.
 * 예상되는 원인 : MobileSecurityConfig와 SeucrityConfig로 두 개의 설정파일이 동작하는 과정에서 RestController인 경우 MobileSecurityConfig를 넘어가면 바로 출력값을 뱉어야 하는데 SecurityConfig도 통과하는 듯 하다.
 *
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MultiChainProxyTest {


    @LocalServerPort
    int port;

    RestTemplate restTemplate = new RestTemplate();


    TestRestTemplate testTemplate = new TestRestTemplate("jo", "1");
    @DisplayName("1. jo선생의 학생 받기")
    @Test
    void test_2(){

        ResponseEntity<List<Student>> resp = testTemplate.exchange("http://localhost:" + port + "/api/teacher/students",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
                });

        assertNotNull(resp.getBody());
        System.out.println(resp.getBody());
        assertEquals(3, resp.getBody().size());

    }


}
