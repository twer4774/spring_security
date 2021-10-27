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
 * 테스트 실패. 실제 서버를 돌리면 되는데, 테스트 코드로는 계속 실패한다.
 * 원하는 값 : studentManager.myStudentList()를 불러와서 student 리스트를 출력해야 한다.
 * 나오는 값 : LoginForm의 HTML 태그를 출력하고 있다.
 * 예상되는 원인 : MobileSecurityConfig와 SeucrityConfig로 두 개의 설정파일이 동작하는 과정에서 RestController인 경우 MobileSecurityConfig를 넘어가면 바로 출력값을 뱉어야 하는데 SecurityConfig도 통과하는 듯 하다.
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MultiChainProxyTest {


    @LocalServerPort
    int port;

    RestTemplate restTemplate = new RestTemplate();

    @DisplayName("1. 학생 조사")
    @Test
    void test_1() throws JsonProcessingException {
        String url = format("http://localhost:%d/api/teacher/students", port);


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(
                "jo:1".getBytes()
        ));

        HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        List<Student> list = new ObjectMapper().readValue(response.getBody(), new TypeReference<List<Student>>() {});

        System.out.println(list);
        assertEquals(3, list.size());

    }

    TestRestTemplate testTemplate = new TestRestTemplate("jo", "1");
    @DisplayName("2")
    @Test
    void test_2(){


        ResponseEntity<List<Student>> resp = testTemplate.exchange("http://localhost:" + port + "/api/teacher/students",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
                });

        assertNotNull(resp.getBody());
        assertEquals(3, resp.getBody().size());

    }
}
