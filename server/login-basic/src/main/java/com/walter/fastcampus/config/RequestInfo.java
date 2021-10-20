package com.walter.fastcampus.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestInfo {
    private LocalDateTime loginTime;
    private String remoteIp;
    private String sessionId;
}
