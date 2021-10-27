package com.walter.fastcampus.controller;

import com.walter.fastcampus.student.Student;
import com.walter.fastcampus.student.StudentManager;
import com.walter.fastcampus.teacher.Teacher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 모바일 Security를 확인하기 위한 코드
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teacher")
public class ApiTeacherController {

    private final StudentManager studentManager;

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/students")
    public List<Student> studentList(@AuthenticationPrincipal Teacher teacher){


        log.info("controller {}", studentManager.myStudentList(teacher.getId()));
        return studentManager.myStudentList(teacher.getId());

    }

}
