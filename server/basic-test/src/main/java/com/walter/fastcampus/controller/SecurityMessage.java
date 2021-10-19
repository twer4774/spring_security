package com.walter.fastcampus.controller;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
import org.springframework.security.core.Authentication;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityMessage {

    private Authentication auth;
    private String message;
}
